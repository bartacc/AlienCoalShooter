package com.gamejam.czest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by bartek on 08.04.17
 */

public class Assets implements Disposable, AssetErrorListener
{
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public Missiles missiles;
    public Tiles tiles;
    public Enemies enemies;
    public Player player;
    public Sounds sounds;

    public void queueLoading(AssetManager assetManager)
    {
        //if(this.assetManager != null)
        //    this.assetManager.dispose();

        this.assetManager = assetManager;
        assetManager.setErrorListener(this);

        assetManager.load(Constants.World.TEXTURE_ATLAS_PATH, TextureAtlas.class);
        queueSounds(assetManager);

        FileHandleResolver resolver = new InternalFileHandleResolver();
    }

    private void queueSounds(AssetManager assetManager)
    {
        assetManager.load(Constants.World.SOUNDS_PATH + "background.mp3", Music.class);

        assetManager.load(Constants.World.SOUNDS_PATH + "coal-shot.wav", Sound.class);
        assetManager.load(Constants.World.SOUNDS_PATH + "enemy-shot.ogg", Sound.class);

        assetManager.load(Constants.World.SOUNDS_PATH + "mining1.ogg", Sound.class);
        assetManager.load(Constants.World.SOUNDS_PATH + "mining2.ogg", Sound.class);
        assetManager.load(Constants.World.SOUNDS_PATH + "mining3.ogg", Sound.class);

        assetManager.load(Constants.World.SOUNDS_PATH + "damage.wav", Sound.class);
        assetManager.load(Constants.World.SOUNDS_PATH + "enemyDestroyed.wav", Sound.class);
    }

    public void finishLoading()
    {
        assetManager.finishLoading();
        init();
    }

    public void init()
    {
        TextureAtlas atlas = assetManager.get(Constants.World.TEXTURE_ATLAS_PATH, TextureAtlas.class);
        missiles = new Missiles(atlas);
        tiles = new Tiles(atlas);
        enemies = new Enemies(atlas);
        player = new Player(atlas);
        sounds = new Sounds(assetManager);
    }

