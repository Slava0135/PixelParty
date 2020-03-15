package com.slava0135.pixelparty.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    final static Color background = Color.WHITE;
    final static int scale = 50;
    final static float unitScale = 0.3f;
    final static float unitRadius = unitScale;
    final static int border = scale * 2;
    final static float impulse = 0.1f;
    final static int floorSize = scale * Floor.size;
    final static int unitAmount = 100;
    double speedMultiplier = 1.05;
    double maxVelocity = 2;
    Integer score = 0;
    //rendering
    final PixelGame game;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    Stage stage;
    //data
    Random random = new Random();
    World world;
    Floor floor = new Floor();
    Array<Body> bodies = new Array<>();
    Body player;
    //timing
    double time = 0;
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

        stage = new Stage(new ScreenViewport());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, floorSize + 2 * border, floorSize + 2 * border);

        world = new World(new Vector2(0, 0),false);
        generateBorders();
        floor.generateFloor();

        spawnUnits(unitAmount);
        spawnPlayer();
    }

    @Override
    public void render(float delta) {
        time += delta;

        //graphics
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        floor.draw(border, border, scale);
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
                }
                break;
            }
            case BREAK: {
                if (isOver) {
                    Step.length /= speedMultiplier;
                    maxVelocity *= speedMultiplier;
                    floor.generateFloor();
                    score++;
                } else {
                    saveMove();
                    eliminate();
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
            moveBody(new Vector2((touchPos.x - border) / scale, (touchPos.y - border) / scale), player);
        }
        if (isDead(player)) {
            finishGame();
        }
    }

    private FixtureDef getCircleFixture() {
        CircleShape circle = new CircleShape();
        circle.setRadius(unitRadius);
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
        polygon.setRadius(unitRadius);
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
        bodyDef.position.set(random.nextInt(floorSize) / (float) scale, random.nextInt(floorSize) / (float) scale);
        player = world.createBody(bodyDef);
        player.createFixture(fixture);
    }

    private void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixture = getCircleFixture();
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(random.nextInt(floorSize) / (float) scale, random.nextInt(floorSize) / (float) scale);
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
                        body.applyLinearImpulse(impulse, 0, pos.x, pos.y, true);
                    }
                    break;
                }
                case 1: {
                    if (velX > -maxVelocity) {
                        body.applyLinearImpulse(-impulse, 0, pos.x, pos.y, true);
                    }
                    break;
                }
                case 2: {
                    if (velY < maxVelocity) {
                        body.applyLinearImpulse(0, impulse, pos.x, pos.y, true);
                    }
                    break;
                }
                default: {
                    if (velY > -maxVelocity) {
                        body.applyLinearImpulse(0, -impulse, pos.x, pos.y, true);
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
            body.applyLinearImpulse(impulse, 0, pos.x, pos.y, true);
        }
        if (destination.x < pos.x && velX > -maxVelocity) {
            body.applyLinearImpulse(-impulse, 0, pos.x, pos.y,true);
        }
        if (destination.y > pos.y && velY < maxVelocity) {
            body.applyLinearImpulse(0, impulse, pos.x, pos.y,true);
        }
        if (destination.y < pos.y && velY > -maxVelocity) {
            body.applyLinearImpulse(0, -impulse, pos.x, pos.y,true);
        }
    }

    private void drawBody(Body body, Color color) {
        Vector2 vector = body.getPosition();
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(vector.x * scale + border, vector.y * scale + border, unitRadius * scale);
        shapeRenderer.end();
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
        return !floor.isOnTile(vector.x, vector.y, unitRadius);
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
