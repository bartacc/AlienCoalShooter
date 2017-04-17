package com.gamejam.czest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gamejam.czest.Assets;
import com.gamejam.czest.entities.Background;
import com.gamejam.czest.Constants;
import com.gamejam.czest.GameState;
import com.gamejam.czest.Intro;
import com.gamejam.czest.JamGame;
import com.gamejam.czest.entities.Missile;
import com.gamejam.czest.Outro;
import com.gamejam.czest.entities.Player;
import com.gamejam.czest.enemies.EnemyPhase;
import com.gamejam.czest.enemies.EnemySpawner;

/**
 * Created by bartek on 08.04.17.
 */
public class GameplayScreen implements Screen
{
    public static final String TAG = GameplayScreen.class.getSimpleName();

    public static GameState gameState;
    public final JamGame game;

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Intro intro;
    private Outro outro;

    private ExtendViewport viewport;
    private HUDoverlay hudOverlay;

    private com.gamejam.czest.enemies.EnemyPhase enemyPhase;

    private Background background;
    private Player player;
    private DelayedRemovalArray<Missile> missiles;
    private Array<com.gamejam.czest.enemies.Enemy> enemies;


    public GameplayScreen(JamGame game, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        this.spriteBatch = spriteBatch;
        this.shapeRenderer = shapeRenderer;

        this.game = game;

        intro = new Intro();
        outro = new Outro();

        hudOverlay = new HUDoverlay(this);

        missiles = new DelayedRemovalArray<Missile>();
        enemies = new Array<com.gamejam.czest.enemies.Enemy>();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(null);

        intro.init(spriteBatch, this);

        viewport = new ExtendViewport(Constants.World.VIEWPORT_SIZE, Constants.World.VIEWPORT_SIZE);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player = new Player(viewport.getWorldWidth() + Constants.Player.WIDTH/2f, Constants.Player.INITIAL_Y_POS, this);
        background = new Background(player, viewport);

        gameState = GameState.INTRO;
    }

    public void initIntroEnemy()
    {
        enemyPhase = com.gamejam.czest.enemies.EnemyPhase.INTRO;
        com.gamejam.czest.enemies.EnemySpawner.initEnemies(enemyPhase, this, enemies);
    }

    private void initGameplay()
    {
        Gdx.input.setInputProcessor(player);

        enemyPhase = com.gamejam.czest.enemies.EnemyPhase.L1;
        com.gamejam.czest.enemies.EnemySpawner.initEnemies(enemyPhase, this, enemies);

        Assets.instance.sounds.backgroundMusic.play();
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Color clearColor = Constants.World.CLEAR_COLOR;
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);


        spriteBatch.begin();

        background.render(spriteBatch);
        if(player != null) player.render(spriteBatch);
        for(com.gamejam.czest.enemies.Enemy enemy : enemies)
            enemy.render(spriteBatch);
        for(Missile missile : missiles)
            missile.render(spriteBatch);

        spriteBatch.end();


        shapeRenderer.begin();

        //tileBackground.render(shapeRenderer);
        //player.render(shapeRenderer);
        //for(Missile missile : missiles)
        //    missile.render(shapeRenderer);
        //for (Enemy enemy : enemies)
        //    enemy.render(shapeRenderer);

        shapeRenderer.end();

        if(gameState == GameState.GAMEPLAY)
            hudOverlay.render(spriteBatch, player.getCoalAmmo(), player.getLives());

        if(gameState == GameState.INTRO)
            intro.render();
    }

    public void update(float delta)
    {
        if(gameState == GameState.INTRO)
        {
            intro.update(delta);
            if(intro.isIntroFinished())
            {
                gameState = GameState.GAMEPLAY;
                initGameplay();
            }
        }
        else
        {
            checkForEndOfPhase();
        }

        if(gameState == GameState.OUTRO)
            outro.update(delta);

        player.update(delta);

        if(gameState != GameState.INTRO || intro.isBackgroundAllowedToMove())
        background.update(delta);

        for(Missile missile : missiles)
            missile.update(delta);

        for(com.gamejam.czest.enemies.Enemy enemy : enemies)
            enemy.update(delta);


        checkMissilesCollisions();
        checkForInvisibleMissiles();

        removeInvisibleEnemies();
        checkForOutOfBoundsEnemies();

        checkPlayerEnemyCollision();
    }

    private void checkPlayerEnemyCollision()
    {
        for(int i = 0; i < enemies.size; i++)
        {
            player.checkEnemyCollision(enemies.get(i));
        }
    }

    private void checkForEndOfPhase()
    {
        if(enemies.size == 0 && missiles.size == 0)
        {
            if(enemyPhase.getNextPhase() == null) return;

            setPhase(enemyPhase.getNextPhase());
        }
    }

    public void setPhase(com.gamejam.czest.enemies.EnemyPhase phase)
    {
        enemyPhase = phase;
        if(enemyPhase == com.gamejam.czest.enemies.EnemyPhase.OUTRO)
        {
            Gdx.app.log(TAG, "Game state is outro");
            gameState = GameState.OUTRO;
            outro.startOutro(this);
        }
        if(enemyPhase == EnemyPhase.EPILOGUE)
        {
            game.initWinScreen();
        }
        EnemySpawner.initEnemies(enemyPhase, this, enemies);
    }

    public void shootMissile(float centerX, float centerY, float speedX, float speedY, Missile.Type type)
    {
        Missile missile = new Missile(centerX, centerY, speedX, speedY, type);
        missiles.add(missile);

        if(type == Missile.Type.ENEMY_MISSILE) Assets.instance.sounds.enemyThrow.play(Constants.Sound.ENEMY_SHOT_VOLUME);
        else if(type == Missile.Type.COAL_MISSILE) Assets.instance.sounds.coalThrow.play();
    }

    private void checkForOutOfBoundsEnemies()
    {
        for(int i = 0; i < enemies.size; i++)
        {
            if(enemies.get(i).getBounds().y + enemies.get(i).getBounds().height < 0)
                enemies.removeIndex(i);
        }
    }

    private void removeInvisibleEnemies()
    {
        for(int i = 0; i < enemies.size; i++)
        {
            if(enemies.get(i).isReadyForRemoval())
                enemies.removeIndex(i);
        }
    }

    private void checkMissilesCollisions()
    {
        missiles.begin();
        for(int i = 0; i < missiles.size; i++)
        {
            Missile missile = missiles.get(i);

            if(missile.getType() == Missile.Type.ENEMY_MISSILE
                    && missile.getBounds().overlaps(player.getEnemyDamageHitbox()))
            {
                player.substractLife();
                missiles.removeIndex(i);
                Gdx.app.log(TAG, "Player hit, lives: " + player.getLives());
            }

            for(int j = 0; j < enemies.size; j++)
            {
                if(!enemies.get(j).isDestroyed()  &&
                        missile.getType() == Missile.Type.COAL_MISSILE &&
                        missile.getBounds().overlaps(enemies.get(j).getBounds()))
                {
                    enemies.get(j).destroyed();
                    missiles.removeIndex(i);
                }
            }
        }
        missiles.end();
    }

    private void checkForInvisibleMissiles()
    {
        missiles.begin();
        for(int i = 0; i < missiles.size; i++)
        {
            Rectangle missileBounds = missiles.get(i).getBounds();
            if(missileBounds.y > viewport.getWorldHeight() || missileBounds.y + missileBounds.height < 0)
                missiles.removeIndex(i);
        }
        missiles.end();
    }

    public JamGame getGame() {return game;}

    public Viewport getViewport() {return viewport;}
    public Background getBackground() {return background;}
    public Player getPlayer() {return player;}

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
