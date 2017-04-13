package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by bartek on 08.04.17.
 */
public class Player extends InputAdapter
{
    public final static String TAG = Player.class.getSimpleName();

    private Rectangle bounds;
    private Rectangle wallHitbox;
    private Rectangle enemyDamageHitbox;

    private GameplayScreen screen;

    private AnimationState animationState;
    private SideTile.Side lastSide;

    private float recoilVelocity;

    private int lives;
    private int coalAmmo;

    public Player(float x, float y, GameplayScreen screen)
    {
        bounds = new Rectangle();
        wallHitbox = new Rectangle();
        enemyDamageHitbox = new Rectangle();

        init(x, y, screen);
    }

    public void init(float x, float y, GameplayScreen screen)
    {
        this.screen = screen;
        bounds.set(x - Constants.Player.WIDTH/2f, y - Constants.Player.HEIGHT/2f,
                Constants.Player.WIDTH, Constants.Player.HEIGHT);

        updateHitboxes();

        lives = Constants.Player.INITIAL_LIVES;
        coalAmmo = Constants.Player.INITIAL_AMMO;

        animationState = AnimationState.IDLE_FALLING;
        animationState.elapsedTime = 0;
    }

    private void updateHitboxes()
    {
        wallHitbox.set(bounds.x, bounds.y + (bounds.height - Constants.Player.WALL_HITBOX_HEIGHT)/2f,
                bounds.width, Constants.Player.WALL_HITBOX_HEIGHT);
        enemyDamageHitbox.set(bounds.x + (bounds.width - Constants.Player.ENEMY_DAMAGE_HITBOX_WIDTH)/2f, bounds.y,
                Constants.Player.ENEMY_DAMAGE_HITBOX_WIDTH, bounds.height);
    }

    public void update(float delta)
    {
        animationState.elapsedTime += delta;

        if(recoilVelocity > 0)
        {
            recoilVelocity -= Constants.Player.RECOIL_DEACCELERATION * delta;
            if(recoilVelocity < 0) recoilVelocity = 0;
        }
        else if(recoilVelocity < 0)
        {
            recoilVelocity += Constants.Player.RECOIL_DEACCELERATION * delta;
            if(recoilVelocity > 0) recoilVelocity = 0;
        }

        if(recoilVelocity != 0)
            bounds.x += recoilVelocity * delta;
        else
        {

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                if(animationState == AnimationState.IDLE_FALLING)
                {
                    animationState = AnimationState.SIDE_MOVEMENT;
                    animationState.elapsedTime = 0;
                }

                bounds.x -= Constants.Player.MOVE_VELOCITY * delta;
                lastSide = SideTile.Side.LEFT;
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                if(animationState == AnimationState.IDLE_FALLING)
                {
                    animationState = AnimationState.SIDE_MOVEMENT;
                    animationState.elapsedTime = 0;
                }

                bounds.x += Constants.Player.MOVE_VELOCITY * delta;
                lastSide = SideTile.Side.RIGHT;
            }
            else  if(animationState != AnimationState.IDLE_FALLING &&
                    animationState.stateAnimation.isAnimationFinished(animationState.elapsedTime))
            {
                animationState = AnimationState.IDLE_FALLING;
                animationState.elapsedTime = 0;
            }
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && coalAmmo > 0)
        {
            screen.shootMissile(bounds.x + bounds.width / 2f,
                    bounds.y + bounds.height / 2f, Constants.Player.SHOT_SPEED,
                    Missile.Type.COAL_MISSILE);
            coalAmmo--;
            Gdx.app.log(TAG, "Shot coal, ammo: " + coalAmmo);

            if(animationState == AnimationState.IDLE_FALLING)
            {
                animationState = AnimationState.THROW_FALLING;
                animationState.elapsedTime = 0;
            }
            else if(animationState == AnimationState.SIDE_MOVEMENT)
            {
                animationState = AnimationState.THROW_MOVING;
                animationState.elapsedTime = 0;
            }
        }

        if(animationState != AnimationState.IDLE_FALLING && animationState != AnimationState.SIDE_MOVEMENT &&
                animationState.stateAnimation.isAnimationFinished(animationState.elapsedTime))
        {
            animationState = AnimationState.IDLE_FALLING;
            animationState.elapsedTime = 0;
        }

