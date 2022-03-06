package com.videojuego.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.videojuego.extras.AssetMan;
import com.videojuego.extras.Utils;

public class Woodcutter extends Actor {

    //creo diferentes variables que me indican si esta muerto o vivo y la velocidfad con la que salta el personaje
    public static final int STATE_NORMAL = 0;
    public static final int STATE_DEAD = 1;
    private static final float JUMP_SPEED = 4.3f;


    //me creo el objeto  animacion que tendra
    private Animation<TextureRegion> animation;
    //un objeto vector que le asignara la posicion
    private Vector2 position;
    //un objeto mundo
    private World world;
    private float stateTime;
    //el estado en el que se encuentra
    public int state;
    //el cuerpo y la fixture
    private Body body;
    private Fixture fixture;

    //me creo el sonido que tendra al saltar
    private Sound sonido_salto;

    //me creo un objeto de asset manager para controlar las animaciones de cuando salta
    private AssetMan assetMan;


    //en el constructor les asigno a las variables los diferentes datos que obtengo al crear el actor
    public Woodcutter(World world, Animation<TextureRegion> animation, Vector2 position, Sound sonido) {
        this.animation = animation;
        this.position  = position;
        this.world = world;
        this.sonido_salto = sonido;
        assetMan = new AssetMan();
        stateTime = 0f;
        state = STATE_NORMAL;
        createBody();
        createFixture();
    }


    public void createBody(){
        //Creamos BodyDef
        BodyDef bodyDef = new BodyDef();
        //Position
        bodyDef.position.set(position);

        //tipo
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //createBody de mundo
        this.body = this.world.createBody(bodyDef);
    }


    public void createFixture(){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.25f,0.25f);
        //createFixture
        this.fixture = this.body.createFixture(polygonShape,3);
        this.fixture.setUserData(Utils.USER_PERSONAJE);
        //dispose
        polygonShape.dispose();
    }

    //metodo que cambia el estado de mi personaje a muerto
    public void Hurt(){
        this.state = STATE_DEAD;
        this.stateTime = 0;

    }

    public void changeAnimation(){
        animation = assetMan.MuerteAnimacion();
    }


    @Override
    public void act(float delta) {
        //me combiene que sea esta tocando, asi el jugador puede mantener la pantalla y el personaje saltara nada mas toque el suelo
        boolean jump = Gdx.input.isTouched();
        //aqui añado una condicion para que si es menor o igual a la altura que mas o menos esta el suelo, pueda saltar
        //ya que si no podria saltar mas de una vez, cosa que no se puede en mi videojuego. Pongo el menor igual ya que no se seguro
        //donde va a caer el personaje exactamente cuando termine el salto
        if(jump && this.state == STATE_NORMAL && this.body.getPosition().y <= 1.27f){
            this.body.setLinearVelocity(0, JUMP_SPEED);
            animation = assetMan.getWoodcutterJumpAnimation();
            this.sonido_salto.play();
        }

        //cuando el personaje este de nuevo en el suelo se cambiara de nuevo a la animacion de correr
        if(!jump && this.body.getPosition().y <= 1.27f && this.state == STATE_NORMAL){
            animation = assetMan.getWoodcutterRuningAnimation();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x-0.25f, body.getPosition().y - 0.25f);
        batch.draw(this.animation.getKeyFrame(stateTime,true),getX(),getY(), 0.6f,0.5f);
        stateTime += Gdx.graphics.getDeltaTime();
    }


    public void detach(){
        //(body) destroyFixture
        this.body.destroyFixture(this.fixture);
        //(world) destroyBody
        this.world.destroyBody(this.body);
    }

    public Sound getSonido_salto(){
        return this.sonido_salto;
    }

}
