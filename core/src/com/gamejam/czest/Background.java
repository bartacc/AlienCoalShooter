package com.gamejam.czest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.gamejam.czest.SideTile.Side.LEFT;
import static com.gamejam.czest.SideTile.Side.RIGHT;

/**
 * Created by bartek on 08.04.17.
 */
public class Background
{
    public static final String TAG = Background.class.getSimpleName();

    private float skyYPos;
    private float[] grassTilesYpos = {0, 0};
    private Array<Vector2> backgroundPositions;
    private float backgroundTileSize;
    private float backgroundYVelocity;

    private Array<SideTile> leftTiles;
    private Array<SideTile> rightTiles;

    private Player player;
    private Viewport viewport;

    private float fallSpeed;


    private boolean tileBelowExitSpawned = false;
    private int tilesToOmit = 0;
    private boolean spawnedExit;
    private float exitPos;
    private boolean stopped;
    private float doorYpos;

    public Background(Player player, ExtendViewport viewport)
    {
        leftTiles = new Array<SideTile>();
        rightTiles = new Array<SideTile>();
        backgroundPositions = new Array<Vector2>();

        init(player, viewport);
    }

    public void init(Player player, Viewport viewport)
    {
        this.player = player;
        this.viewport = viewport;

        backgroundTileSize = viewport.getWorldWidth() - (Constants.SideTile.WIDTH);

        fallSpeed = Constants.SideTile.FALL_SPEED;
        backgroundYVelocity = fallSpeed;
        addTilesBelowScreen();

        float backgroundBottomY = -backgroundTileSize + Constants.SideTile.HEIGHT - Constants.Background.SPACE_BETWEEN_GRASS_AND_CENTER_BACKGROUND;
        spawnBackgroundTile(backgroundBottomY);

        skyYPos = Constants.SideTile.HEIGHT - Constants.Background.SPACE_BETWEEN_GRASS_AND_CENTER_BACKGROUND;;

        spawnedExit = false;
        stopped = false;
    }

    private void addTilesBelowScreen()
    {
        for(int i = 0; i < 2; i++)
        {
            SideTile.Side side = LEFT;
            if(i == 1) side = RIGHT;

            SideTile.Type type;
            if (side.coalTilesInRow > 0 && side.coalTilesInRow < Constants.SideTile.MIN_COAL_TILES_IN_ROW)
                type = SideTile.Type.Coal;
            else
            {
                if(Utils.randomFloat(0, 1) < Constants.SideTile.CHANCE_FOR_COAL)
                    type = SideTile.Type.Coal;
                else type = SideTile.Type.Stone;
            }
            //type = SideTile.Type.Stone;


            SideTile tile = spawnTileBelowScreen(side, type);

            if(side == LEFT) leftTiles.add(tile);
            else
            {
                if(!spawnedExit || tilesToOmit == 0) rightTiles.add(tile);
                else tilesToOmit--;
            }

            if(type == SideTile.Type.Coal) side.coalTilesInRow++;
            else side.coalTilesInRow = 0;
        }
    }

    public void spawnExit()
    {
        exitPos = rightTiles.peek().getBounds().y - Constants.SideTile.HEIGHT * Constants.Background.EXIT_HEIGHT;
        spawnedExit = true;
    }

    private SideTile spawnTileBelowScreen(SideTile.Side side, SideTile.Type type)
    {
        float centerX;
        if(side == LEFT) centerX = Constants.SideTile.WIDTH/2f;
        else centerX = viewport.getWorldWidth() - (Constants.SideTile.WIDTH/2f);

        float centerY = - Constants.SideTile.HEIGHT/2f;

        if(side == RIGHT && spawnedExit && tilesToOmit == 0 && !tileBelowExitSpawned)
        {
            centerY = exitPos - Constants.SideTile.HEIGHT/2f;
            tileBelowExitSpawned = true;
        }
        else if((side == LEFT && leftTiles.size > 0) || (side == RIGHT && rightTiles.size > 0))
        {
            if (side == LEFT) centerY = leftTiles.peek().getBounds().y - Constants.SideTile.HEIGHT / 2f;
            else centerY = rightTiles.peek().getBounds().y - Constants.SideTile.HEIGHT / 2f;
        }

        SideTile tile = new SideTile(centerX,
                centerY, type, side);

        return tile;
    }

    private void spawnBackgroundTileBelowScreen()
    {
        float bottomY = -backgroundTileSize;
        if(backgroundPositions.size > 0)
        {
            bottomY = backgroundPositions.peek().y - backgroundTileSize;
        }
        spawnBackgroundTile(bottomY);
    }

