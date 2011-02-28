package biomatec.javaBeans;

import java.io.Serializable;

public class Int implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int i;
	public Int() {
		i=0;
	}
	
	public Int(int i) {
		this.i = i;
	}
	
	public int getInt() {
		return i;
	}
	
	public void setInt(int i) {
		this.i = i;
	}
}
