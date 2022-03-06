package com.videojuego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.videojuego.extras.AssetMan;
import com.videojuego.screen.PantallaGameOver;
import com.videojuego.screen.PantallaInicio;
import com.videojuego.screen.PantallaJuego;

public class MainGame extends Game {

    public AssetMan assetManager;

    public PantallaJuego gameScreen;
    public PantallaGameOver gameOverScreen;
    public PantallaInicio pantallaInicio;

    @Override
    public void create() {
        this.assetManager = new AssetMan();
        this.gameScreen = new PantallaJuego(this);
        this.gameOverScreen = new PantallaGameOver(this);
        this.pantallaInicio = new PantallaInicio(this);
        //Scene2d nos ayuda a manejar las diferentes instancias de las diferentes pantallas que
        //compondrá nuestro juego.
        setScreen(this.pantallaInicio);
    }


}
