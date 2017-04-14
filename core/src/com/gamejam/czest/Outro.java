package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by bartek on 13.04.17.
 */
public class Outro
{
    public final static String TAG = Outro.class.getSimpleName();

    private GameplayScreen screen;

    private Player player;
    private Background background;
    private Viewport viewport;

    private float playerEndDistanceToSpawnExit;
    private boolean spawnedExit;
    private float timeToWaitBeforeMovement;

    private boolean spawnedEpilogueEnemies;

    public void startOutro(GameplayScreen screen)
    {
        this.screen = screen;

        this.player = screen.getPlayer();
        this.background = screen.getBackground();
        this.viewport = screen.getViewport();

        spawnedExit = false;
        spawnedEpilogueEnemies = false;

        float exitDstToTravel = Constants.Player.INITIAL_Y_POS + (Constants.SideTile.HEIGHT * Constants.Background.EXIT_HEIGHT);
        float exitTravelDuration = exitDstToTravel / Constants.SideTile.FALL_SPEED;
        playerEndDistanceToSpawnExit = viewport.getWorldWidth() - (viewport.getWorldWidth() - Constants.Player.MOVE_VELOCITY * exitTravelDuration);

        float playerEndDistance = viewport.getWorldWidth() - player.getBounds().x;
        if(playerEndDistance <= playerEndDistanceToSpawnExit)
        {
            float currentTargetDst = playerEndDistanceToSpawnExit - playerEndDistance;
            timeToWaitBeforeMovement = currentTargetDst / Constants.Player.MOVE_VELOCITY;

            background.spawnExit();
            spawnedExit = true;
        }
    }

    public void update(float delta)
    {
        if(timeToWaitBeforeMovement > 0)
        {
            timeToWaitBeforeMovement -= delta;
            Gdx.app.log(TAG, "Waiting, remaining time: " + timeToWaitBeforeMovement);
            return;
        }

        player.queueMove(SideTile.Side.RIGHT);

        float playerEndDistance = viewport.getWorldWidth() - player.getBounds().x;
        if(!spawnedExit && playerEndDistance <= playerEndDistanceToSpawnExit)
        {
            background.spawnExit();
            spawnedExit = true;
        }

        if(!spawnedEpilogueEnemies && player.getBounds().x > viewport.getWorldWidth())
        {
            //screen.setPhase(EnemyPhase.OUTRO);
            spawnedEpilogueEnemies = true;
            //screen.getGame().initWinScreen();
        }
    }
}
