package com.slava0135.pixelparty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.slava0135.pixelparty.world.Floor;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    final static Color background = Color.LIGHT_GRAY;
    final static int scale = 50;
    final static float unitScale = 0.3f;
    final static float unitRadius = unitScale * scale;
    final static int border = scale * 2;
    final static int impulse = 5000;
    final static int floorSize = scale * Floor.size;
    final static int unitAmount = 100;
    double speedMultiplier = 1.05;
    double maxVelocity = 100;
    //rendering
    final PixelGame game;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer = new ShapeRenderer();
    //data
    Random random = new Random();
    World world;
    Floor floor = new Floor();
    Array<Body> bodies = new Array<>();
    Body player;
    //timing
    double time = 0;
    //staging
    Stage stage = Stage.WAIT;
    enum Stage {
        WAIT, RUN, BREAK;
        public static double length = 3;
        private static Stage[] vals = values();
        public Stage next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    public GameScreen(final PixelGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, floorSize + 2 * border, floorSize + 2 * border);

        world = new World(new Vector2(0, 0),false);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixture = getFixture();
        bodyDef.position.set(border + random.nextInt(floorSize), border + random.nextInt(floorSize));
        player = world.createBody(bodyDef);
        player.createFixture(fixture);

        floor.generateFloor();
        spawnUnits(unitAmount);
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
        //logic
        boolean isOver = time > Stage.length;
        switch(stage) {
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
                    Stage.length /= speedMultiplier;
                    maxVelocity *= speedMultiplier;
                    floor.generateFloor();
                } else {
                    saveMove();
                    eliminate();
                }
                break;
            }
        }
        if (isOver) {
            time = 0;
            stage = stage.next();
        }
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            moveBody(new Vector2((touchPos.x - border) / scale, (touchPos.y - border) / scale), player);
        }
        world.step(1/60f, 6, 2);
    }

    private FixtureDef getFixture() {
        CircleShape circle = new CircleShape();
        circle.setRadius(unitRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        circle.dispose();
        return fixtureDef;
    }

    private void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        FixtureDef fixture = getFixture();
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(border + random.nextInt(floorSize), border + random.nextInt(floorSize));
            Body body = world.createBody(bodyDef);
            body.createFixture(fixture);
            bodies.add(body);
        }
    }

    private void randomMove() {
        for (Body body: bodies) {
            body.applyLinearImpulse(
                    random.nextInt(2 * impulse) - impulse,
                    random.nextInt(2 * impulse) - impulse,
                    body.getPosition().x,
                    body.getPosition().y,
                    true);
        }
    }

    private void saveMove() {
        for (Body body: bodies) {
            Vector2 pos = body.getPosition();
            float x = (pos.x - border) / scale;
            float y = (pos.y - border) / scale;
            moveBody(floor.findClosest(x, y), body);
        }
    }

    private void moveBody(Vector2 destination, Body body) {
        Vector2 velocity = body.getLinearVelocity();
        double velX = velocity.x;
        double velY = velocity.y;
        Vector2 pos = body.getPosition();
        float x = (pos.x - border) / scale;
        float y = (pos.y - border) / scale;
        if (destination.x > x && velX < maxVelocity) {
            body.applyLinearImpulse(impulse, 0, x, y, true);
        }
        if (destination.x < x && velX > -maxVelocity) {
            body.applyLinearImpulse(-impulse, 0, x, y,true);
        }
        if (destination.y > y && velY < maxVelocity) {
            body.applyLinearImpulse(0, impulse, x, y,true);
        }
        if (destination.y < y && velY > -maxVelocity) {
            body.applyLinearImpulse(0, -impulse, x, y,true);
        }
    }

    private void drawBody(Body body, Color color) {
        Vector2 vector = body.getPosition();
        shapeRenderer.setColor(color);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(vector.x, vector.y, unitRadius);
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
        return !floor.isOnTile((vector.x - border) / scale, (vector.y - border) / scale, (double) unitRadius / scale);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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
    }
}
