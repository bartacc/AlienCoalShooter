package com.gamejam.czest;

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

    private ExtendViewport viewport;
    private HUDoverlay hudOverlay;

    private EnemyPhase enemyPhase;

    private Background background;
    private Player player;
    private DelayedRemovalArray<Missile> missiles;
    private Array<Enemy> enemies;


    public GameplayScreen(JamGame game, SpriteBatch spriteBatch, ShapeRenderer shapeRenderer)
    {
        this.spriteBatch = spriteBatch;
        this.shapeRenderer = shapeRenderer;

        this.game = game;

        hudOverlay = new HUDoverlay(this);

        missiles = new DelayedRemovalArray<Missile>();
        enemies = new Array<Enemy>();
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(null);

        viewport = new ExtendViewport(Constants.World.VIEWPORT_SIZE, Constants.World.VIEWPORT_SIZE);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player = new Player(viewport.getWorldWidth()/2f, Constants.Player.INITIAL_Y_POS, this);
        Gdx.input.setInputProcessor(player);

        background = new Background(player, viewport);

        gameState = GameState.GAMEPLAY;

        enemyPhase = EnemyPhase.L1;
        EnemySpawner.initEnemies(enemyPhase, this, enemies);

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
        player.render(spriteBatch);
        for(Enemy enemy : enemies)
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

        hudOverlay.render(spriteBatch, player.getCoalAmmo(), player.getLives());
    }

    public void update(float delta)
    {
        if(gameState == GameState.INTRO) return;
        if(player.getBounds().x > viewport.getWorldWidth())
            game.initWinScreen();

        player.update(delta);

        if(gameState != GameState.OUTRO_STOPPED)
            background.update(delta);

        for(Missile missile : missiles)
            missile.update(delta);

        for(Enemy enemy : enemies)
            enemy.update(delta);


        checkMissilesCollisions();
        checkForInvisibleMissiles();

        removeInvisibleEnemies();
        checkForOutOfBoundsEnemies();

        checkPlayerEnemyCollision();

        checkForEndOfPhase();
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
        if(enemies.size == 0)
        {
            if(enemyPhase.getNextPhase() == null) return;

            enemyPhase = enemyPhase.getNextPhase();
            if(enemyPhase == EnemyPhase.OUTRO)
            {
                Gdx.app.log(TAG, "Game state is outro");
                gameState = GameState.OUTRO;
                background.startOutro();
            }
            EnemySpawner.initEnemies(enemyPhase, this, enemies);
        }
    }

    public void shootMissile(float centerX, float centerY, float speedY, Missile.Type type)
    {
        Missile missile = new Missile(centerX, centerY, speedY, type);
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
                if(missile.getType() == Missile.Type.COAL_MISSILE &&
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

    public Viewport getViewport() {return viewport;}

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
