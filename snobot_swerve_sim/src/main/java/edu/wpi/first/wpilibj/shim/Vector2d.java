package edu.wpi.first.wpilibj.shim;

import edu.wpi.first.math.geometry.Translation2d;

public class Vector2d extends Translation2d
{
    public Vector2d()
    {

    }

    public Vector2d(double x, double y)
    {
        super(x, y);
    }

    /**
     * Returns dot product of this vector with argument.
     *
     * @param vec Vector with which to perform dot product.
     * @return Dot product of this vector with argument.
     */
    public double dot(Vector2d vec) {
        return getX() * vec.getX() + getY() * vec.getY();
    }

    public Vector2d rotate(double degrees)
    {
        edu.wpi.first.wpilibj.drive.Vector2d vector = new edu.wpi.first.wpilibj.drive.Vector2d(getX(), getY());
        vector.rotate(degrees);
        return new Vector2d(vector.x, vector.y);
    }
}
