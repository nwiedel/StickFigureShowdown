package de.nicolas.sfs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.nicolas.sfs.resources.Assets;
import de.nicolas.sfs.screens.GameScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class SFS extends Game {

    public SpriteBatch batch;
    public Assets assets;

    // Screens
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();

        // Lade alle Assets
        assets.load();
        assets.assetManager.finishLoading();

        // GameScreen initialisieren und dahin wechseln
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assets.dispose();
    }
}
