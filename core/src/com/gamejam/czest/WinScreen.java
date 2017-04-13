package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by bartek on 09.04.17.
 */
public class WinScreen implements Screen
{
    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private Color clearColor = new Color(201f/255f, 203f/255f, 198f/255f, 1);

    public WinScreen(SpriteBatch spriteBatch, Viewport viewport)
    {
        this.spriteBatch = spriteBatch;
        this.viewport = viewport;
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        float width = 8;
        float x = viewport.getWorldWidth() * 0.15f;
        float y = 1;

        spriteBatch.begin();
        spriteBatch.draw(Assets.instance.tiles.endPlayer, x, y, width, width);

        width = 4;
        x = viewport.getWorldWidth() * 0.6f;
        y = 1;
        spriteBatch.draw(Assets.instance.tiles.endText, x, y, width, width);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
