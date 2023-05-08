import java.awt.Point;
import java.util.Comparator;

public class PointYComparator implements Comparator<Point> {

	@Override
	public int compare(Point p1, Point p2) {
		if(p1.y > p2.y)
			return -1;
		else if(p1.y < p2.y)
			return 1;
		else return 0;
	}
}
