package de.nicolas.sfs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.nicolas.sfs.objects.Fighter;
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

    // Fighters deklarieren
    public Fighter player;
    public Fighter opponent;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();

        // Lade alle Assets
        assets.load();
        assets.assetManager.finishLoading();

        // Fighters initialisieren
        player = new Fighter(this, "Slim Stallone", new Color(1f, 0.2f, 0.2f, 1f));
        opponent = new Fighter(this,"Thin Disel", new Color(0.25f, 0.7f, 1f, 1f));

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
