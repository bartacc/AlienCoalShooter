package com.gamejam.czest.enemies;

import com.gamejam.czest.entities.SideTile;

/**
 * Created by bartek on 08.04.17.
 */
public class EnemyDef
{
    public float playableArenaPercentageX, playableArenaPercentageY;
    public Enemy.Type enemyType;

    public float timeToStartShake;

    public float leftMaxOffset;
    public float rightMaxOffset;
    public SideTile.Side initialMovementDirection;

    public EnemyDef()
    {
    }

    public EnemyDef initIdleShooting(float playableArenaPercentageX, float playableArenaPercentageY)
    {
        this.playableArenaPercentageX = playableArenaPercentageX;
        this.playableArenaPercentageY = playableArenaPercentageY;
        this.enemyType = Enemy.Type.IDLE_SHOOTING;
        return this;
    }

    public EnemyDef initFalling(float playableArenaPercentageX, float playableArenaPercentageY, float timeToStartShake)
    {
        this.playableArenaPercentageX = playableArenaPercentageX;
        this.playableArenaPercentageY = playableArenaPercentageY;

        this.enemyType = Enemy.Type.FALLING;
        this.timeToStartShake = timeToStartShake;

        return this;
    }

    public EnemyDef initMovingShooting(float playableArenaPercentageX, float playableArenaPercentageY,
                                       float leftMaxOffsetPercentage, float rightMaxOffsetPercentage, SideTile.Side initialDirection)
    {
        this.playableArenaPercentageX = playableArenaPercentageX;
        this.playableArenaPercentageY = playableArenaPercentageY;

        this.enemyType = Enemy.Type.MOVING_SHOOTING;

        this.leftMaxOffset = leftMaxOffsetPercentage;
        this.rightMaxOffset = rightMaxOffsetPercentage;
        this.initialMovementDirection = initialDirection;

        return this;
    }

    public EnemyDef initIntroEnemy()
    {
        playableArenaPercentageX = 1;
        playableArenaPercentageY = 0.2f;

        enemyType = Enemy.Type.INTRO_ENEMY;

        leftMaxOffset = 1;
        rightMaxOffset = 0;
        initialMovementDirection = SideTile.Side.LEFT;

        return this;
    }
}
