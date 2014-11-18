package utils;

public class Triplet<X, Y, Z> {
	public X x;
	public Y y;
	public Z z;
	
	public Triplet(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return x.toString() + " " + y.toString() + " " + z.toString();
	}
}
