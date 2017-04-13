package com.gamejam.czest;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by bartek on 08.04.17.
 */
public class Utils
{
    public static float randomFloat(float minNumber, float maxNumber)
    {
        float rolledNumber = minNumber + (random.nextFloat() * (maxNumber - minNumber));
        //Gdx.app.log(TAG, "float  min: "+minNumber+" max: "+maxNumber+" rolled: "+rolledNumber);
        return rolledNumber;
    }

    /**
     * @param minNumber Minimum number (inclusive)
     * @param maxNumber Maximum number (inclusive)
     * @return Random number
     */
    public static int randomInt(int minNumber, int maxNumber)
    {
        int rolledNumber = random.nextInt((maxNumber - minNumber) + 1) + minNumber;
        //Gdx.app.log(TAG, "int  min: "+minNumber+" max: "+maxNumber+" rolled: "+rolledNumber);
        return rolledNumber;
    }
}