    public boolean update() {return assetManager.update();}
    public float getProgress() {return assetManager.getProgress();}

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);}

    @Override
    public void dispose()
    {
        if (assetManager.getProgress() < 1f) assetManager.finishLoading();
        assetManager.dispose();
    }

    public class Missiles
    {
        public final TextureRegion coal;
        public final TextureRegion laser;

        public Missiles(TextureAtlas textureAtlas)
        {
            coal = textureAtlas.findRegion("wegielPocisk");
            laser = textureAtlas.findRegion("kosmitaPocisk");
        }
    }

    public class Tiles
    {
        public final TextureRegion[] coal;
        public final TextureRegion[] stone;

        public final TextureRegion dirt;
        public final TextureRegion sky;

        public final TextureRegion tileBackground;
        public final TextureRegion centerBackground;

        public final TextureRegion heart;
        public final TextureRegion exit;
        public final TextureRegion exitGate;

        public final TextureRegion endPlayer;
        public final TextureRegion endText;

        public final TextureRegion endEnemy;
        public final TextureRegion endLoseText;
        public final TextureRegion restart;

        public Tiles(TextureAtlas textureAtlas)
        {
            coal = new TextureRegion[3];
            coal[0] = textureAtlas.findRegion("wegiel1");
            coal[1] = textureAtlas.findRegion("wegiel2");
            coal[2] = textureAtlas.findRegion("wegiel3");

            stone = new TextureRegion[3];
            stone[0] = textureAtlas.findRegion("kamien1");
            stone[1] = textureAtlas.findRegion("kamien2");
            stone[2] = textureAtlas.findRegion("kamien3");

            tileBackground = textureAtlas.findRegion("tlo");
            centerBackground = textureAtlas.findRegion("tloCenter");

            dirt = textureAtlas.findRegion("trawa");
            sky = textureAtlas.findRegion("niebo");

            heart = textureAtlas.findRegion("serce");
            exit = textureAtlas.findRegion("wyjscie");
            exitGate = textureAtlas.findRegion("drzwi");

            endPlayer = textureAtlas.findRegion("koniec");
            endEnemy = textureAtlas.findRegion("duzykosmita");
            restart = textureAtlas.findRegion("restart");

            endText = textureAtlas.findRegion("napis2");
            endLoseText = textureAtlas.findRegion("napis1");
        }
    }

    public class Enemies
    {
        public final Animation shootingEnemy;
        public final Animation explosion;

        public final TextureRegion fallingEnemyIdle;
        public final TextureRegion fallingEnemyFalling;

        public Enemies(TextureAtlas textureAtlas)
        {
            TextureRegion shootingEnemy1 = textureAtlas.findRegion("kosmita1");
            TextureRegion shootingEnemy2 = textureAtlas.findRegion("kosmita2");
            TextureRegion shootingEnemy3 = textureAtlas.findRegion("kosmita3");

            shootingEnemy = new Animation(0.1f, shootingEnemy1, shootingEnemy2, shootingEnemy3);
            shootingEnemy.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

            TextureRegion explosion1 = textureAtlas.findRegion("eksplozja1");
            TextureRegion explosion2 = textureAtlas.findRegion("eksplozja2");
            TextureRegion explosion3 = textureAtlas.findRegion("eksplozja3");
            explosion = new Animation(0.1f, explosion1, explosion2, explosion3);

            fallingEnemyIdle = textureAtlas.findRegion("kulka");
            fallingEnemyFalling = textureAtlas.findRegion("spadajacy");
        }
    }

    public class Player
    {
        public final TextureRegion idleFalling;

        public final Animation sideMovement;
        public final Animation idleThrow;
        public final Animation sideMovementThrow;
        public final Animation takenDamage;
        public final Animation mining;

        public Player(TextureAtlas textureAtlas)
        {
            idleFalling = textureAtlas.findRegion("spadanie1");
            TextureRegion transition = textureAtlas.findRegion("przejscie");
            TextureRegion sideMovement = textureAtlas.findRegion("ruch1");
            this.sideMovement = new Animation(0.05f, idleFalling, transition, sideMovement);

            TextureRegion idleThrow = textureAtlas.findRegion("spadanie-rzut");
            this.idleThrow = new Animation(0.2f, idleThrow, idleFalling);

            TextureRegion sideThrow = textureAtlas.findRegion("ruch-rzut");
            this.sideMovementThrow = new Animation(0.2f, sideThrow, sideMovement);

            TextureRegion damage1 = textureAtlas.findRegion("obrazenia1");
            TextureRegion damage2 = textureAtlas.findRegion("obrazenia2");
            this.takenDamage = new Animation(0.2f, damage1, damage2, damage1);
            this.takenDamage.setPlayMode(Animation.PlayMode.LOOP);

            TextureRegion mining1 = textureAtlas.findRegion("kopanie1");
            TextureRegion mining2 = textureAtlas.findRegion("kopanie2");
            this.mining = new Animation(0.1f, mining1, mining2, mining1);
            this.mining.setPlayMode(Animation.PlayMode.LOOP);
        }
    }

    public class Sounds
    {
        public final Music backgroundMusic;

        public final Sound coalThrow;
        public final Sound enemyThrow;

        public final Sound[] mining;

        public final Sound damage;
        public final Sound enemyDestroyed;

        public Sounds(AssetManager assetManager)
        {
            backgroundMusic = assetManager.get(Constants.World.SOUNDS_PATH + "background.mp3", Music.class);
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.4f);

            coalThrow = assetManager.get(Constants.World.SOUNDS_PATH + "coal-shot.wav", Sound.class);
            enemyThrow = assetManager.get(Constants.World.SOUNDS_PATH + "enemy-shot.ogg", Sound.class);

            mining = new Sound[3];
            mining[0] = assetManager.get(Constants.World.SOUNDS_PATH + "mining1.ogg", Sound.class);
            mining[1] = assetManager.get(Constants.World.SOUNDS_PATH + "mining2.ogg", Sound.class);
            mining[2] = assetManager.get(Constants.World.SOUNDS_PATH + "mining3.ogg", Sound.class);

            damage = assetManager.get(Constants.World.SOUNDS_PATH + "damage.wav", Sound.class);
            enemyDestroyed = assetManager.get(Constants.World.SOUNDS_PATH + "damage.wav", Sound.class);
        }
    }
}