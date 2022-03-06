package com.videojuego.screen;



import static com.videojuego.extras.Utils.SCREEN_HEIGHT;
import static com.videojuego.extras.Utils.SCREEN_WIDTH;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.videojuego.MainGame;

public class PantallaGameOver extends PantallaBase {

    //creamos las variables diferentes que utilizare en la pantalla de game over
    //son el stage, que es el escenario, el skin que sirve para darle un diseño a los botones, una imagen de fondo otra normal y un boton
    private Stage stage;
    private Skin skin;
    private Image image;
    private TextButton reiniciar;
    private Image background;

    //creamos el constructor
    public PantallaGameOver(final MainGame mainGame) {
        super(mainGame);

        //le asignamos un tamaño al fitviewport que luego se lo asignaremos al stage(al crearlo)
        FitViewport fitViewport = new FitViewport(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.stage = new Stage(fitViewport);
        //inicializamos la variable skin con un fichero json que he insertado en la carpeta assets
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        //inicializanmos la imagen y el boton reiniciar
        this.image = new Image(mainGame.assetManager.getGameOver());
        this.reiniciar = new TextButton("REINTENTAR", skin);

        //hacemos que el boton tenga un Listener que se activara al pulsarlo
        this.reiniciar.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //hacemos que se fije la pantalla a la pantalla del juego, inicializandola para que se inicie el juego desde cero
                mainGame.setScreen(mainGame.gameScreen = new PantallaJuego(mainGame));
            }
        });

        //ponemos el boton y la imagen en la posicion que querramos
        this.image.setPosition(SCREEN_WIDTH / 2 - this.image.getWidth() / 2, SCREEN_HEIGHT - 150 );
        this.reiniciar.setSize(200, 100);
        this.reiniciar.setPosition(130, 300);

        //llamo a un metodo para que me agregue el fondo
        addBackground();
        //añadimos los elementos al stage
        this.stage.addActor(image);
        this.stage.addActor(reiniciar);
    }


    public void addBackground(){
        //inicializo la imagen del fondo de mi aplicacion
        this.background = new Image(mainGame.assetManager.getBackground());
        //indico donde va a estar y el tamaño
        this.background.setPosition(0,0);
        this.background.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        //añado el fondo al stage
        this.stage.addActor(this.background);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    //para poder hacer acciones con los botones de el "menu" de gameover se tiene que seleccioner un inputProcesor en el show, por que si no podría haber problemas
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    //cuando se oculta la pantalla de game over se tiene que dejar de usar el input procesor del stage por que si no empezaria a dar errores
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
