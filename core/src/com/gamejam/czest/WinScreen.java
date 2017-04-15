package com.gamejam.czest;

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

/**
 * Created by bartek on 09.04.17.
 */
public class WinScreen implements Screen
{
    private JamGame game;

    private Stage stage;
    private Skin skin;

    private Color clearColor = new Color(201f/255f, 203f/255f, 198f/255f, 1);

    public WinScreen(SpriteBatch spriteBatch, final JamGame game)
    {
        this.game = game;

        Viewport viewport = new FitViewport(800, 480);
        stage = new Stage(viewport, spriteBatch);
        skin = new Skin();

        setUpBackgroundTable();
        setUpUItable();

        Gdx.input.setInputProcessor(stage);
    }

    private void setUpUItable()
    {
        Table rootTable = new Table();
        rootTable.setRound(false);
        //rootTable.setDebug(true);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);


        Table restartCreditsTable = new Table();
        restartCreditsTable.setRound(false);
        //restartCreditsTable.setDebug(true);
        //restartCreditsTable.setFillParent(true);

        //restartCreditsTable.add().expandX();
        //restartCreditsTable.row();
        restartCreditsTable.add(setUpCreditsLabel()).expand();
        restartCreditsTable.row();
        restartCreditsTable.add(setUpRestartButton()).size(230f, 150f).center().bottom().expandX();

        rootTable.center();
        rootTable.add(setUpTitleLabel()).expandY().colspan(2).center();
        //rootTable.add().expand();
        rootTable.row();
        rootTable.add(setUpPlayerImage()).size(400f, 400f).center();
        rootTable.add(restartCreditsTable).expandX().fill();
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
            backgroundTable.add(tile).size(tileSize);
        }

        stage.addActor(backgroundTable);
    }

    private Label setUpTitleLabel()
    {
        BitmapFont font = Assets.instance.fonts.title;
        font.getData().setScale(1f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label titleLabel = new Label("You get to live one more day... Maybe.", labelStyle);

        return titleLabel;
    }

    private Label setUpCreditsLabel()
    {
        BitmapFont font = Assets.instance.fonts.credits;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label creditsLabel = new Label("Art and Design:\nOla Zawilińska\n\nProgramming and Design:\nBartek Szczeciński\n(@artecgam)", labelStyle);
        //creditsLabel.setWrap(true);
        creditsLabel.setAlignment(Align.center);
        return creditsLabel;
    }

    private Image setUpPlayerImage()
    {
        Image playerWin = new Image(Assets.instance.tiles.endPlayer);
        return playerWin;
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

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        /*
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
        stage.dispose();
        skin.dispose();
    }
}