        updateHitboxes();
    }

    public void checkLeftTileCollision(SideTile tile)
    {
        Rectangle tileBounds = tile.getBounds();

        if(tileBounds.overlaps(wallHitbox))
        {
            bounds.x = tileBounds.x + tileBounds.width;
            hitTile(tile);
        }

       /* if(bounds.x <= tileBounds.x + tileBounds.width &&
                (bounds.y < tileBounds.y + tileBounds.height || bounds.y + bounds.height > tileBounds.y))
        {
            if(bounds.x != tileBounds.x + tileBounds.width)
                bounds.x = tileBounds.x + tileBounds.width;
            hitTile(tile);
        }*/
    }

    public void checkRightTileCollision(SideTile tile)
    {
        Rectangle tileBounds = tile.getBounds();

        if(tileBounds.overlaps(wallHitbox))
        {
            bounds.x = tileBounds.x - bounds.width;
            hitTile(tile);
        }

/*        if(bounds.x + bounds.width >= tileBounds.x &&
                (bounds.y < tileBounds.y + tileBounds.height || bounds.y + bounds.height > tileBounds.y))
        {
            if(bounds.x + bounds.width != tileBounds.x)
                bounds.x = tileBounds.x - bounds.width;
            hitTile(tile);
        }*/
    }

    public void checkEnemyCollision(Enemy enemy)
    {
        Rectangle enemyBounds = enemy.getBounds();

        if(enemyBounds.overlaps(enemyDamageHitbox) && !enemy.didHitPlayer())
        {
            substractLife();
            enemy.hitPlayer();
        }
    }

    public void render(SpriteBatch spriteBatch)
    {
        float scaleX = 1;
        if(lastSide == SideTile.Side.RIGHT) scaleX = -1;

        spriteBatch.draw(animationState.getTextureRegion(),
                bounds.x, bounds.y, bounds.width/2f, bounds.height/2f, bounds.width, bounds.height,
                scaleX, 1, 0);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(wallHitbox.x, wallHitbox.y, wallHitbox.width, wallHitbox.height);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(enemyDamageHitbox.x, enemyDamageHitbox.y, enemyDamageHitbox.width, enemyDamageHitbox.height);
    }

    public void substractLife()
    {
        lives--;
        animationState = AnimationState.TAKEN_DAMAGE;
        animationState.elapsedTime = 0;
        Assets.instance.sounds.damage.play();

        if(lives == 0) screen.game.initLoseScreen();
    }
    public void hitTile(SideTile tile)
    {
        if(tile.getType() == SideTile.Type.Coal && !tile.wasHitByPlayer() && coalAmmo < Constants.Player.MAX_AMMO)
        {
            int addedAmmo = Utils.randomInt(Constants.SideTile.MIN_AMMO_ADDED_AFTER_COLLISION,
                    Constants.SideTile.MAX_AMMO_ADDED_AFTER_COLLISION);
            coalAmmo += addedAmmo;
            if(coalAmmo > Constants.Player.MAX_AMMO) coalAmmo = Constants.Player.MAX_AMMO;

            Gdx.app.log(TAG, "Added " + addedAmmo + " coal ammo, sum: " + coalAmmo);

            tile.hitByPlayer();
            recoilVelocity = -Constants.Player.RECOIL_VELOCITY_FROM_COAL * tile.getSide().getIntDirection();

            animationState = AnimationState.MINING;
            animationState.elapsedTime = 0;

            Assets.instance.sounds.mining[Utils.randomInt(0, 2)].play(Constants.Sound.MINING_VOLUME);
        }
        else if(tile.getType() != SideTile.Type.Coal)
        {
            recoilVelocity = -Constants.Player.RECOIL_VELOCITY_DAMAGE * tile.getSide().getIntDirection();
            substractLife();
        }
    }
    //public void addAmmo(int amount) {coalAmmo += amount;}


    public int getLives() {return lives;}
    public int getCoalAmmo() {return coalAmmo;}
    public Rectangle getBounds() {return bounds;}
    public Rectangle getEnemyDamageHitbox() {return enemyDamageHitbox;}


    public enum AnimationState
    {
        IDLE_FALLING(null),
        SIDE_MOVEMENT(Assets.instance.player.sideMovement),
        THROW_FALLING(Assets.instance.player.idleThrow),
        THROW_MOVING(Assets.instance.player.sideMovementThrow),
        TAKEN_DAMAGE(Assets.instance.player.takenDamage),
        MINING(Assets.instance.player.mining);

        AnimationState(Animation currentAnimation)
        {
            this.stateAnimation = currentAnimation;
        }

        public TextureRegion getTextureRegion()
        {
            if(this == IDLE_FALLING) return Assets.instance.player.idleFalling;
            else return stateAnimation.getKeyFrame(elapsedTime);
        }

        private final Animation stateAnimation;
        public static float elapsedTime = 0;
    }
}
