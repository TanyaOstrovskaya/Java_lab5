public class FTriangle implements Figur
{
    private double sideLength;

    public FTriangle (double sideLength)
    {
        this.sideLength = sideLength;
    }

    public boolean isInArea (Punctum punctum)
    {
        double punctX = punctum.getX();
        double punctY = punctum.getY();

        if ((punctY > punctX - sideLength) && (punctX > 0) && (punctY < 0))
            return true;
        else
            return false;
    }
}
