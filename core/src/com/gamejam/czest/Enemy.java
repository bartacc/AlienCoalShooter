package com.gamejam.czest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.gamejam.czest.Enemy.Type.EXPLODING;

/**
 * Created by bartek on 08.04.17.
 */
public class Enemy
{
    private GameplayScreen screen;
    protected Type type;
    protected boolean movedToPosition;

    protected float animationElapsedTime;

    protected Rectangle bounds;

    private float timeSinceLastShot;
    private float timeBetweenShots;

    public float targetYPos = 0;
    public float traveledDistance = 0;

    private boolean hitPlayer;

    public Enemy()
    {
        bounds = new Rectangle();
    }

    public Enemy(float centerX, float centerY, GameplayScreen screen, Type type)
    {
        this();
        init(centerX, centerY, screen, type);
    }

    public void init(float centerX, float centerY, GameplayScreen screen, Type type)
    {
        this.screen = screen;
        this.type = type;

        movedToPosition = false;
        targetYPos = centerY - Constants.Enemy.HEIGHT/2f;
        animationElapsedTime = 0;

        if(type != Type.INTRO_ENEMY)
        {
            bounds.set(centerX - Constants.Enemy.WIDTH / 2f,
                    centerY - Constants.Enemy.HEIGHT / 2f + Constants.Enemy.ENTERING_ARENA_SPAWN_OFFSET,
                    Constants.Enemy.WIDTH, Constants.Enemy.HEIGHT);
        }
        else if(type == Type.INTRO_ENEMY)
        {
            bounds.set(centerX - Constants.Enemy.WIDTH / 2f,
                    centerY - Constants.Enemy.HEIGHT / 2f,
                    Constants.Enemy.WIDTH, Constants.Enemy.HEIGHT);
            movedToPosition = true;
        }

        if(type == Type.IDLE_SHOOTING || type == Type.MOVING_SHOOTING)
        {
            timeBetweenShots = Constants.Enemy.MIN_TIME_BETWEEN_SHOTS;
            timeSinceLastShot = timeBetweenShots;
        }
    }

    protected void updateMovingToPosition(float delta)
    {
        if(!movedToPosition)
        {
            bounds.y -= Constants.Enemy.ENTERING_ARENA_VELOCITY * delta;
            traveledDistance += Constants.Enemy.ENTERING_ARENA_VELOCITY * delta;
            if(traveledDistance >= Constants.Enemy.ENTERING_ARENA_SPAWN_OFFSET)
                movedToPosition = true;
        }
    }

    public void update(float delta)
    {
        animationElapsedTime += delta;
        updateMovingToPosition(delta);

        if(!movedToPosition) return;

        if(type == Type.IDLE_SHOOTING || type == Type.MOVING_SHOOTING)
        {
            timeSinceLastShot += delta;
            if (timeSinceLastShot > timeBetweenShots)
            {
                timeSinceLastShot = 0;
                timeBetweenShots = Utils.randomFloat(Constants.Enemy.MIN_TIME_BETWEEN_SHOTS, Constants.Enemy.MAX_TIME_BETWEEN_SHOTS);
                screen.shootMissile(bounds.x + bounds.width / 2f,
                        bounds.y + bounds.height / 2f, 0,
                        -Utils.randomFloat(Constants.Enemy.MIN_SHOT_SPEED, Constants.Enemy.MAX_SHOT_SPEED),
                        Missile.Type.ENEMY_MISSILE);
            }
        }
    }

    public void render(SpriteBatch spriteBatch)
    {
        TextureRegion currentRegion = type.getAnimation().getKeyFrame(animationElapsedTime);
        spriteBatch.draw(currentRegion, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {return bounds;}

    public void hitPlayer() {hitPlayer = true;}
    public boolean didHitPlayer() {return hitPlayer;}

    public void destroyed()
    {
        type = EXPLODING;
        animationElapsedTime = 0;
        Assets.instance.sounds.enemyDestroyed.play();
    }
    public boolean isDestroyed()
    {
        return type == EXPLODING;
    }

    public boolean isReadyForRemoval()
    {
        return type == EXPLODING && type.getAnimation().isAnimationFinished(animationElapsedTime);
    }

    public enum Type
    {
        INTRO_ENEMY, IDLE_SHOOTING, MOVING_SHOOTING, FALLING, EXPLODING;

        public Animation getAnimation()
        {
            switch (this)
            {
                case IDLE_SHOOTING: return Assets.instance.enemies.shootingEnemy;
                case MOVING_SHOOTING: return Assets.instance.enemies.shootingEnemy;
                case INTRO_ENEMY: return Assets.instance.enemies.shootingEnemy;
                case EXPLODING: return Assets.instance.enemies.explosion;
                default: return Assets.instance.enemies.shootingEnemy;
            }
        }
    }
}
