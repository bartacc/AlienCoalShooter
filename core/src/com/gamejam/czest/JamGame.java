package com.gamejam.czest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gamejam.czest.screens.GameplayScreen;
import com.gamejam.czest.screens.WinScreen;
import com.gamejam.czest.screens.LoseScreen;

public class JamGame extends Game
{
	GameplayScreen gameplayScreen;
	WinScreen winScreen;
	LoseScreen loseScreen;

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

		gameplayScreen = new GameplayScreen(this, spriteBatch, shapeRenderer);
		winScreen = new WinScreen(spriteBatch, this);
		loseScreen = new LoseScreen(spriteBatch, this);

		initGameplayScreen();
		//initWinScreen();
		//initLoseScreen();
	}

	public void initGameplayScreen()
	{
		setScreen(gameplayScreen);
	}

	public void initWinScreen()
	{
		//WinScreen winScreen = new WinScreen(spriteBatch, new ExtendViewport(Constants.World.VIEWPORT_SIZE, Constants.World.VIEWPORT_SIZE), this);
		setScreen(winScreen);
	}

	public void initLoseScreen()
	{
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
