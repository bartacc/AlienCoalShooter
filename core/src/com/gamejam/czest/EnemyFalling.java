package com.gamejam.czest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by bartek on 10.04.17.
 */
public class EnemyFalling extends Enemy
{
    private FallingState fallingState;
    private float fallingVelocity;
    private float fallingStateElapsedTime;

    private float timeToStartShake;
    private float shakingXoffset;
    private SideTile.Side shakeDirection;

    public EnemyFalling(float centerX, float centerY, GameplayScreen screen, float timeToStartShake)
    {
        super();
        init(centerX, centerY, screen, timeToStartShake);
    }

    public void init(float centerX, float centerY, GameplayScreen screen, float timeToStartShake)
    {
        super.init(centerX, centerY, screen, Type.FALLING);

        this.timeToStartShake = timeToStartShake;
        shakingXoffset = 0;
        fallingStateElapsedTime = 0;
        fallingVelocity = 0;
        shakeDirection = SideTile.Side.LEFT;
        fallingState = FallingState.WAITING;
        bounds.set(centerX - Constants.Enemy.FALLING_ENEMY_IDLE_SIZE/2f,
                centerY - Constants.Enemy.FALLING_ENEMY_IDLE_SIZE/2f + Constants.Enemy.ENTERING_ARENA_SPAWN_OFFSET,
                Constants.Enemy.FALLING_ENEMY_IDLE_SIZE, Constants.Enemy.FALLING_ENEMY_IDLE_SIZE);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        if(!movedToPosition || type == Type.EXPLODING) return;

        if(fallingState == FallingState.WAITING)
        {
            fallingStateElapsedTime += delta;
            if(fallingStateElapsedTime >= timeToStartShake)
            {
                fallingStateElapsedTime = 0;
                fallingState = FallingState.SHAKING;
            }
        }
        else if(fallingState == FallingState.SHAKING)
        {
            if(shakeDirection == SideTile.Side.LEFT)
            {
                shakingXoffset -= Constants.Enemy.SHAKE_VELOCITY * delta;
                bounds.x -= Constants.Enemy.SHAKE_VELOCITY * delta;
                if(shakingXoffset < -Constants.Enemy.SHAKING_OFFSET)
                    shakeDirection = SideTile.Side.RIGHT;
            }
            else if(shakeDirection == SideTile.Side.RIGHT)
            {
                shakingXoffset += Constants.Enemy.SHAKE_VELOCITY * delta;
                bounds.x += Constants.Enemy.SHAKE_VELOCITY * delta;
                if(shakingXoffset > Constants.Enemy.SHAKING_OFFSET)
                    shakeDirection = SideTile.Side.LEFT;
            }
            fallingStateElapsedTime += delta;
            if(fallingStateElapsedTime > Constants.Enemy.SHAKE_DURATION)
            {
                shakingXoffset = 0;
                fallingState = FallingState.FALLING;
                fallingStateElapsedTime = 0;
                fallingVelocity = Constants.Enemy.FALLING_INITIAL_VELOCITY;

                bounds.setHeight(Constants.Enemy.FALLING_ENEMY_HEIGHT);
            }
        }
        else if(fallingState == FallingState.FALLING)
        {
            bounds.y += fallingVelocity * delta;
            fallingVelocity += Constants.Enemy.FALLING_ACCELERATION * delta;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        if(type == Type.EXPLODING) super.render(spriteBatch);
        else
        {
            TextureRegion currentRegion = Type.FALLING.getAnimation().getKeyFrame(animationElapsedTime);

            if (fallingState == FallingState.WAITING || fallingState == FallingState.SHAKING)
                currentRegion = Assets.instance.enemies.fallingEnemyIdle;
            else if (fallingState == FallingState.FALLING)
                currentRegion = Assets.instance.enemies.fallingEnemyFalling;

            spriteBatch.draw(currentRegion, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.x + shakingXoffset, bounds.y, bounds.width, bounds.height);
    }

    public enum FallingState
    {
        WAITING, SHAKING, FALLING;
    }
}
