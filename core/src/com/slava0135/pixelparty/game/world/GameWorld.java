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

import static com.slava0135.pixelparty.game.Game.MAX_UNIT_AMOUNT;
import static com.slava0135.pixelparty.game.world.WorldGenerator.generateWorld;

public class GameWorld implements Disposable {
    private World world;
    private Floor floor;
    private Fall fall;

    public final static float UNIT_SCALE = 0.3f;
    public final static float UNIT_RADIUS = UNIT_SCALE;

    private final static float IMPULSE = 0.1f;

    private double maxVelocity = 2;
    private boolean playerIsAlive = true;

    private Random random = new Random();

    private final FixtureDef fixture;
    private final Array<Body> bodies = new Array<>();
    private Body player;

    public GameWorld(Floor floor, Fall fall) {
        world = generateWorld();
        fixture = getCircleFixture();
        this.floor = floor;
        this.fall = fall;
        spawnPlayer();
        spawnUnits(MAX_UNIT_AMOUNT);
    }

    public boolean update(GameStage stage, Vector2 click) {
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
                    world.destroyBody(player);
                    playerIsAlive = true;
                    return false;
                }
                break;
            }
        }
        if (playerIsAlive) {
            moveBody(click, player);
        }
        return true;
    }

    public Array<Vector2> getBodiesPositions() {
        Array<Vector2> array = new Array<>();
        for (Body body: bodies) {
            array.add(new Vector2(body.getPosition()));
        }
        return array;
    }

    public Vector2 getPlayerPosition() {
        return new Vector2(player.getPosition());
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
