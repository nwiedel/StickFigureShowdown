package de.nicolas.sfs.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import de.nicolas.sfs.SFS;
import de.nicolas.sfs.resources.Assets;
import de.nicolas.sfs.resources.GlobalVariables;

public class GameScreen implements Screen {

    private final SFS game;
    private final ExtendViewport viewport;

    // Hintergrund/ Ring
    private Texture backgroundTexture;
    private Texture frontRopeTexture;

    public GameScreen(SFS game){
        this.game = game;

        viewport = new ExtendViewport(GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
            GlobalVariables.WORLD_WIDTH, 0);


        createGameArea();
    }

    private void createGameArea(){
        backgroundTexture = game.assets.assetManager.get(Assets.BACKGROUND_TEXTURE);
        frontRopeTexture = game.assets.assetManager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // dem Spritebatch mitteilen, die Camera zu benutzen!
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // zeichnen beginnen
        game.batch.begin();

        // zeichnen
        game.batch.draw(backgroundTexture,
            0, 0,
            backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // zeichen beenden
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
