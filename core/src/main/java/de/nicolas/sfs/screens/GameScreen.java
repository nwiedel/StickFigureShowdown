package de.nicolas.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import de.nicolas.sfs.SFS;
import de.nicolas.sfs.resources.Assets;

public class GameScreen implements Screen {

    private final SFS game;

    // Hintergrund/ Ring
    private Texture backgroundTexture;
    private Texture frontRopeTexture;

    public GameScreen(SFS game){
        this.game = game;
    }

    private void createGameArea(){
        backgroundTexture = game.assets.assetManager.get(Assets.BACKGROUND_TEXTURE);
        frontRopeTexture = game.assets.assetManager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
