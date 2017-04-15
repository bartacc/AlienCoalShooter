package com.gamejam.czest;

/**
 * Created by bartek on 12.04.17.
 */
public class EnemyMoving extends Enemy
{
    private float fullLeftXoffset;
    private float fullRightXoffset;

    private float positionYoffset;
    private float positionXoffset;

    private SideTile.Side movementXdirection;
    private SideY movementYdirection;

    private float movementVelocity;


    public EnemyMoving()
    {
        super();
    }

    public EnemyMoving(float centerX, float centerY, GameplayScreen screen,
                       float leftMaxOffset, float rightMaxOffset, SideTile.Side initialDirection)
    {
        super();
        init(centerX, centerY, screen, leftMaxOffset, rightMaxOffset, initialDirection);
    }

    public void initIntroEnemy(float centerX, float centerY, GameplayScreen screen,
                               float leftMaxOffset, float rightMaxOffset, SideTile.Side initialDirection)
    {
        super.init(centerX, centerY, screen, Type.INTRO_ENEMY);

        fullLeftXoffset = leftMaxOffset;
        fullRightXoffset = rightMaxOffset;

        positionXoffset = 0;
        positionYoffset = 0;

        movementXdirection = initialDirection;
        movementYdirection = SideY.UP;
        movementVelocity = Constants.INTRO.ENEMY_VELOCITY;
    }

    public void init(float centerX, float centerY, GameplayScreen screen,
                     float leftMaxOffset, float rightMaxOffset, SideTile.Side initialDirection)
    {
        super.init(centerX, centerY, screen, Type.MOVING_SHOOTING);

        fullLeftXoffset = leftMaxOffset;
        fullRightXoffset = rightMaxOffset;

        positionXoffset = 0;
        positionYoffset = 0;

        movementXdirection = initialDirection;
        movementYdirection = SideY.UP;
        movementVelocity = Constants.Enemy.SIDE_MOVEMENT_VELOCITY;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        if (!movedToPosition || type == Type.EXPLODING) return;

        if(movementXdirection == SideTile.Side.LEFT)
        {
            positionXoffset -= movementVelocity * delta;
            bounds.x -= movementVelocity * delta;
            if(positionXoffset < -fullLeftXoffset)
                movementXdirection = SideTile.Side.RIGHT;
        }
        else if(movementXdirection == SideTile.Side.RIGHT)
        {
            positionXoffset += movementVelocity * delta;
            bounds.x += movementVelocity * delta;
            if(positionXoffset > fullRightXoffset)
                movementXdirection = SideTile.Side.LEFT;
        }


        if(movementYdirection == SideY.UP)
        {
            positionYoffset += Constants.Enemy.UP_MOVEMENT_VELOCITY * delta;
            bounds.y += Constants.Enemy.UP_MOVEMENT_VELOCITY * delta;
            if(positionYoffset > Constants.Enemy.UP_MOVEMENT_MAX_OFFSET)
                movementYdirection = SideY.DOWN;
        }
        else if(movementYdirection == SideY.DOWN)
        {
            positionYoffset -= Constants.Enemy.UP_MOVEMENT_VELOCITY * delta;
            bounds.y -= Constants.Enemy.UP_MOVEMENT_VELOCITY * delta;
            if(positionYoffset < -Constants.Enemy.UP_MOVEMENT_MAX_OFFSET)
                movementYdirection = SideY.UP;
        }
    }


    public enum SideY
    {
        UP, DOWN;
    }
}
