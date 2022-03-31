import java.util.ArrayList;
import java.util.List;


public class LabProject {
	
	private static final double PI = Math.PI;
	
	public static void main(String[] args) {
		
		List<Point> training = new ArrayList<Point>();
		List<Point> validation = new ArrayList<Point>();
		
		for (int i = 0; i < 1000; i++) {
			double x = Math.random() * (PI + PI) -PI;
			double y = Math.random() * (PI + PI) -PI ;
			training.add(new Point(x,y));
			double x1 = Math.random() * (PI + PI) -PI;
			double y1 = Math.random() * (PI + PI) -PI ;
			validation.add(new Point(x1,y1)) ;
		}
		System.out.println(training);
		
		/*for (int i = 0; i < samples.size(); i++) {
			System.out.println();
		}*/
	}
	
	


}
