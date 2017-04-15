package com.gamejam.czest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class JamGame extends Game
{
	GameplayScreen gameplayScreen;

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	@Override
	public void create ()
	{
		Assets.instance.queueLoading(new AssetManager());
		Assets.instance.finishLoading();

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		//initGameplayScreen();
		initWinScreen();
		//initLoseScreen();
	}

	public void initGameplayScreen()
	{
		gameplayScreen = new GameplayScreen(this, spriteBatch, shapeRenderer);
		setScreen(gameplayScreen);
	}

	public void initWinScreen()
	{
		//WinScreen winScreen = new WinScreen(spriteBatch, new ExtendViewport(Constants.World.VIEWPORT_SIZE, Constants.World.VIEWPORT_SIZE), this);
		WinScreen winScreen = new WinScreen(spriteBatch, this);
		setScreen(winScreen);
	}

	public void initLoseScreen()
	{
		LoseScreen loseScreen = new LoseScreen(spriteBatch,
				new ExtendViewport(Constants.World.VIEWPORT_SIZE, Constants.World.VIEWPORT_SIZE), this);
		setScreen(loseScreen);
	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
	}

	@Override
	public void dispose ()
	{
		spriteBatch.dispose();
		shapeRenderer.dispose();
		Assets.instance.dispose();
	}
}
