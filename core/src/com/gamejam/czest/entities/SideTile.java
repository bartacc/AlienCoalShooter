package com.gamejam.czest.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.gamejam.czest.Assets;
import com.gamejam.czest.Constants;
import com.gamejam.czest.Utils;

/**
 * Created by bartek on 08.04.17.
 */
public class SideTile
{
    private Type type;
    private Side side;

    private TextureRegion region;

    private float width, height;

    private Rectangle bounds;

    private boolean hitByPlayer;

    public SideTile(float centerX, float centerY, Type type, Side side)
    {
        bounds = new Rectangle();

        init(centerX, centerY, type, side);
    }

    public void init(float centerX, float centerY, Type type, Side side)
    {
        this.type = type;
        this.side = side;

        width = Constants.SideTile.WIDTH;
        height = Constants.SideTile.HEIGHT;

        region = type.rollRegion();

        bounds.set(centerX - width/2f, centerY - height/2f, width, height);

        hitByPlayer = false;
    }

    public void update(float delta, float fallSpeed)
    {
        bounds.y += fallSpeed * delta;
    }

    public void render(SpriteBatch spriteBatch)
    {
        float scaleX = 1;
        if(side == Side.RIGHT) scaleX = -1;
        TextureRegion backgroundRegion = Assets.instance.tiles.tileBackground;

        spriteBatch.draw(backgroundRegion, bounds.x, bounds.y, bounds.width/2f, bounds.height/2f,
                bounds.width, bounds.height, scaleX, 1, 0);
        spriteBatch.draw(region, bounds.x, bounds.y, bounds.width/2f, bounds.height/2f,
                bounds.width, bounds.height, scaleX, 1, 0);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {return bounds;}
    public Type getType() {return type;}
    public Side getSide() {return side;}

    public boolean wasHitByPlayer() {return hitByPlayer;}
    public void hitByPlayer() {hitByPlayer = true;}

    public enum Type
    {
        Coal, Stone;

        public TextureRegion rollRegion()
        {
            int regionNR;
            switch (this)
            {
                case Coal:
                    regionNR = Utils.randomInt(0, 2);
                    return Assets.instance.tiles.coal[regionNR];
                case Stone:
                    regionNR = Utils.randomInt(0, 2);
                    return Assets.instance.tiles.stone[regionNR];

            }
            return null;
        }
    }

    public enum Side
    {
        LEFT, RIGHT;

        float coalTilesInRow;
        public int getIntDirection()
        {
            switch (this)
            {
                case RIGHT: return 1;
                case LEFT: return -1;
            }
            return 0;
        }
    }
}
