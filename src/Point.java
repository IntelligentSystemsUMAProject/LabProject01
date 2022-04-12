
public class Point {
	private double x;
	private double y;
	private double F;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		F = (Math.sin(x) * Math.cos(y));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getF() {
		return F;
	}

	public void setF(double f) {
		F = f;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")" + " -> " + F;
	}

}
