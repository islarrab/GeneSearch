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
	private String genes;
   
   /*Constructors*/
	public Dataset(){
		name = "";
		description = "";
		genes = "";
	}
   
   /*Gets*/
	public int getDatasetKey(){
		return data_set_key;
	}
	
	public String getName (){
		return name;
	}
	
	public String getDescription (){
		return description;
	}
	
	public String getGenes (){
		return genes;
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
	
	public void setGenes (String newValue){
		this.genes = newValue;
	}
	
	public void addGene (String gene){
		this.genes += gene + ", ";
	}
	
	public void trimGenes (){
		this.genes = this.genes.substring(0, this.genes.length() - 2);
	}
	
	public String toString(){
		return data_set_key + " " + name + " " + description + " " + genes;
	}
}