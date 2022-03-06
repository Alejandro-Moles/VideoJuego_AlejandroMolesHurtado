package com.videojuego.screen;


import static com.videojuego.actors.Woodcutter.STATE_NORMAL;
import static com.videojuego.extras.Utils.PIEDRA;
import static com.videojuego.extras.Utils.SCREEN_HEIGHT;
import static com.videojuego.extras.Utils.SCREEN_WIDTH;
import static com.videojuego.extras.Utils.USER_FLOOR;
import static com.videojuego.extras.Utils.USER_PERSONAJE;
import static com.videojuego.extras.Utils.WORLD_HEIGTH;
import static com.videojuego.extras.Utils.WORLD_WIDTH;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.videojuego.MainGame;
import com.videojuego.actors.Fruta;
import com.videojuego.actors.Piedra;
import com.videojuego.actors.Woodcutter;


public class PantallaJuego extends PantallaBase implements ContactListener{

    //me creo dos variables que indicaran el tiempo en el que apareceran las frutas y las piedras
    private float timetoCreateObject;
    private float timetoCreateFruta;

    //me creo el stage, una imagen para el fondo, el actor principal(WoodCutter), un mundo
    private Stage stage;
    private Image background;
    private Woodcutter woodcutter;
    private World world;

    //me creo un Dos arrays diferentes que guardaran una lista de objetos de los actores de piedras y frutas
    private Array<Piedra> arrayPiedra;
    private Array<Fruta> arrayfruta;

    //private Box2DDebugRenderer debugRenderer; -->COMENTO ESTO POE QUE NO LO VOY A NECESITAR EN EL JUEGO TERMINADO
    //me creo una camara para la parte principal y otra para el contador que iria como en un segundo plano para que no se mezclen entre si
    private OrthographicCamera ortCamera;
    private OrthographicCamera camaraFont;
    private BitmapFont puntuacion;
    //me creo un objeto Music que es el que gestionara la musica del juego
    private Music musicbg;

    //me crepo una variable que es la que se encargara de contar los puntos
    private int contador_puntos = 0;


    public PantallaJuego(MainGame mainGame) {
        super(mainGame);

        //instancio el mundo y le asigno la "gravedad" qu eeste tendra
        this.world =new World(new Vector2(0,0 - 10),true);
        //hago que se puedan manejar las colisiones
        this.world.setContactListener(this);

        //le asignamos un tamaño al fitviewport que luego se lo asignaremos al stage(al crearlo)
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGTH);
        this.stage = new Stage(fitViewport);
        //inicializo los arrays
        this.arrayPiedra = new Array();
        this.arrayfruta = new Array<>();

        //creo dos contadores del tiempo, ya que si no lo hiciese no se podrian spamear la fruta y la piedra a la vez, ya
        //que las dos cogerian el mismo tiempo de referencia, por eso creo un contador para crear las piedras y otro para las frutas
        this.timetoCreateObject =0f;
        this.timetoCreateFruta = 0f;

        //le asigno al objeto de la musica la cancion
        this.musicbg = this.mainGame.assetManager.getMusicBG();

