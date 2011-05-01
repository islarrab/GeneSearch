package biomatec.javaBeans;

import java.io.Serializable;

public class Gene implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*Attributes*/
	private int unifeature_key;
	private int feature_type_key;
	private String symbol;
	private String all_known_ids;
	private String organism;
	private String annotation;
    private boolean available;
   
   /*Constructors*/
	public Gene(){
		unifeature_key = 0;
		feature_type_key = 0;
		symbol = "";
		all_known_ids = "";
		organism = "";
		annotation = "";
		available = false;
	}
   
   /*Gets*/
	public int getUnifeatureKey (){
		return unifeature_key;
	}
	
	public int getFeatureTypeKey (){
		return feature_type_key;
	}
	
	public String getSymbol (){
		return symbol;
	}
	
	public String getAllKnownIds (){
		return all_known_ids;
	}
	
   public String getOrganism (){
		return organism;
	}
	
	public String getAnnotation (){
		return annotation;
	}

    public boolean getAvailable () {
        return this.available;
    }
	
   /*Sets*/
	public void setUnifeatureKey (int unifeature_key){
		this.unifeature_key = unifeature_key;
	}
	
	public void setFeatureTypeKey (int feature_type_key){
		this.feature_type_key = feature_type_key;
	}
	
	public void setSymbol (String symbol){
		this.symbol = symbol;
	}
	
	public void setAllKnownIds (String all_known_ids){
		this.all_known_ids = "";
		while (all_known_ids.length() > 30){
			this.all_known_ids += all_known_ids.substring(0,30) + "\n";
			all_known_ids = all_known_ids.substring(30);
		}
		this.all_known_ids += all_known_ids;
	}
	
   public void setOrganism (String organism){
		this.organism = organism;
	}
	
	public void setAnnotation (String annotation){
		this.annotation = annotation;
	}

    public void setAvailable (boolean available){
        this.available = available;
    }
      
}
