package de.nicolas.sfs.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.nicolas.sfs.SFS;
import de.nicolas.sfs.resources.Assets;
import de.nicolas.sfs.resources.GlobalVariables;

public class Fighter {

    // Anzahl der Zeilen und Spalten in den Animations SpriteSheets
    private static final int FRAME_ROWS = 2;
    private static final int FRAME_COLS = 3;

    // Bewegungsgeschwindigkeit des Fighters
    public static final float MOVEMENT_SPEED = 10f;

    // Maximales Leben eines Fighters
    public static final float MAX_LIVE = 100f;

    // Schaden, den ein Schlag verursacht
    public static final float HIT_STRENGTH = 5f;

    // Faktor für Schaden eines Schlages, der geblockt wurde
    public static final float BLOCK_DAMAGE_FACTOR = 0.2f;

    // Fighter Details
    private String name;
    private Color color;

    // Zustand des Fighters
    public enum State{
        BLOCK,
        HURT,
        IDLE,
        KICK,
        LOSE,
        PUNCH,
        WALK,
        WIN
    }

    private State state;
    private float stateTime;
    private State renderState;
    private float renderStateTime;
    private final Vector2 position = new Vector2();
    private final Vector2 movementDirection = new Vector2();
    private float life;
    private int facing;
    private boolean madeContact;

    // Animationen
    private Animation<TextureRegion> blockAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> kickAnimation;
    private Animation<TextureRegion> loseAnimation;
    private Animation<TextureRegion> punchAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> winAnimation;

