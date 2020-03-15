package com.slava0135.pixelparty.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.slava0135.pixelparty.PixelGame;
import com.slava0135.pixelparty.World.Floor;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    private final static int SCALE = 50;
    private final static float UNIT_SCALE = 0.3f;
    private final static float UNIT_RADIUS = UNIT_SCALE;
    private final static int BORDER = SCALE * 2;
    private final static float IMPULSE = 0.1f;
    private final static int FLOOR_SIZE = SCALE * Floor.size;
    private final static int MAX_UNIT_AMOUNT = 50;
    private double speedMultiplier = 1.05;
    private double maxVelocity = 2;
    Integer score = 0;
    //rendering
    final PixelGame game;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Stage stage;
    SpriteBatch batch = new SpriteBatch();
    BitmapFont font;
    //data
    Random random = new Random();
    World world;
    Floor floor = new Floor();
    Array<Body> bodies = new Array<>();
    Body player;
    //timing
    private double time = 0;
    //staging
    Step step = Step.WAIT;
    enum Step {
        WAIT, RUN, BREAK;
        public static double length = 2;
        private static Step[] vals = values();
        public Step next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    public GameScreen(final PixelGame game) {
        this.game = game;

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        font = game.generator.generateFont(parameter);

        stage = new Stage(new ScreenViewport());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FLOOR_SIZE + 2 * BORDER, FLOOR_SIZE + 2 * BORDER);

        world = new World(new Vector2(0, 0),false);
        generateBorders();
        floor.generateFloor();

        spawnUnits(MAX_UNIT_AMOUNT);
        spawnPlayer();
    }

    @Override
    public void render(float delta) {
        time += delta;

        //graphics
        Gdx.gl.glClearColor(PixelGame.BACKGROUND.r, PixelGame.BACKGROUND.g, PixelGame.BACKGROUND.b, PixelGame.BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        floor.draw(BORDER, BORDER, SCALE);
        for (Body body: bodies) {
            drawBody(body, Color.BLACK);
        }
        drawBody(player, Color.WHITE);
        stage.act();
        stage.draw();
        world.step(1/60f, 6, 2);
        //logic
        boolean isOver = time > Step.length;
        switch(step) {
            case WAIT: {
                if (!isOver) {
                    randomMove();
                }
                break;
            }
            case RUN: {
                if (isOver) {
                    floor.throwFloor();
                } else {
                    saveMove();
                    printColor();
                }
                break;
            }
            case BREAK: {
                if (isOver) {
                    Step.length /= speedMultiplier;
                    maxVelocity *= speedMultiplier;
                    floor.generateFloor();
                    score++;
                    if (bodies.size < MAX_UNIT_AMOUNT) {
                        spawnUnits(MAX_UNIT_AMOUNT - bodies.size);
                    }
                } else {
                    saveMove();
                    eliminate();
                    printColor();
                }
                break;
            }
        }
        if (isOver) {
            time = 0;
            step = step.next();
        }
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            moveBody(new Vector2((touchPos.x - BORDER) / SCALE, (touchPos.y - BORDER) / SCALE), player);
        }
        if (isDead(player)) {
            finishGame();
        }
    }

    private FixtureDef getCircleFixture() {
        CircleShape circle = new CircleShape();
        circle.setRadius(UNIT_RADIUS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        circle.dispose();
        return fixtureDef;
    }

    private void generateBorders() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape polygon = new PolygonShape();
        polygon.setRadius(UNIT_RADIUS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        polygon.setAsBox(Floor.size / 2f - 0.3f, Floor.size / 2f - 0.3f);

        bodyDef.position.set(Floor.size / 2f, Floor.size * 3f / 2);
        Body up = world.createBody(bodyDef);
        up.createFixture(fixtureDef);

        bodyDef.position.set(Floor.size * 3f / 2, Floor.size / 2f);
        Body right = world.createBody(bodyDef);
        right.createFixture(fixtureDef);

        bodyDef.position.set(-Floor.size / 2f, Floor.size / 2f);
        Body left = world.createBody(bodyDef);
        left.createFixture(fixtureDef);

        bodyDef.position.set(Floor.size / 2f, -Floor.size / 2f);
        Body down = world.createBody(bodyDef);
        down.createFixture(fixtureDef);

        polygon.dispose();
    }

    private void spawnPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixture = getCircleFixture();
        bodyDef.position.set(random.nextInt(FLOOR_SIZE) / (float) SCALE, random.nextInt(FLOOR_SIZE) / (float) SCALE);
        player = world.createBody(bodyDef);
        player.createFixture(fixture);
    }

    private void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixture = getCircleFixture();
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(random.nextInt(FLOOR_SIZE) / (float) SCALE, random.nextInt(FLOOR_SIZE) / (float) SCALE);
            Body body = world.createBody(bodyDef);
            body.createFixture(fixture);
            bodies.add(body);
        }
    }

    private void randomMove() {
        for (Body body: bodies) {
            Vector2 pos = body.getPosition();
            Vector2 velocity = body.getLinearVelocity();
            double velX = velocity.x;
            double velY = velocity.y;
            int side = random.nextInt(4);
            switch (side) {
                case 0: {
                    if (velX < maxVelocity) {
                        body.applyLinearImpulse(IMPULSE, 0, pos.x, pos.y, true);
                    }
                    break;
                }
                case 1: {
                    if (velX > -maxVelocity) {
                        body.applyLinearImpulse(-IMPULSE, 0, pos.x, pos.y, true);
                    }
                    break;
                }
                case 2: {
                    if (velY < maxVelocity) {
                        body.applyLinearImpulse(0, IMPULSE, pos.x, pos.y, true);
                    }
                    break;
                }
                default: {
                    if (velY > -maxVelocity) {
                        body.applyLinearImpulse(0, -IMPULSE, pos.x, pos.y, true);
                    }
                    break;
                }
            }
        }
    }

    private void saveMove() {
        for (Body body: bodies) {
            Vector2 pos = body.getPosition();
            moveBody(floor.findClosest(pos.x, pos.y), body);
        }
    }

    private void moveBody(Vector2 destination, Body body) {
        Vector2 velocity = body.getLinearVelocity();
        double velX = velocity.x;
        double velY = velocity.y;
        Vector2 pos = body.getPosition();
        if (destination.x > pos.x && velX < maxVelocity) {
            body.applyLinearImpulse(IMPULSE, 0, pos.x, pos.y, true);
        }
        if (destination.x < pos.x && velX > -maxVelocity) {
            body.applyLinearImpulse(-IMPULSE, 0, pos.x, pos.y,true);
        }
        if (destination.y > pos.y && velY < maxVelocity) {
            body.applyLinearImpulse(0, IMPULSE, pos.x, pos.y,true);
        }
        if (destination.y < pos.y && velY > -maxVelocity) {
            body.applyLinearImpulse(0, -IMPULSE, pos.x, pos.y,true);
        }
    }

    private void drawBody(Body body, Color color) {
        Vector2 vector = body.getPosition();
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(vector.x * SCALE + BORDER, vector.y * SCALE + BORDER, UNIT_RADIUS * SCALE);
        shapeRenderer.end();
    }

    private void printColor() {
        batch.begin();
        font.setColor(floor.currentColor.color);
        font.draw(batch, floor.currentColor.name,300, 975);
        batch.end();
    }

    private void eliminate() {
        for (Iterator<Body> iter = bodies.iterator(); iter.hasNext(); ) {
            Body body = iter.next();
            if (isDead(body)) {
                world.destroyBody(body);
                bodies.removeValue(body, true);
            }
        }
    }

    private boolean isDead(Body body) {
        Vector2 vector = body.getPosition();
        return !floor.isOnTile(vector.x, vector.y, UNIT_RADIUS);
    }

    private void finishGame() {
        game.setScreen(new GameOverScreen(game, score));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        world.dispose();
        shapeRenderer.dispose();
        stage.dispose();
    }
}
