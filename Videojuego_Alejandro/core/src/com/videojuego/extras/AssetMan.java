package com.videojuego.extras;

import static com.videojuego.extras.Utils.ATLAS_MAP;
import static com.videojuego.extras.Utils.BACKGROUND_IMAGE;

import static com.videojuego.extras.Utils.COGER_FRUTA;
import static com.videojuego.extras.Utils.FRUTA10;
import static com.videojuego.extras.Utils.FRUTA2;

import static com.videojuego.extras.Utils.FRUTA6;
import static com.videojuego.extras.Utils.FRUTA7;
import static com.videojuego.extras.Utils.FRUTA8;
import static com.videojuego.extras.Utils.FRUTA9;
import static com.videojuego.extras.Utils.FUENTE_FNT;
import static com.videojuego.extras.Utils.FUENTE_PNG;
import static com.videojuego.extras.Utils.IMAGEN_GAMEOVER;
import static com.videojuego.extras.Utils.MUSIC_BG;
import static com.videojuego.extras.Utils.PIEDRA;
import static com.videojuego.extras.Utils.PIEDRA2;
import static com.videojuego.extras.Utils.PIEDRA3;
import static com.videojuego.extras.Utils.SONIDO_SALTO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class AssetMan {

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public AssetMan() {
        this.assetManager = new AssetManager();

        //cargo el atlas map, y los diferentes sonidos y musica
        assetManager.load(ATLAS_MAP, TextureAtlas.class);
        assetManager.load(SONIDO_SALTO, Sound.class);
        assetManager.load(COGER_FRUTA, Sound.class);
        assetManager.load(MUSIC_BG, Music.class);
        assetManager.finishLoading();

        textureAtlas = assetManager.get(ATLAS_MAP);
    }

    //metodo que me devuelve la textura del fondo
    public TextureRegion getBackground() {
        return this.textureAtlas.findRegion(BACKGROUND_IMAGE);
    }


    //metodo que me devuelve la animaciod del leñador 
    public Animation<TextureRegion> getWoodcutterAnimation(){
        return new Animation<TextureRegion>(0.33f,
                textureAtlas.findRegion("leniador1"));
    }

    //metodo que me devuelve la animaciod del leñador corriendo
    public Animation<TextureRegion> getWoodcutterRuningAnimation(){
        return new Animation<TextureRegion>(0.10f,
                textureAtlas.findRegion("leniador_run"),
                textureAtlas.findRegion("leniador_run2"),
                textureAtlas.findRegion("leniador_run3"),
                textureAtlas.findRegion("leniador_run4"),
                textureAtlas.findRegion("leniador_run5"),
                textureAtlas.findRegion("leniador_run6")
                );
    }

    //metodo que me devuelve la animaciod del leñador saltando
    public Animation<TextureRegion> getWoodcutterJumpAnimation(){
        return new Animation<TextureRegion>(0.15f,
                textureAtlas.findRegion("leniador_jump1"),
                textureAtlas.findRegion("leniador_jump2"),
                textureAtlas.findRegion("leniador_jump3"),
                textureAtlas.findRegion("leniador_jump4"),
                textureAtlas.findRegion("leniador_jump5"),
                textureAtlas.findRegion("leniador_jump6")

        );
    }

    //metodo que me devuelve la animaciod del leñador cuando ha muerto
    public Animation<TextureRegion> MuerteAnimacion(){
        return new Animation<TextureRegion>(0.15f,
                textureAtlas.findRegion("muerte1"),
                textureAtlas.findRegion("muerte2"),
                textureAtlas.findRegion("muerte3"),
                textureAtlas.findRegion("muerte4"),
                textureAtlas.findRegion("muerte5"),
                textureAtlas.findRegion("muerte6")

        );
    }

    //metodo que me devuelve los diferentes sprites de las piedras y frutas
    public TextureRegion getPiedra(){return this.textureAtlas.findRegion(PIEDRA);}
    public TextureRegion getPiedra2(){return this.textureAtlas.findRegion(PIEDRA2);}
    public TextureRegion getPiedra3(){return this.textureAtlas.findRegion(PIEDRA3);}

    public TextureRegion getFruta(){
        return this.textureAtlas.findRegion(FRUTA2);
    }
    public TextureRegion getFruta2(){
        return this.textureAtlas.findRegion(FRUTA6);
    }
    public TextureRegion getFruta3(){
        return this.textureAtlas.findRegion(FRUTA7);
    }
    public TextureRegion getFruta4(){
        return this.textureAtlas.findRegion(FRUTA8);
    }
    public TextureRegion getFruta5(){
        return this.textureAtlas.findRegion(FRUTA9);
    }
    public TextureRegion getFruta6(){
        return this.textureAtlas.findRegion(FRUTA10);
    }


    //metodo que me devuelve la fuente que utilñizare mas adelante
    public BitmapFont getFuente(){
        return new BitmapFont(Gdx.files.internal(FUENTE_FNT),Gdx.files.internal(FUENTE_PNG), false);
    }
    
    //diferentes metodos que me devuelven la musica y lso sonidos que utilizare en mi aplicacion
    public Sound getSonidoSalto(){
        return this.assetManager.get(SONIDO_SALTO);
    }
    public Sound getSonidoFruta(){
        return this.assetManager.get(COGER_FRUTA);
    }
    public Music getMusicBG(){
        return this.assetManager.get(MUSIC_BG);
    }


    public TextureRegion getGameOver() {
        return this.textureAtlas.findRegion(IMAGEN_GAMEOVER);
    }

}
