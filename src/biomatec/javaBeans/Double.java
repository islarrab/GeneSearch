package biomatec.javaBeans;

import java.io.Serializable;

public class Double implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double n;
	public Double() {
		n=0;
	}
	
	public Double(double n) {
		this.n = n;
	}
	
	public double getDouble() {
		return n;
	}
	
	public void setDouble(double n) {
		this.n = n;
	}
}
