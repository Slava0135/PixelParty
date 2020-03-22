package com.slava0135.pixelparty.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.slava0135.pixelparty.game.GameStage;
import com.slava0135.pixelparty.game.floor.Fall;
import com.slava0135.pixelparty.game.floor.Floor;

import java.util.Iterator;
import java.util.Random;

import static com.slava0135.pixelparty.game.world.WorldGenerator.generateWorld;

public class GameWorld implements Disposable {
    private World world;
    private Floor floor;
    private Fall fall;

    private final static float IMPULSE = 0.1f;
    private final static float UNIT_SCALE = 0.3f;
    private final static float UNIT_RADIUS = UNIT_SCALE;
    private final static int MAX_UNIT_AMOUNT = 50;

    private double maxVelocity = 2;

    Random random = new Random();

    FixtureDef fixture;
    Array<Body> bodies = new Array<>();
    Body player;

    GameWorld(Floor floor, Fall fall) {
        world = generateWorld();
        fixture = getCircleFixture();
        this.floor = floor;
        this.fall = fall;
        spawnPlayer();
        spawnUnits(MAX_UNIT_AMOUNT);
    }

    public boolean update(GameStage stage) {
        world.step(1/60f, 6, 2);
        switch (stage) {
            case RUN: {
                saveMove();
                break;
            }
            case WAIT: {
                randomMove();
                break;
            }
            case BREAK: {
                saveMove();
                eliminate();
                if (isDead(player)) {
                    return false;
                }
                break;
            }
        }
        return true;
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

    public void spawnUnits(int amount) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < amount; i++) {
            bodyDef.position.set(random.nextFloat() * Floor.SIZE, random.nextFloat() * Floor.SIZE);
            Body body = world.createBody(bodyDef);
            body.createFixture(fixture);
            bodies.add(body);
        }
    }

    public void speedUp(float speedMultiplier) {
        maxVelocity *= speedMultiplier;
    }

    private void saveMove() {
        for (Body body: bodies) {
            Vector2 pos = body.getPosition();
            moveBody(floor.findClosest(pos.x, pos.y), body);
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
                    } break;
                }
                case 1: {
                    if (velX > -maxVelocity) {
                        body.applyLinearImpulse(-IMPULSE, 0, pos.x, pos.y, true);
                    } break;
                }
                case 2: {
                    if (velY < maxVelocity) {
                        body.applyLinearImpulse(0, IMPULSE, pos.x, pos.y, true);
                    } break;
                }
                default: {
                    if (velY > -maxVelocity) {
                        body.applyLinearImpulse(0, -IMPULSE, pos.x, pos.y, true);
                    } break;
                }
            }
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

    private void eliminate() {
        for (Iterator<Body> iter = bodies.iterator(); iter.hasNext(); ) {
            Body body = iter.next();
            if (isDead(body)) {
                fall.addUnit(Color.BLACK, Color.BLACK, new Vector2(body.getPosition()));
                world.destroyBody(body);
                bodies.removeValue(body, true);
            }
        }
    }

    private boolean isDead(Body body) {
        Vector2 vector = body.getPosition();
        return !floor.isOnTile(vector.x, vector.y, UNIT_RADIUS);
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