    public Fighter(SFS game, String name, Color color){
        this.name = name;
        this.color = color;

        // Animationen initialisieren
        initializeBlockAnimation(game.assets.assetManager);
        initializeHurtAnimation(game.assets.assetManager);
        initializeIdleAnimation(game.assets.assetManager);
        initializeKickAnimation(game.assets.assetManager);
        initializeLoseAnimation(game.assets.assetManager);
        initializePunchAnimation(game.assets.assetManager);
        initializeWalkAnimation(game.assets.assetManager);
        initializeWinAnimation(game.assets.assetManager);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void getReady(float positionX, float positionY){
        state = renderState = State.IDLE;
        stateTime = renderStateTime = 0f;
        position.set(positionX, positionY);
        movementDirection.set(0, 0);
        life = MAX_LIVE;
        madeContact = false;
    }

    public void render(SpriteBatch batch){
        // Ermitteln des aktuellen Frames der gezeichnet werden muss
        TextureRegion currentFrame;
        switch (renderState){
            case BLOCK:
                currentFrame = blockAnimation.getKeyFrame(renderStateTime, true);
                break;
            case HURT:
                currentFrame = hurtAnimation.getKeyFrame(renderStateTime, false);
                break;
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(renderStateTime, true);
                break;
            case KICK:
                currentFrame = kickAnimation.getKeyFrame(renderStateTime, false);
                break;
            case LOSE:
                currentFrame = loseAnimation.getKeyFrame(renderStateTime, false);
                break;
            case PUNCH:
                currentFrame = punchAnimation.getKeyFrame(renderStateTime, false);
                break;
            case WALK:
                currentFrame = walkAnimation.getKeyFrame(renderStateTime, true);
                break;
            default:
                currentFrame = winAnimation.getKeyFrame(renderStateTime, true);
        }

        batch.setColor(color);
        batch.draw(currentFrame,
            position.x, position.y,
            currentFrame.getRegionWidth() * 0.5f * GlobalVariables.WORLD_SCALE,
            0,
            currentFrame.getRegionWidth() * GlobalVariables.WORLD_SCALE,
            currentFrame.getRegionHeight() * GlobalVariables.WORLD_SCALE,
            facing, 1,
            0);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float deltaTime){
        // stateTime aktualisieren
        stateTime += deltaTime;

        // nur updaten, wenn deltaTime größer als null ist
        if(deltaTime > 0){
            renderState = state;
            renderStateTime = stateTime;
        }

        if (state == State.WALK){
            position.x += movementDirection.x * MOVEMENT_SPEED * deltaTime;
            position.y += movementDirection.y * MOVEMENT_SPEED * deltaTime;
        } else if ((state == State.PUNCH && punchAnimation.isAnimationFinished(stateTime)) ||
            (state == State.KICK && kickAnimation.isAnimationFinished(stateTime)) ||
            (state == State.HURT && hurtAnimation.isAnimationFinished(stateTime))){
            // wenn eine Bewegungsrichtung gegeben ist -> walk - sonst idle
            if(movementDirection.x != 0 || movementDirection.y != 0){
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public void faceLeft(){
        facing = -1;
    }

    public void faceRight(){
        facing = 1;
    }

    public void changeState(State newState){
        state = newState;
        stateTime = 0f;
    }

    private void setMovement(float x, float y){
        movementDirection.set(x, y);
        if (state == State.WALK && x == 0 && y == 0){
            changeState(State.IDLE);
        } else if (state == State.IDLE && (x !=0 || y != 0)){
            changeState(State.WALK);
        }
    }

    public void moveLeft(){
        setMovement(-1, movementDirection.y);
    }

    public void moveRight(){
        setMovement(1, movementDirection.y);
    }

    public void moveUp(){
        setMovement(movementDirection.x, 1);
    }

    public void moveDown(){
        setMovement(movementDirection.x, -1);
    }

    public void stopMovingLeft(){
        if (movementDirection.x == -1){
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingRight(){
        if (movementDirection.x == 1){
            setMovement(0, movementDirection.y);
        }
    }

    public void stopMovingUP(){
        if (movementDirection.y == 1){
            setMovement(movementDirection.x, 0);
        }
    }

    public void stopMovingDown(){
        if (movementDirection.y == -1){
            setMovement(movementDirection.x, 0);
        }
    }

    public void block(){
        if (state == State.IDLE || state == State.WALK){
            changeState(State.BLOCK);
        }
    }

    public void stopBlocking(){
        if (state == State.BLOCK){
            // wenn eine Bewegungsrichtung gegeben ist -> walk - sonst idle
            if(movementDirection.x != 0 || movementDirection.y != 0){
                changeState(State.WALK);
            } else {
                changeState(State.IDLE);
            }
        }
    }

    public boolean isBlocking(){
        return state == State.BLOCK;
    }

    public void punch(){
        if (state == State.IDLE || state == State.WALK){
            changeState(State.PUNCH);

            // gerade begonnen, noch kein Kontakt erfolgt
            madeContact = false;
        }
    }

    public void kick(){
        if (state == State.IDLE || state == State.WALK){
            changeState(State.KICK);

            // gerade begonnen, noch kein Kontakt erfolgt
            madeContact = false;
        }
    }

    public void makeContact(){
        madeContact = true;
    }

    public boolean hasMadeContact(){
        return madeContact;
    }

    public boolean isAttacking(){
        return state == State.PUNCH || state == State.KICK;
    }

    public boolean isAttackActive(){
        // der Angriff ist nur aktiv, wenn noch kein Angriff stattgefunden hat und der Angriff
        // nicht gerade begonnen hat oder beendet wird.
        if (hasMadeContact()){
            return false;
        } else if (state == State.PUNCH){
            return stateTime > punchAnimation.getAnimationDuration() * 0.33f &&
                stateTime < punchAnimation.getAnimationDuration() * 0.66f;
        }else if (state == State.KICK){
            return stateTime > kickAnimation.getAnimationDuration() * 0.33f &&
                stateTime < kickAnimation.getAnimationDuration() * 0.66f;
        }else {
            return false;
        }
    }

    public void getHit(float damage){
        if (state == State.HURT || state == State.WIN || state == State.LOSE){
            return;
        }
        // Leben wird reduziert, wenn geblockt wird um einen geringeren Betrag
        life -= state == State.BLOCK ? damage * BLOCK_DAMAGE_FACTOR : damage;
        if(life <= 0f){
            lose();
        } else if (state != State.BLOCK){
            changeState(State.HURT);
        }
    }

    public void lose(){
        changeState(State.LOSE);
        life = 0;
    }

    private void initializeBlockAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.BLOCK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        blockAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeHurtAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.HURT_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        hurtAnimation = new Animation<>(0.03f, frames);
    }

    private void initializeIdleAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.IDLE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        idleAnimation = new Animation<>(0.1f, frames);
    }

    private void initializeKickAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.KICK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        kickAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeLoseAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.LOSE_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        loseAnimation = new Animation<>(0.05f, frames);
    }

    private void initializePunchAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.PUNCH_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        punchAnimation = new Animation<>(0.05f, frames);
    }

    private void initializeWalkAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.WALK_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        walkAnimation = new Animation<>(0.08f, frames);
    }

    private void initializeWinAnimation(AssetManager assetManager){
        Texture spriteSheet = assetManager.get(Assets.WIN_SPRITE_SHEET);
        TextureRegion[] frames = getAnimationFrames(spriteSheet);
        winAnimation = new Animation<>(0.05f, frames);
    }

    private TextureRegion[] getAnimationFrames(Texture spriteSheet){
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
            spriteSheet.getWidth() / FRAME_COLS,
            spriteSheet.getHeight() / FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++){
            for (int j = 0; j < FRAME_COLS; j++){
                frames[index] = tmp[i][j];
                index++;
            }
        }
        return frames;
    }
}
