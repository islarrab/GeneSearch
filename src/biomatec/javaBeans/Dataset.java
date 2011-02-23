package biomatec.javaBeans;

import java.io.Serializable;

public class Dataset implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*Attributes*/
	private int data_set_key;
	private String name;
	private String description;
   
   /*Constructors*/
	public Dataset(){
		name = "";
		description = "";
	}
   
   /*Gets*/
	public int getDatasetkey(){
		return data_set_key;
	}
	
	public String getName (){
		return name;
	}
	
	public String getDescription (){
		return description;
	}
	
   /*Sets*/
	public void setDatasetkey (int newValue){
		data_set_key = newValue;
	}
	
	public void setName (String newValue){
		this.name = newValue;
	}
	
	public void setDescription (String newValue){
		this.description = newValue;
	}      
}