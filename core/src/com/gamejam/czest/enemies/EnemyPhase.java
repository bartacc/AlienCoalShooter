package com.gamejam.czest.enemies;

import com.gamejam.czest.entities.SideTile;

/**
 * Created by bartek on 08.04.17.
 */
public enum EnemyPhase
{
    EMPTY(),

    INTRO(new EnemyDef().initIntroEnemy()),

    L1(new EnemyDef().initIdleShooting(0.5f, 0.8f)),

    L2(new EnemyDef().initIdleShooting(0.3f, 0.7f),
            new EnemyDef().initIdleShooting(0.7f, 0.7f)),

    L3(new EnemyDef().initIdleShooting(0.15f, 0.6f),
            new EnemyDef().initIdleShooting(0.85f, 0.6f),
            new EnemyDef().initIdleShooting(0.5f, 0.8f)),

    L4(new EnemyDef().initFalling(0.5f, 0.7f, 0.25f)),

    L5(new EnemyDef().initFalling(0.2f, 0.85f, 0.5f),
            new EnemyDef().initIdleShooting(0.5f, 0.65f),
            new EnemyDef().initFalling(0.8f, 0.85f, 0.5f)),

    L6(new EnemyDef().initFalling(0.15f, 0.6f, 1f),
            new EnemyDef().initFalling(0.5f, 0.75f, 0.5f),
            new EnemyDef().initFalling(0.85f, 0.6f, 1f)),

    L7(new EnemyDef().initMovingShooting(0.5f, 0.8f, 0.25f, 0.25f, SideTile.Side.LEFT)),

    L8(new EnemyDef().initMovingShooting(0.25f, 0.7f, 0.15f, 0.15f, SideTile.Side.LEFT),
            new EnemyDef().initMovingShooting(0.75f, 0.7f, 0.15f, 0.15f, SideTile.Side.RIGHT)),

    L9(new EnemyDef().initMovingShooting(0.1f, 0.7f, 0.01f, 0.8f, SideTile.Side.RIGHT),
            new EnemyDef().initIdleShooting(0.75f, 0.7f)),

    L10(new EnemyDef().initFalling(0.1f, 0.55f, 0.5f),
            new EnemyDef().initFalling(0.9f, 0.55f, 0.5f),
            new EnemyDef().initFalling(0.5f, 0.75f, 2f),
            new EnemyDef().initIdleShooting(0.15f, 0.8f),
            new EnemyDef().initIdleShooting(0.85f, 0.8f),
            new EnemyDef().initFalling(0.4f, 0.9f, 2f),
            new EnemyDef().initFalling(0.6f, 0.9f, 2f)),

    L11(new EnemyDef().initIdleShooting(0.5f, 0.65f),
            new EnemyDef().initMovingShooting(0.25f, 0.75f, 0.1f, 0.1f, SideTile.Side.RIGHT),
            new EnemyDef().initMovingShooting(0.75f, 0.75f, 0.1f, 0.1f, SideTile.Side.LEFT)),

    L12(new EnemyDef().initMovingShooting(0.85f, 0.85f, 0.7f, 0.05f, SideTile.Side.LEFT),
            new EnemyDef().initFalling(0.8f, 0.65f, 0.5f),
            new EnemyDef().initFalling(0.6f, 0.65f, 1f),
            new EnemyDef().initFalling(0.4f, 0.65f, 1.5f),
            new EnemyDef().initFalling(0.2f, 0.65f, 2f)),

    L13(new EnemyDef().initFalling(0.05f, 0.85f, 0.1f),
            new EnemyDef().initFalling(0.20f, 0.75f, 0.6f),
            new EnemyDef().initFalling(0.35f, 0.65f, 1.1f),
            new EnemyDef().initFalling(0.5f, 0.55f, 1.6f),
            new EnemyDef().initFalling(0.65f, 0.65f, 1.1f),
            new EnemyDef().initFalling(0.80f, 0.75f, 0.6f),
            new EnemyDef().initFalling(0.95f, 0.85f, 0.1f),
            new EnemyDef().initIdleShooting(0.5f, 0.92f)),

    OUTRO(new EnemyDef().initFalling(0.05f, 0.85f, 0.1f),
            new EnemyDef().initFalling(0.20f, 0.75f, 0.6f),
            new EnemyDef().initFalling(0.35f, 0.65f, 1.1f),
            new EnemyDef().initFalling(0.5f, 0.55f, 1.6f),
            new EnemyDef().initFalling(0.65f, 0.65f, 1.1f),
            new EnemyDef().initFalling(0.80f, 0.75f, 0.6f),
            new EnemyDef().initFalling(0.95f, 0.85f, 0.1f)),

    EPILOGUE();


    public final EnemyDef[] enemyDefs;

    EnemyPhase(EnemyDef ... enemyDefs)
    {
        this.enemyDefs = enemyDefs;
    }

    public EnemyPhase getNextPhase()
    {
        return EnemyPhase.values()[this.ordinal()+1];
    }
}
