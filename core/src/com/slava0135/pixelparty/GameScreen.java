package com.slava0135.pixelparty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.slava0135.pixelparty.world.Floor;
import com.slava0135.pixelparty.world.Palette;

import java.util.Random;

public class GameScreen implements Screen {
    final static Color background = Color.WHITE;
    final static int unitRadius = 15; //pixels
    final static int scale = 50; //1 tile length
    //rendering
    final PixelGame game;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    //data
    World world;
    Random random = new Random();
    Floor floor;
    Array<Body> bodies;
    //timing
    float time;
    //staging
    Stage stage = Stage.WAIT;
    enum Stage {
        WAIT, RUN, BREAK;
        public static float length = 3;
        private static Stage[] vals = values();
        public Stage next() {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }

    public GameScreen(final PixelGame game, int unitAmount) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 1000);
        world = new World(new Vector2(0, 0),false);
        shapeRenderer = new ShapeRenderer();
        floor = new Floor();
        time = 0;
        spawnUnits(unitAmount);
        floor.generateFloor();
    }

    @Override
    public void render(float delta) {
        time += delta;
        //graphics
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        floor.draw(100, 100, scale);
        for (Body body: bodies) {
            Vector2 vector = body.getPosition();
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(vector.x, vector.y, unitRadius);
            shapeRenderer.end();
        }
        //logic
        switch(stage) {
            case WAIT: {
                if (time > Stage.length) {
                    time = 0;
                    stage = stage.next();
                }
            }
            case RUN: {
                if (time > Stage.length) {
                    time = 0;
                    stage = stage.next();
                    floor.throwFloor();
                }
            }
            case BREAK: {
                if (time > Stage.length) {
                    time = 0;
                    stage = stage.next();
                    floor.generateFloor();
                }
            }
        }
        //movement
        world.step(1/60f, 6, 2);
    }

    private void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        CircleShape circle = new CircleShape();
        circle.setRadius(unitRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        bodies = new Array<>();
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(100 + random.nextInt(800), 100 + random.nextInt(800));
            Body body = world.createBody(bodyDef);
            Fixture fixture = body.createFixture(fixtureDef);
            bodies.add(body);
        }
        circle.dispose();
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
    }
}
