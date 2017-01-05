public class Punctum
{
    private double x;
    private double y;
    public boolean isInside;

    public Punctum (double x, double y) {
        this.x = x;
        this.y = y;
        this.isInside = false;
    }

    public double getX() {return x;};
    public double getY() {return y;};

}
