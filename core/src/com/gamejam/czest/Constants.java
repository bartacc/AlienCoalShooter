package com.gamejam.czest;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by bartek on 08.04.17.
 */
public class Constants
{
    public static class World
    {
        public static final String TEXTURE_ATLAS_PATH = "assets/textures.pack.atlas";
        public static final String SOUNDS_PATH = "sounds/";
        public static final String FONTS_PATH = "rawTextures/";

        public static final Color CLEAR_COLOR = Color.GRAY;
        public static final float VIEWPORT_SIZE = 10;
    }

    public static class Background
    {
        public static final float FALL_SPEED = SideTile.FALL_SPEED/2f;
        public static final float GRASS_TILE_HEIGHT = 3f;

        public static final float SPACE_BETWEEN_GRASS_AND_CENTER_BACKGROUND = 0.5f;

        public static final float EXIT_POS = 1;
        public static final int EXIT_HEIGHT = 4;

        public static final float DOOR_VELOCITY = 6;
        public static final float DOOR_WIDTH = 0.5f;
        public static final float DOOR_HEIGHT = SideTile.HEIGHT * EXIT_HEIGHT;
    }

    public static class INTRO
    {
        public static float PLAYER_ENEMY_DISTANCE = 2;
        public static float ENEMY_VELOCITY = 5.5f;

        public static float PLAYER_MIN_WORLD_PERCENTAGE_POS_TO_SHOOT = 0.6f;

        public static float COAL_SPEED_X = 4f;
        public static float COAL_SPEED_Y = 3f;

        public static float INITIAL_DIRT_POS_DST_FROM_0 = 10;
    }

    public static class UI
    {
        //Percentages
        public static float UI_WIDTH_ARENA_PERCENTAGE = 0.4f;
        public static float UI_PADDING_FROM_CENTER = 0.06f;

        //Values in world coordinates
        public static float UI_PADDING_FROM_BOTTOM = 0.25f;


        public static float COAL_ICONS_PER_ROW = 5;
        public static float COAL_ICON_PADDING_SIZE_RATIO = 0.2f;

        public static float HEART_ICONS_PER_ROW = 5;
    }

    public static class Player
    {
        public static final int INITIAL_LIVES = 5;
        public static final int INITIAL_AMMO = 5;

        public static final int MAX_AMMO = 5;
        public static final int MAX_LIVES = 5;

        public static final float MOVE_VELOCITY = 8f;
        public static final float RECOIL_VELOCITY_DAMAGE = 15f;
        public static final float RECOIL_VELOCITY_FROM_COAL = 5f;
        public static final float RECOIL_DEACCELERATION = 30f;

        public static final float SHOT_SPEED = 10f;


        public static final float INITIAL_Y_POS = 2;
        public static final float WIDTH = 2.33f;
        public static final float HEIGHT = 2f;
        public static final float WALL_HITBOX_HEIGHT = HEIGHT/2f;
        public static final float ENEMY_DAMAGE_HITBOX_WIDTH = WIDTH/2f;
    }

    public static class Enemy
    {
        public static final float WIDTH = 2f;
        public static final float HEIGHT = 1.5f;


        public static final float FALLING_ENEMY_IDLE_SIZE = 1f;
        public static final float FALLING_ENEMY_HEIGHT = FALLING_ENEMY_IDLE_SIZE * 3f;

        public static final float ENTERING_ARENA_SPAWN_OFFSET = 10;
        public static final float ENTERING_ARENA_VELOCITY = 7;

        public static final float MIN_SHOT_SPEED = 5f;
        public static final float MAX_SHOT_SPEED = 5f;

        public static final float MIN_TIME_BETWEEN_SHOTS = 1f;
        public static final float MAX_TIME_BETWEEN_SHOTS = 2f;

        public static final float FALLING_INITIAL_VELOCITY = -5f;
        public static final float FALLING_ACCELERATION = -3f;

        public static final float SHAKING_OFFSET = 0.07f;
        public static final float SHAKE_VELOCITY = 3f;
        public static final float SHAKE_DURATION = 0.5f;

        public static final float SIDE_MOVEMENT_VELOCITY = 1f;
        public static final float UP_MOVEMENT_VELOCITY = 0.5f;
        public static final float UP_MOVEMENT_MAX_OFFSET = 0.15f;
    }

    public static class SideTile
    {
        public static final int MIN_COAL_TILES_IN_ROW = 3;

        public static final float CHANCE_FOR_COAL = 0.25f;
        public static final int MIN_AMMO_ADDED_AFTER_COLLISION = 1;
        public static final int MAX_AMMO_ADDED_AFTER_COLLISION = 1;

        public static final float WIDTH = 3;
        public static final float HEIGHT = 1.5f;

        public static final float FALL_SPEED = 6f;
        public static final float MIN_POSITION_OF_PREV_TILE_TO_SPAWN_NEW_ONE = 0;//-COAL_HEIGHT/2f;
    }

    public static class Missile
    {
        public static final float COAL_WIDTH = 0.5f;
        public static final float COAL_HEIGHT = 0.5f;

        public static final float ENEMY_MISSILE_WIDTH = 0.15f;
        public static final float ENEMY_MISSILE_HEIGHT = 0.6f;

        public static final float COAL_MISSILE_DEACCELERATION = 5.5f;
    }

    public static class Sound
    {
        public static final float ENEMY_SHOT_VOLUME = 0.4f;
        public static final float MINING_VOLUME = 0.4f;
    }
}
