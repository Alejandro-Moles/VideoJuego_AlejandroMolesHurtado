package com.videojuego.actors;

import static com.videojuego.extras.Utils.PIEDRA;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Piedra extends Actor {

    //me creo variables que indican el tamaño de la piedra y con que velocidad se mueven
    private static final float PIEDRA_WIDTH = 0.4f;
    private static final float PIEDRA_HEIGHT = 0.4f;
    private static final float SPEED = -2.5f;


    //me creo las diferentes variables para crearme el actor
    private TextureRegion piedraTR;
    private Body piedra_body;
    private Fixture piedra_fiture;
    private World world;


    //en el constructor les asigno a las variables los diferentes datos que obtengo al crear el actor
    public Piedra(World world, TextureRegion trp, Vector2 position){
        this.world = world;
        this.piedraTR =  trp;
        //me creo el cuerpo de la piedra
        createBodyPiedra(position);
        //me creo la "hitbox" de la piedra
        createFixture();
    }

    private void createBodyPiedra(Vector2 position){
        //le asigno una posicion y el tipo de cuerpo que va a ser
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody;
        piedra_body = world.createBody(def);
        piedra_body.setUserData(PIEDRA);
        //tambien le asigno la velocidad a la que se va a mover
        piedra_body.setLinearVelocity(SPEED,0);
    }


    private void createFixture() {
        //le creo una "hitbox" del forma rectangular
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PIEDRA_WIDTH / 2, PIEDRA_HEIGHT / 2);

        this.piedra_fiture = piedra_body.createFixture(shape,8);
        this.piedra_fiture.setUserData(PIEDRA);
        shape.dispose();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(piedra_body.getPosition().x -PIEDRA_WIDTH / 2, piedra_body.getPosition().y- PIEDRA_HEIGHT / 2);
        batch.draw(this.piedraTR, getX(), getY(), PIEDRA_WIDTH,PIEDRA_HEIGHT);
    }

    public void detach(){
        //(body) destroyFixture
        this.piedra_body.destroyFixture(this.piedra_fiture);
        //(world) destroyBody
        this.world.destroyBody(this.piedra_body);
    }

    public boolean fueraPantalla(){return this.piedra_body.getPosition().x <= -2;}

    public void stopPîedra(){
        this.piedra_body.setLinearVelocity(0,0);
    }
}
