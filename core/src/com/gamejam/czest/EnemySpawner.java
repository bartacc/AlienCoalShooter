package com.gamejam.czest;

import com.badlogic.gdx.utils.Array;

/**
 * Created by bartek on 08.04.17.
 */
public class EnemySpawner
{
    public static void initEnemies(EnemyPhase phase, GameplayScreen screen, Array<Enemy> outArray)
    {
        float playableArenaWidth = screen.getViewport().getWorldWidth() - (Constants.SideTile.WIDTH * 2f);
        float playableArenaHeight = screen.getViewport().getWorldHeight();

        for(EnemyDef def : phase.enemyDefs)
        {
            float centerX = Constants.SideTile.WIDTH + (def.playableArenaPercentageX * playableArenaWidth);
            float centerY = playableArenaHeight * def.playableArenaPercentageY;

            Enemy enemy = null;
            if(def.enemyType == Enemy.Type.IDLE_SHOOTING)
                enemy = new Enemy(centerX, centerY, screen, def.enemyType);

            else if(def.enemyType == Enemy.Type.MOVING_SHOOTING)
            {
                float leftMaxOffset = playableArenaWidth * def.leftMaxOffset;
                float rightMaxOffset = playableArenaWidth * def.rightMaxOffset;
                enemy = new EnemyMoving(centerX, centerY, screen, leftMaxOffset, rightMaxOffset, def.initialMovementDirection);
            }

            else if(def.enemyType == Enemy.Type.FALLING)
                enemy = new EnemyFalling(centerX, centerY, screen, def.timeToStartShake);


            outArray.add(enemy);
        }
    }
}
