package biomatec.javaBeans;

import java.io.Serializable;

public class Function implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Accepted values for <code>returnType</code>
	 * I = Image
	 * H = HTML
	 * Anything else will be treated as HTML
	 */
	
	private String name;
	private String url;
	private char returnType; 
	private char functionType;
	private char method;
	
	public Function() {
		setName("");
		setUrl("");
		setReturnType(' ');
		setFunctionType('S');
		setMethod('G');
	}
	
	public Function(String name, String url, char returnType, char functionType, char method) {
		this.setName(name);
		this.setUrl(url);
		this.setReturnType(returnType);
		this.setFunctionType(functionType);
		this.setMethod(method);
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
	 * @return the url
	 */
	public char getMethod() {
		return method;
	}

	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(char returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the returnType
	 */
	public char getReturnType() {
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
	
	/**
	 * @return the url
	 */
	public void setMethod(char method) {
		this.method = method;
	}
	
}
