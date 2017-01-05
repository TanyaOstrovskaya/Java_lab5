public class FQuaterCircle implements Figure
{
    private double circleR;

    public FQuaterCircle (double circleR)
    {
        this.circleR = circleR;
    }

    public boolean isInArea (Punctum punctum)
    {
        double punctX = punctum.getX();
        double punctY = punctum.getY();
        if (((punctX*punctX) + (punctY*punctY) < circleR*circleR) && (punctX >=0) && (punctY >= 0))
            return true;
        else
            return false;
    }
}
