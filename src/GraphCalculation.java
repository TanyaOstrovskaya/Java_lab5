import java.util.LinkedList;
import java.util.*;

public class GraphCalculation {

    private double R;                   //user R, default R = 4
    LinkedList<Figure> figures;

    public GraphCalculation (double R, LinkedList<Figure> figurs) {
        this.R = R;
        this.figures = figurs;
    }

    public double getR () { return this.R; }
    public void setR (double R) { this.R = R;}

    public boolean isInArea (double x, double y)   {
        for (Figure figure : figures)  {
            if (figure.isInArea(x, y))
                return true;
        }
        return false;
    }

    public boolean isInArea (Punctum punctum) {
        return isInArea(punctum.getX(), punctum.getY());
    }

}
