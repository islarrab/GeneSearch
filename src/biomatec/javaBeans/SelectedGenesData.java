package biomatec.javaBeans;

public class SelectedGenesData {
	
	private String[][] data;
	private String columnsType;
	
	/**
	 * @param data the data to set
	 */
	public void setData(String[][] data) {
		this.data = data;
	}
	/**
	 * @return the data
	 */
	public String[][] getData() {
		return data;
	}
	/**
	 * @param columnsType the columnsType to set
	 */
	public void setColumnsType(String columnsType) {
		this.columnsType = columnsType;
	}
	/**
	 * @return the columnsType
	 */
	public String getColumnsType() {
		return columnsType;
	}
	
	
}
