interface Figure
{
    public boolean isInArea (Punctum punctum);
    public default boolean isInArea (double x, double y)
    {
        return isInArea(new Punctum(x,y));
    }
}
