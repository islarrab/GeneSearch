package biomatec.client.function;

public class Function {

	private String name;
	private String url;
	private String returnType;
	private char functionType;
	
	public Function() {
		setName("");
		setUrl("");
		setReturnType("");
		setFunctionType('S');
	}
	
	public Function(String name, String url, String returnType, char functionType) {
		this.setName(name);
		this.setUrl(url);
		this.setReturnType(returnType);
		this.setFunctionType(functionType);
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param functionType the functionType to set
	 */
	public void setFunctionType(char functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the functionType
	 */
	public char getFunctionType() {
		return functionType;
	}
	
}
