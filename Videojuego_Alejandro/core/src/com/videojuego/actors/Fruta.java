package com.videojuego.actors;


import static com.videojuego.extras.Utils.FRUTA2;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Fruta extends Actor {

    //me creo variables que indican el tamaño de la fruta y con que velocidad se mueven
    private static final float FRUTA_WITDH = 0.6f;
    private static final float FRUTA_HEIGHT = 0.6f;
    private static final float SPEED = -2.5f;

    //verdadero significara que esta en la pantalla, y falso que la habran cogido y por lo tanto desaparecera
    public static boolean FRUTA_EN_PANTALLA;

    //me creo las diferentes variables para crearme el actor
    private TextureRegion FrutaTR;
    public Body fruta_body;
    public Fixture fruta_fixture;
    private World world;
    private Sound sonido_fruta;

    //en el constructor les asigno a las variables los diferentes datos que obtengo al crear el actor
    public Fruta(World world, TextureRegion trp, Vector2 position, Sound sonido){
        this.world = world;
        this.FrutaTR =  trp;
        FRUTA_EN_PANTALLA = true;
        this.sonido_fruta = sonido;

        //me creo el cuerpo de la fruta
        createBodyFruta(position);
        //me creo la "hitbox" de la fruta
        createFixture();
    }

    private void createBodyFruta(Vector2 position){
        //le asigno una posicion y el tipo de cuerpo que va a ser
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody;
        this.fruta_body = this.world.createBody(def);
        //tambien le asigno la velocidad a la que se va a mover
        fruta_body.setLinearVelocity(SPEED,0);

    }

    public void createFixture(){
        //le creo una "hitbox" del forma rectangular
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(FRUTA_WITDH / 4, FRUTA_HEIGHT / 4);
        //createFixture
        this.fruta_fixture = this.fruta_body.createFixture(polygonShape,0);
        this.fruta_fixture.setSensor(true);
        //Le paso la propia "Fruta" ya que lo necesito a la hora de que haya una colisión hacer desaparecer la fruta
        this.fruta_fixture.setUserData(this);
        //dispose
        polygonShape.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //para hacer que las hitbox y la imagen del arbol mas o menos cuadren, a la hora de dibujar el arbol, al posicionarlo,
        //en el eje x sera cogiendo la posicion donde esta en la x menos el tamaño que le he dado al poligono de la hitbox de ancho
        //lo mismo pasaria con el la posicion en y pero en vez del ancho, la altura
        setPosition(fruta_body.getPosition().x - FRUTA_WITDH / 2, fruta_body.getPosition().y - FRUTA_HEIGHT / 2);
        batch.draw(this.FrutaTR, getX(), getY(), FRUTA_WITDH, FRUTA_HEIGHT);
    }

    public void detach(){
        this.fruta_body.destroyFixture(this.fruta_fixture);
        this.world.destroyBody(this.fruta_body);
    }

    //metodo que hace que la fruta pare
    public void stopFruta(){
        this.fruta_body.setLinearVelocity(0,0);
    }

    //metodo que comprueba si la fruta esta fuera de la pantalla
    public boolean fueraPantalla(){return this.fruta_body.getPosition().x <= -2;}

    //metodo que devuelve el sonido de la fruta al ser recogida
    public Sound getSonido_fruta(){
        return this.sonido_fruta;
    }
}
