package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by bartek on 09.04.17.
 */
public class LoseScreen extends InputAdapter implements Screen
{
    private JamGame game;

    private SpriteBatch spriteBatch;
    private Viewport viewport;
    private com.badlogic.gdx.math.Rectangle restartButton;



    public LoseScreen(SpriteBatch spriteBatch, Viewport viewport, JamGame game)
    {
        this.spriteBatch = spriteBatch;
        this.viewport = viewport;

        restartButton = new com.badlogic.gdx.math.Rectangle();

        this.game = game;
    }

    @Override
    public void show()
    {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);

        float width = 4;
        float height = 2;
        float x = viewport.getWorldWidth()/2f - width/2f;
        float y = 0.5f;

        restartButton.set(x, y, width, height);
    }

    @Override
    public void render(float delta)
    {
        Color clearColor = Color.GRAY;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(Assets.instance.tiles.restart, restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        spriteBatch.draw(Assets.instance.tiles.endEnemy, restartButton.x - 1, restartButton.y + 2.5f, restartButton.width + 2, restartButton.width + 3);
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector2 unProjected = new Vector2(screenX, screenY);
        viewport.unproject(unProjected);

        if(restartButton.contains(unProjected.x, unProjected.y))
        {
            game.initGameplayScreen();
            return true;
        }

        return false;
    }
}
