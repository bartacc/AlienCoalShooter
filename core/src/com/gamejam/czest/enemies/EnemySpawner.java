package com.gamejam.czest.enemies;

import com.badlogic.gdx.utils.Array;
import com.gamejam.czest.Constants;
import com.gamejam.czest.screens.GameplayScreen;
import com.gamejam.czest.entities.SideTile;

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

            else if(def.enemyType == Enemy.Type.INTRO_ENEMY)
            {
                EnemyMoving introEnemy = new EnemyMoving();
                float x = screen.getViewport().getWorldWidth() + Constants.INTRO.PLAYER_ENEMY_DISTANCE + Constants.Enemy.WIDTH/2f;
                float y = Constants.Player.INITIAL_Y_POS + Constants.Enemy.HEIGHT/2f;
                introEnemy.initIntroEnemy(x, y, screen, screen.getViewport().getWorldWidth(), 0, SideTile.Side.LEFT);
                enemy = introEnemy;
            }

            outArray.add(enemy);
        }
    }
}