    private void spawnBackgroundTile(float bottomY)
    {
        Vector2 position = new Vector2(Constants.SideTile.WIDTH/2f, bottomY);
        backgroundPositions.add(position);
    }

    public void update(float delta)
    {
        if(GameplayScreen.gameState != GameState.INTRO)
        {
            for(int i = 0; i < grassTilesYpos.length; i++)
                grassTilesYpos[i] += Constants.SideTile.FALL_SPEED * delta;

            skyYPos += Constants.SideTile.FALL_SPEED * delta;
        }

        if(stopped)
        {
            if(doorYpos > exitPos)
                doorYpos -= Constants.Background.DOOR_VELOCITY * delta;
            else doorYpos = exitPos;
            return;
        }

        if(spawnedExit)
        {
            exitPos += Constants.SideTile.FALL_SPEED * delta;
            if(exitPos >= Constants.Background.EXIT_POS)
            {
                doorYpos = exitPos + Constants.SideTile.HEIGHT * Constants.Background.EXIT_HEIGHT;
                stopped = true;
            }
        }

        for(SideTile tile : leftTiles)
        {
            tile.update(delta, fallSpeed);
            player.checkLeftTileCollision(tile);
        }

        for(SideTile tile : rightTiles)
        {
            tile.update(delta, fallSpeed);
            player.checkRightTileCollision(tile);
        }


        if(leftTiles.peek().getBounds().y >= Constants.SideTile.MIN_POSITION_OF_PREV_TILE_TO_SPAWN_NEW_ONE)
        {
            addTilesBelowScreen();
        }

        if(leftTiles.size > 1 && leftTiles.first().getBounds().y > viewport.getWorldHeight())
        {
            backgroundYVelocity = Constants.Background.FALL_SPEED;
            leftTiles.removeIndex(0);
            rightTiles.removeIndex(0);
        }


        for(int i = 0; i < backgroundPositions.size; i++)
        {
            backgroundPositions.get(i).y += backgroundYVelocity * delta;
        }

        if(backgroundPositions.peek().y >= 0) spawnBackgroundTileBelowScreen();

        if(backgroundPositions.size > 1 && backgroundPositions.first().y > viewport.getWorldHeight())
            backgroundPositions.removeIndex(0);

    }

    public boolean isDoorClosed()
    {
        return spawnedExit && stopped && doorYpos == exitPos;
    }

    public void render(SpriteBatch spriteBatch)
    {
        for(Vector2 backgroundPos : backgroundPositions)
            spriteBatch.draw(Assets.instance.tiles.centerBackground, backgroundPos.x, backgroundPos.y, backgroundTileSize, backgroundTileSize);


        if(spawnedExit)
        {
            spriteBatch.draw(Assets.instance.tiles.exit, viewport.getWorldWidth() - Constants.SideTile.WIDTH,
                    exitPos, Constants.SideTile.WIDTH, Constants.SideTile.HEIGHT * Constants.Background.EXIT_HEIGHT);
        }
        if(stopped)
        {
            spriteBatch.draw(Assets.instance.tiles.exitGate, viewport.getWorldWidth() - Constants.SideTile.WIDTH,
                    doorYpos, Constants.Background.DOOR_WIDTH, Constants.Background.DOOR_HEIGHT);
        }

        for(SideTile tile : leftTiles)
            tile.render(spriteBatch);

        for(SideTile tile : rightTiles)
            tile.render(spriteBatch);

        spriteBatch.draw(Assets.instance.tiles.sky, 0, skyYPos, viewport.getWorldWidth(),
                viewport.getWorldHeight() - (Constants.Background.SPACE_BETWEEN_GRASS_AND_CENTER_BACKGROUND - Constants.SideTile.HEIGHT));

        spriteBatch.draw(Assets.instance.tiles.dirt, 0, grassTilesYpos[0], Constants.SideTile.WIDTH, Constants.SideTile.HEIGHT);
        spriteBatch.draw(Assets.instance.tiles.dirt, viewport.getWorldWidth() - Constants.SideTile.WIDTH, grassTilesYpos[1], Constants.SideTile.WIDTH, Constants.SideTile.HEIGHT);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setColor(Color.GREEN);

        for(SideTile tile : leftTiles)
            tile.render(shapeRenderer);

        for(SideTile tile : rightTiles)
            tile.render(shapeRenderer);
    }
}
