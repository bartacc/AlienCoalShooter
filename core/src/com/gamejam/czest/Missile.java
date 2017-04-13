package com.gamejam.czest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by bartek on 08.04.17.
 */
public class Missile
{
    private Rectangle bounds;
    private Vector2 velocity;

    private Type type;

    public Missile(float centerX, float centerY, float speedY, Type type)
    {
        bounds = new Rectangle();
        velocity = new Vector2();

        init(centerX, centerY, speedY, type);
    }

    public void init(float centerX, float centerY, float speedY, Type type)
    {
        float width = type.getWidth();
        float height = type.getHeight();
        bounds.set(centerX - width/2f, centerY - height/2f, width, height);

        velocity.set(0, speedY);
        this.type = type;
    }

    public void update(float delta)
    {
        bounds.y += velocity.y * delta;

        if(type == Type.COAL_MISSILE) velocity.y -= Constants.Missile.COAL_MISSILE_DEACCELERATION * delta;
    }

    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(type.getRegion(), bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(type.getColor());
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {return bounds;}
    public Type getType() {return type;}


    public enum Type
    {
        COAL_MISSILE, ENEMY_MISSILE;

        public TextureRegion getRegion()
        {
            switch (this)
            {
                case COAL_MISSILE: return Assets.instance.missiles.coal;
                case ENEMY_MISSILE: return Assets.instance.missiles.laser;
                default: return Assets.instance.missiles.coal;
            }
        }

        public Color getColor()
        {
            switch (this)
            {
                case COAL_MISSILE: return Color.BLACK;
                case ENEMY_MISSILE: return Color.PINK;
            }
            return Color.WHITE;
        }

        public float getWidth()
        {
            switch (this)
            {
                case COAL_MISSILE: return Constants.Missile.COAL_WIDTH;
                case ENEMY_MISSILE: return Constants.Missile.ENEMY_MISSILE_WIDTH;
                default: return Constants.Missile.COAL_WIDTH;
            }
        }

        public float getHeight()
        {
            switch (this)
            {
                case COAL_MISSILE: return Constants.Missile.COAL_HEIGHT;
                case ENEMY_MISSILE: return Constants.Missile.ENEMY_MISSILE_HEIGHT;
                default: return Constants.Missile.COAL_HEIGHT;
            }
        }
    }
}
