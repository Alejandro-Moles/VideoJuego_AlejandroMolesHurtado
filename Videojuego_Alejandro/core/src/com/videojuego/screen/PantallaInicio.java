package com.videojuego.screen;

import static com.videojuego.extras.Utils.SCREEN_HEIGHT;
import static com.videojuego.extras.Utils.SCREEN_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.videojuego.MainGame;

public class PantallaInicio extends PantallaBase{

    //creamos las variables diferentes que utilizare en la pantalla de inicio
    //son el stage, que es el escenario, el skin que sirve para darle un diseño a los botones, una imagen de fondo otra normal y un boton y una fuente para poner un texto
    private Stage stage;
    private Skin skin;
    private TextButton textButton;
    private Image background;
    private OrthographicCamera camaraFont;
    private BitmapFont texto;

    //creamos el contructor
    public PantallaInicio(final MainGame mainGame) {
        super(mainGame);

        //le asignamos un tamaño al fitviewport que luego se lo asignaremos al stage(al crearlo)
        FitViewport fitViewport = new FitViewport(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.stage = new Stage(fitViewport);
        //inicializamos la variable skin con un fichero json que he insertado en la carpeta assets
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.textButton = new TextButton("INICIAR", skin);

        this.textButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainGame.setScreen(mainGame.gameScreen = new PantallaJuego(mainGame));
            }
        });

        this.textButton.setSize(200, 100);
        this.textButton.setPosition(130, 300);

        addBackground();
        this.stage.addActor(textButton);

        //llamo a un metodo que me prepara la camara y el texto para escribirlo en la pantalla
        prepararTexto();
    }

    public void addBackground(){
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0,0);
        this.background.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
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

        //"meto" la camara para que el texto se muestre encima de las demas cosas y asi no haya problemas
        this.stage.getBatch().setProjectionMatrix(this.camaraFont.combined);
        this.stage.getBatch().begin();
        //le asigno un texto y una posicion a mi texto
        this.texto.draw(this.stage.getBatch(), "Get Ready!", SCREEN_WIDTH / 30, 600);
        this.stage.getBatch().end();
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

    private void prepararTexto(){
        //le asigno la fuente
        this.texto = this.mainGame.assetManager.getFuente();
        //lñe doy uin tamaño
        this.texto.getData().scale(0.6f);

        //instancio la camara
        this.camaraFont = new OrthographicCamera();
        this.camaraFont.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.camaraFont.update();
    }
}
