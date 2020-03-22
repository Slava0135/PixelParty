package com.slava0135.pixelparty.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

import static com.slava0135.pixelparty.world.WorldGenerator.generateWorld;

public class GameWorld implements Disposable {
    World world;

    private final static float IMPULSE = 0.1f;
    public final static float UNIT_SCALE = 0.3f;
    public final static float UNIT_RADIUS = UNIT_SCALE;
    private final static int MAX_UNIT_AMOUNT = 50;

    private double speedMultiplier = 1.05;
    private double maxVelocity = 2;

    Random random = new Random();

    FixtureDef fixture;
    Array<Body> bodies = new Array<>();
    Body player;

    GameWorld() {
        world = generateWorld();
        fixture = getCircleFixture();
        spawnPlayer();
        spawnUnits(MAX_UNIT_AMOUNT);
    }

    private FixtureDef getCircleFixture() {
        CircleShape circle = new CircleShape();
        circle.setRadius(UNIT_RADIUS);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.7f;
        circle.dispose();
        return fixtureDef;
    }

    private void spawnPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(random.nextFloat() * Floor.SIZE, random.nextFloat() * Floor.SIZE);
        player = world.createBody(bodyDef);
        player.createFixture(fixture);
    }

    private void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(random.nextFloat() * Floor.SIZE, random.nextFloat() * Floor.SIZE);
            Body body = world.createBody(bodyDef);
            body.createFixture(fixture);
            bodies.add(body);
        }
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
