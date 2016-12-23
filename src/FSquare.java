public class FSquare implements Figur
{
    private double sideLength;

    public FSquare(double sideLength)
    {
        this.sideLength = sideLength;
    }

    public boolean isInArea (Punctum punctum)
    {
        double punctX = punctum.getX();
        double punctY = punctum.getY();
        if ((punctX > - sideLength) && (punctY < sideLength) && (punctX <= 0) && (punctY>=0))
            return true;
        else
            return false;
    }
}
