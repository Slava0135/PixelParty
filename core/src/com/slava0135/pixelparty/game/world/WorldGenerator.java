package com.slava0135.pixelparty.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.slava0135.pixelparty.game.floor.Floor;

class WorldGenerator {
    public static World generateWorld() {
        World world = new World(new Vector2(0, 0),false);
        generateBorders(world);
        return world;
    }

    private static void generateBorders(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        PolygonShape polygon = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 1;
        polygon.setAsBox(Floor.SIZE / 2f, Floor.SIZE / 2f);

        bodyDef.position.set(Floor.SIZE / 2f, Floor.SIZE * 3f / 2);
        Body up = world.createBody(bodyDef);
        up.createFixture(fixtureDef);

        bodyDef.position.set(Floor.SIZE * 3f / 2, Floor.SIZE / 2f);
        Body right = world.createBody(bodyDef);
        right.createFixture(fixtureDef);

        bodyDef.position.set(-Floor.SIZE / 2f, Floor.SIZE / 2f);
        Body left = world.createBody(bodyDef);
        left.createFixture(fixtureDef);

        bodyDef.position.set(Floor.SIZE / 2f, -Floor.SIZE / 2f);
        Body down = world.createBody(bodyDef);
        down.createFixture(fixtureDef);

        polygon.dispose();
    }
}