        //le añado al stage la camara
        this.ortCamera = (OrthographicCamera) this.stage.getCamera();
        //this.debugRenderer = new Box2DDebugRenderer();
        //llamo al metodo que me preparara la camara y la fuente para el contador de puntuacion
        prepararPuntuacion();
    }

    public void addBackground(){
        //le asigno una imagen al fondo , una posicion y un tamaño
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0,0);
        this.background.setSize(WORLD_WIDTH,WORLD_HEIGTH);
        //añado el fondo al stage
        this.stage.addActor(this.background);
    }

    //este metodo se ejecuta al mostrarse la pantalla por primera vez
    @Override
    public void show() {
        //Añadimos el metodo que crea el fondo, el que añade el suelo y el que añade al actor principal
        addBackground();
        addFloor();
        addLeniador();

        //hacemos que la musica se ejecute contantemente(si termina vuelve empezar) y hacemos que comience
        this.musicbg.setLooping(true);
        this.musicbg.play();
    }

    //este es el metodo que añade al stage el actor principal
    private void addLeniador(){
        //le pasamos el sonido del salto para quie al crear el actor tenga ese sonido
        Sound sonido =this.mainGame.assetManager.getSonidoSalto();
        //le pasamos tambien la animacion que tendra el personaje
        Animation<TextureRegion> spriteWood = mainGame.assetManager.getWoodcutterRuningAnimation();
        //instancio  al personaje pasandole todos los parametros
        this.woodcutter =new Woodcutter(this.world,spriteWood, new Vector2(1.5f, 1.2f), sonido);
        //indico que el personaje esta "VIVO" y lo añado al stage
        this.woodcutter.state = STATE_NORMAL;
        this.stage.addActor(this.woodcutter);
    }

    //este metodo es el que añade tanto el objeto de piedra como el de fruta
    private void addObjeto(float delta){
        //me creo un Texture Regio tanto uno como para otro
        TextureRegion piedraTr;
        TextureRegion frutaTr;

        //genero un numero aleatorio para la pidera y fruta, que seran el tiempo en el que se generaran estos actores
        //tambien genero otro numero aleatorio para cada uno que indicara que sprite se mostrara en pantalla
        float tiempo_spawm_piedra = MathUtils.random(1f, 2.7f);
        float tiempo_spawm_fruta = MathUtils.random(2f,7f);
        int opcion_piedra = MathUtils.random(1,3);
        int opcion_fruta = MathUtils.random(1,6);

        //si el leñador esta vivo, enontces me genera tanto las fruas como las piedras
        if(woodcutter.state == STATE_NORMAL ){
            //le voy sumando al tiempo en el que se generan lso objetos delta, para que cuando llegue al tiempo
            //deseado se genere una piedea o fruta
            this.timetoCreateObject +=delta;
            this.timetoCreateFruta += delta;

            //si el tiempo que se ha ido sumando, es igual o mayor al tiempo aleatorio que se ha generado
            //entonces se genera un fruta
            if (this.timetoCreateFruta >= tiempo_spawm_fruta){
                this.timetoCreateFruta -=tiempo_spawm_fruta;
                //hago un switch para ver que sprite se le coloca a la fruta
                switch (opcion_fruta){
                    case 1:
                        //dependiendo del numero que haya salido aleatorio, elegire un sprite u otro
                        frutaTr = mainGame.assetManager.getFruta();
                        addFruta(frutaTr);
                        break;
                    case 2:
                        frutaTr = mainGame.assetManager.getFruta2();
                        addFruta(frutaTr);
                        break;
                    case 3:
                        frutaTr = mainGame.assetManager.getFruta3();
                        addFruta(frutaTr);
                        break;
                    case 4:
                        frutaTr = mainGame.assetManager.getFruta4();
                        addFruta(frutaTr);
                        break;
                    case 5:
                        frutaTr = mainGame.assetManager.getFruta5();
                        addFruta(frutaTr);
                        break;
                    case 6:
                        frutaTr = mainGame.assetManager.getFruta6();
                        addFruta(frutaTr);
                        break;
                }
            }
            //lo mismo que con la fruta realizo con la piedra
            if(this.timetoCreateObject >= tiempo_spawm_piedra){
                this.timetoCreateObject -=tiempo_spawm_piedra;
                switch (opcion_piedra){
                    case 1 :
                        piedraTr = mainGame.assetManager.getPiedra();
                        addPiedra(piedraTr);
                        break;
                    case 2:
                        piedraTr = mainGame.assetManager.getPiedra2();
                        addPiedra(piedraTr);
                        break;
                    case 3:
                        piedraTr = mainGame.assetManager.getPiedra3();
                        addPiedra(piedraTr);
                        break;
                }
            }
        }
    }

    //metodo que llamo para agregar una actor piedra
    private void addPiedra(TextureRegion tr){
        //me creo un nuevo objeto piedra y le paso por parametro lo valores necesarios para su creacion
        Piedra piedra = new Piedra(this.world,tr,new Vector2(8f,1.2f));
        //añado la piedra a su array y tambien al stage
        arrayPiedra.add(piedra);
        this.stage.addActor(piedra);
    }

    //metodo que llamo para agragar un actor fruta
    private void addFruta(TextureRegion tr){
        //creo un sonido y una altura(aleatoriamente) para pasarlos al objeto fruta que me he creado
        Sound sonido =this.mainGame.assetManager.getSonidoFruta();
        float altura = MathUtils.random(1.5f, 2.3f);
        Fruta fruta = new Fruta(this.world,tr, new Vector2(9f,altura), sonido);
        //añado la fruta a su array y al stage
        arrayfruta.add(fruta);
        this.stage.addActor(fruta);
    }

    //metodo que llamo cuando quiero eliminar las piedras
    private void QuitarPiedra(){
        //recorro el array
        for (Piedra piedra : this.arrayPiedra){
            //por cada piedra si
            if(!world.isLocked()){
                //por cada piedra miro si esta fuera de la pantalla, y si lo esta entonces la elimino
                if(piedra.fueraPantalla()){
                    //llamp al metodo deatch y luego le hago un remove
                    piedra.detach();
                    piedra.remove();
                    //la quito del array
                    arrayPiedra.removeValue(piedra, false);
                }
            }
        }
    }


    //con este metodo estoy quitando todas las frutas que hayan sido tocadas por mi personaje principal, o que se hayan salido de la pantalla
    private void RecogerFruta(){
        //recorro el array de frutas
        for (Fruta fruta : this.arrayfruta){
            if(!world.isLocked()){
                //compruebo si ha sido tocada o no
                if(!fruta.FRUTA_EN_PANTALLA){
                    //pongo la variable que indica si ha sido tocada a que no a sido tocada
                    fruta.FRUTA_EN_PANTALLA = true;
                    fruta.detach();
                    fruta.remove();
                    arrayfruta.removeValue(fruta, false);
                    this.contador_puntos = this.contador_puntos + 1;
                }
            }
        }
    }



    //este metodo salta cuando las frutas salen de la pantalla y no se han podido recoger, para eliminarlas de la memoria
    private void QuitarFruta(){
        for (Fruta fruta : this.arrayfruta){
            if(!world.isLocked()){
                //compruebo si esta fuera de la pantalla
                if(fruta.fueraPantalla()){
                    fruta.detach();
                    fruta.remove();
                    arrayfruta.removeValue(fruta, false);
                }
            }
        }
    }

    //metodo que prepara la camara y fuente del contador
    private void prepararPuntuacion(){
        //le asigno una fuente y un tamaño
        this.puntuacion = this.mainGame.assetManager.getFuente();
        this.puntuacion.getData().scale(1f);

        //me creo la camara y le asigo un tamaño
        this.camaraFont = new OrthographicCamera();
        this.camaraFont.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.camaraFont.update();
    }

    //este metodo se ejecutara cada como un bucle y se ira actualizando cada vez que pase delta
    @Override
    public void render(float delta) {
        //lamo al metodo para que me vaya agregando los objetos
        addObjeto(delta);

        //hago que el escenario se dibuje
        this.stage.getBatch().setProjectionMatrix(ortCamera.combined);
        this.stage.act();
        this.world.step(delta,6,2);
        this.stage.draw();

        //Actualizamos la cámara para que aplique cualquier cambio en las matrices internas.
        this.ortCamera.update();


        //this.debugRenderer.render(this.world, this.ortCamera.combined);

        //llamo los diferentes metodos para que si esta fuera de la pantalla las piedras y las frutas, o si
        //han sido recojidas las frutas se eliminen
        QuitarPiedra();
        RecogerFruta();
        QuitarFruta();

        //hago que se agregue el contador y le hago que cada vez que se muestren los puntos que se han obtenmido en tontal
        this.stage.getBatch().setProjectionMatrix(this.camaraFont.combined);
        this.stage.getBatch().begin();
        this.puntuacion.draw(this.stage.getBatch(), ""+this.contador_puntos, SCREEN_WIDTH/2.2F, 700);
        this.stage.getBatch().end();
    }


    //este metodo se ejecutara cuando se "esconda" la pantalla
    @Override
    public void hide() {
        //detach
        this.woodcutter.detach();
        //remove
        this.woodcutter.remove();


        this.musicbg.stop();
    }

    //este metodo libera de la memoria los elemntos que no se vayan a actualizar
    @Override
    public void dispose() {
        this.stage.dispose();
        this.world.dispose();
    }

    //este metodo me crea el suelo donde se mantendra en pie mi personaje
    private void addFloor(){
        //le creo un body y le asigno un tamaño
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(WORLD_WIDTH / 2 , 0.5f);
        //le digo que sea estatico para que no se mueva
        bodyDef.type = BodyDef.BodyType.StaticBody;
        //se lo paso al mundo
        Body body = world.createBody(bodyDef);

        //me creo un poligono que sera su "hitbox"
        PolygonShape edge = new PolygonShape();
        edge.setAsBox(WORLD_WIDTH, 0.5f);

        //Creo una fixture y para asignarle el user data, ya que si no a la hora de ver si hay alguna colisión, al hacer
        //el contact.getFixture, si le doy el user data al body , daria null, ya que no le estoy asignando al fixture.
        Fixture fixture= body.createFixture(edge, 3);

        fixture.setUserData(USER_FLOOR);
        edge.dispose();
    }

    //metodo que comprueba si han colisionado los objetos que le pase por parameros, se tiene que "ejecutar" dos
    //veces para estar seguro de la colision ya que no se puede saber con certeza que ha colisionado primero de los
    //dos objetos que le pasas
    public boolean areColider(Contact contact, Object objA, Object objB){
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }


    //este metodo salta cuando dos objetos han sufrido una colision entre si
    @Override
    public void beginContact(Contact contact) {
            //comprubeo que los objetos que han colisionado sea el suelo y mi personaje
            if(!areColider(contact,USER_FLOOR,USER_PERSONAJE)){
                //AL ser el suelo y mi personaje no pasaria nada al colisionar entre si, entonces llamo de nuevo
                //al metod para comprobar si ha sido contra la piedra
                if(areColider(contact,USER_PERSONAJE, PIEDRA)){
                    //al ser contra la piedra, mi personaje moriria, por lo que llamo a un metodo que lo "mata"
                    this.woodcutter.Hurt();
                    //llamo a un metodo que le cambia la animacio a muerte
                    this.woodcutter.changeAnimation();
                    //hago que se pare el sonido del salto por si ha muerto al saltar, y que no se quede el sonido reproduciendose
                    this.woodcutter.getSonido_salto().stop();
                    //tambien paro la musica
                    this.musicbg.stop();

                    //recorro el array de piedras y por cada una la paro para que no se sigan moviuendo
                    for (Piedra piedra:this.arrayPiedra){
                        piedra.stopPîedra();
                        piedra.remove();
                    }

                    //hago lo mismo para el array de frutas
                    for (Fruta fruta:this.arrayfruta){
                        fruta.getSonido_fruta().stop();
                        fruta.stopFruta();
                    }

                    //llamo a la pantalla de game over para que se cambie la pantalla
                    this.stage.addAction(Actions.sequence(
                            Actions.delay(0.8f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    mainGame.setScreen(mainGame.gameOverScreen);
                                }
                            })
                    ));
                }else{
                    //si no es ninguna de las demas condiciones quiere decir que entonces ha chocado contra una fruta
                    //Casteo el objeto contra el que ha chocado a Fruta
                    Fruta fruta = (Fruta) contact.getFixtureB().getUserData();
                    //Hago que suene un sonido al obtener la fruta
                    fruta.getSonido_fruta().play();
                    //cambio la variable que me indica si la fruta esta en pantalla a false, lo que me indicara que la fruta ha sido cogida
                    fruta.FRUTA_EN_PANTALLA = false;
                }
            }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
