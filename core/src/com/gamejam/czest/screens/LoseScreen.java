package com.gamejam.czest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamejam.czest.Assets;
import com.gamejam.czest.Constants;
import com.gamejam.czest.JamGame;

/**
 * Created by bartek on 09.04.17.
 */
public class LoseScreen implements Screen
{
    private JamGame game;

    private Stage stage;
    private Skin skin;


    public LoseScreen(SpriteBatch spriteBatch, JamGame game)
    {
        Viewport viewport = new FitViewport(Constants.EndScreen.WIDTH, Constants.EndScreen.HEIGHT);
        stage = new Stage(viewport, spriteBatch);

        this.game = game;
    }

    @Override
    public void show()
    {
        stage.clear();
        skin = new Skin();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        setUpBackgroundTable();
        setUpUItable();
    }

    private void setUpUItable()
    {
        Table rootTable = new Table();
        rootTable.setRound(false);
        //rootTable.setDebug(true);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        rootTable.center();
        rootTable.add(setUpTitleLabel()).expandY().colspan(2).center();
        //rootTable.add().expand();
        rootTable.row();
        rootTable.add(setUpEndImage()).expand().size(Constants.EndScreen.END_ENEMY_WIDTH, Constants.EndScreen.END_ENEMY_HEIGHT);
        //rootTable.row();
        rootTable.add(setUpRestartButton()).expand().size(230, 150).center();
        //rootTable.add(setUpRestartButton()).size(4.6f, 3f).center().bottom().expandX();
    }

    private void setUpBackgroundTable()
    {
        Table backgroundTable = new Table();
        backgroundTable.setFillParent(true);

        float tileSize = stage.getViewport().getWorldHeight();
        float worldWidth = stage.getViewport().getWorldWidth();
        int nrOfBackgroundTiles = (int) Math.ceil(worldWidth / tileSize);

        for(int i = 0; i < nrOfBackgroundTiles; i++)
        {
            Image tile = new Image(Assets.instance.tiles.centerBackground);
            tile.setSize(tileSize, tileSize);
            tile.setOrigin(Align.center);
            tile.setRotation(90);
            backgroundTable.add(tile).size(tileSize);
        }

        stage.addActor(backgroundTable);
    }

    private Button setUpRestartButton()
    {
        skin.add("buttonImage", Assets.instance.tiles.restart, TextureRegion.class);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle(
                skin.newDrawable("buttonImage", Color.WHITE),
                skin.newDrawable("buttonImage", Color.GRAY),
                skin.newDrawable("buttonImage")
        );
        skin.add("default", buttonStyle);

        Button restartButton = new Button(skin);
        restartButton.align(Align.center);
        restartButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.initGameplayScreen();
            }
        });

        return restartButton;
    }

    private Image setUpEndImage()
    {
        Image image = new Image(Assets.instance.tiles.endEnemy);
        image.setSize(Constants.EndScreen.END_ENEMY_WIDTH, Constants.EndScreen.END_ENEMY_HEIGHT);
        image.setOrigin(Align.center);
        image.setRotation(90);
        return image;
    }

    private Label setUpTitleLabel()
    {
        BitmapFont font = Assets.instance.fonts.title;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("One more human killed by aliens... Whatever.", labelStyle);

        return titleLabel;
    }

    @Override
    public void render(float delta)
    {
        Color clearColor = Color.GRAY;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        /*
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        spriteBatch.draw(Assets.instance.tiles.restart, restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        spriteBatch.draw(Assets.instance.tiles.endEnemy, restartButton.x - 1, restartButton.y + 2.5f, restartButton.width + 2, restartButton.width + 3);
        spriteBatch.end();
        */
    }

    @Override
    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height, true);
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
