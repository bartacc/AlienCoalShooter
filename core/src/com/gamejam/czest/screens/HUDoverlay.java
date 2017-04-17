package com.gamejam.czest.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gamejam.czest.Assets;
import com.gamejam.czest.Constants;

/**
 * Created by bartek on 08.04.17.
 */
public class HUDoverlay
{
    private com.gamejam.czest.screens.GameplayScreen screen;
    private BitmapFont font;

    public HUDoverlay(com.gamejam.czest.screens.GameplayScreen screen)
    {
        font = new BitmapFont();
        font.getData().setScale(0.09f);
        init(screen);
    }

    public void init(com.gamejam.czest.screens.GameplayScreen screen)
    {
        this.screen = screen;
    }

    public void render(SpriteBatch spriteBatch, int coalAmmo, int lives)
    {
        float playableArenaWidth = screen.getViewport().getWorldWidth() - (Constants.SideTile.WIDTH * 2f);
        float UIareaWidth = playableArenaWidth * Constants.UI.UI_WIDTH_ARENA_PERCENTAGE;
        float startX = Constants.SideTile.WIDTH + playableArenaWidth * (0.5f + Constants.UI.UI_PADDING_FROM_CENTER);

        float iconSpace = UIareaWidth / Constants.UI.COAL_ICONS_PER_ROW;
        float iconSize = iconSpace - (iconSpace * Constants.UI.COAL_ICON_PADDING_SIZE_RATIO);


        spriteBatch.begin();

        float yPos = Constants.UI.UI_PADDING_FROM_BOTTOM;
        int rows = (int) Math.ceil(coalAmmo / Constants.UI.COAL_ICONS_PER_ROW);
        for(int i = 0; i < rows; i++)
        {
            float xPos = startX;
            float coalsInThisRow = Constants.UI.COAL_ICONS_PER_ROW;
            if(coalAmmo - (i * Constants.UI.COAL_ICONS_PER_ROW) < Constants.UI.COAL_ICONS_PER_ROW)
                coalsInThisRow = coalAmmo - (i * Constants.UI.COAL_ICONS_PER_ROW);
            for(int j = 0; j < coalsInThisRow; j++)
            {
                spriteBatch.draw(Assets.instance.missiles.coal, xPos, yPos, iconSize, iconSize);
                xPos += iconSpace;
            }
            yPos += iconSpace;
        }


        yPos = Constants.UI.UI_PADDING_FROM_BOTTOM;
        startX = Constants.SideTile.WIDTH + playableArenaWidth/2f - playableArenaWidth * (Constants.UI.UI_PADDING_FROM_CENTER + Constants.UI.UI_WIDTH_ARENA_PERCENTAGE);
        int heartRows = (int) Math.ceil(lives / Constants.UI.HEART_ICONS_PER_ROW);
        for(int i = 0; i < heartRows; i++)
        {
            float xPos = startX;
            float heartsInThisRow = Constants.UI.HEART_ICONS_PER_ROW;
            if(lives - (i * Constants.UI.HEART_ICONS_PER_ROW) < Constants.UI.HEART_ICONS_PER_ROW)
                heartsInThisRow = lives - (i * Constants.UI.HEART_ICONS_PER_ROW);
            for(int j = 0; j < heartsInThisRow; j++)
            {
                spriteBatch.draw(Assets.instance.tiles.heart, xPos, yPos, iconSize, iconSize);
                xPos += iconSpace;
            }
            yPos += iconSpace;
        }

        spriteBatch.end();
        //font.draw(spriteBatch, "Lives: " + screen.getPlayer().getLives(), 0.5f, 8f);
    }
}
