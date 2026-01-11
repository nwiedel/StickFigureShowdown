package de.nicolas.sfs.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import de.nicolas.sfs.SFS;
import de.nicolas.sfs.resources.Assets;
import de.nicolas.sfs.resources.GlobalVariables;

public class GameScreen implements Screen, InputProcessor {

    private final SFS game;
    private final ExtendViewport viewport;

    // Hintergrund/ Ring
    private Texture backgroundTexture;
    private Texture frontRopeTexture;
    private static final float RING_MIN_X = 7f;
    private static final float RING_MAX_X = 60f;
    private static final float RING_MIN_Y = 4f;
    private static final float RING_MAX_Y = 22f;
    private static final float RING_SLOPE = 3.16f;

    // Fighter
    private static final float PLAYER_START_POSITION_X = 16f;
    private static final float OPPONENT_START_POSITION_X = 51f;
    private static final float FIGHTER_START_POSITION_Y = 15f;

    public GameScreen(SFS game) {
        this.game = game;

        viewport = new ExtendViewport(GlobalVariables.WORLD_WIDTH, GlobalVariables.MIN_WORLD_HEIGHT,
            GlobalVariables.WORLD_WIDTH, 0);

        createGameArea();

        // get Ready
        game.player.getReady(PLAYER_START_POSITION_X, FIGHTER_START_POSITION_Y);
        game.opponent.getReady(OPPONENT_START_POSITION_X, FIGHTER_START_POSITION_Y);
    }

    private void createGameArea() {
        backgroundTexture = game.assets.assetManager.get(Assets.BACKGROUND_TEXTURE);
        frontRopeTexture = game.assets.assetManager.get(Assets.FRONT_ROPES_TEXTURE);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Spiel aktualisieren
        update(delta);

        // dem Spritebatch mitteilen, die Camera zu benutzen!
        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // zeichnen beginnen
        game.batch.begin();

        // zeichnen
        game.batch.draw(backgroundTexture,
            0, 0,
            backgroundTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            backgroundTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // Fighters zeichnen
        renderFighters();

        // vordere Ringbegrenzung zeichnen
        game.batch.draw(frontRopeTexture,
            0, 0,
            frontRopeTexture.getWidth() * GlobalVariables.WORLD_SCALE,
            frontRopeTexture.getHeight() * GlobalVariables.WORLD_SCALE);

        // zeichen beenden
        game.batch.end();
    }

    private void renderFighters() {
        // Y Koordinate benutzen, um zu entscheiden, welcher Fighter zuerst gezeichent wird
        if (game.player.getPosition().y > game.opponent.getPosition().y){
            // Spieler zeichnen
            game.player.render(game.batch);
            // Opponent zeichnen
            game.opponent.render(game.batch);
        } else {
            // Opponent zeichnen
            game.opponent.render(game.batch);
            // Spieler zeichnen
            game.player.render(game.batch);

        }
    }

    private void update(float deltaTime) {
        game.player.update(deltaTime);
        game.opponent.update(deltaTime);

        if(game.player.getPosition().x <= game.opponent.getPosition().x){
            game.player.faceRight();
            game.opponent.faceLeft();
        } else {
            game.player.faceLeft();
            game.opponent.faceRight();
        }

        // Die Fighter im Ring halten
        keepWithinRingBounds(game.player.getPosition());
        keepWithinRingBounds(game.opponent.getPosition());
    }

    private void keepWithinRingBounds(Vector2 position){
        if(position.y < RING_MIN_Y){
            position.y = RING_MIN_Y;
        }else if (position.y > RING_MAX_Y){
            position.y = RING_MAX_Y;
        }
        if (position.x < position.y / RING_SLOPE + RING_MIN_X){
            position.x = position.y / RING_SLOPE + RING_MIN_X;
        } else if (position.x > position.y / -RING_SLOPE + RING_MAX_X){
            position.x = position.y / -RING_SLOPE + RING_MAX_X;
        }
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

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            game.player.moveLeft();
        }else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            game.player.moveRight();
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W){
            game.player.moveUp();
        }else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            game.player.moveDown();
        }

        // kämpfen
        if (keycode == Input.Keys.B){
            game.player.block();
        } else if (keycode == Input.Keys.F){
            game.player.punch();
        } else if (keycode == Input.Keys.V){
            game.player.kick();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            game.player.stopMovingLeft();
        }else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            game.player.stopMovingRight();
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W){
            game.player.stopMovingUP();
        }else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            game.player.stopMovingDown();
        }

        // Blocking
        if (keycode == Input.Keys.B){
            game.player.stopBlocking();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
