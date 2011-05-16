package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.SelectedGenesData;

import com.google.gwt.user.client.ui.ListBox;

public class FunctionDictionary {
	
	private ArrayList<Function> functions = new ArrayList<Function>();

	public FunctionDictionary(ArrayList<Function> functions) {
		this.functions = functions;
	}
	
	/**
	 * used to dynamically generate the functions in a given list
	 * @param lb the list to be populated with functions
	 */
	public void generateList(ListBox lb) {
		for (int i=0; i < functions.size(); i++) {
			lb.addItem(functions.get(i).getName(), i+"");
		}
	}

	public FunctionView getView(int i, ArrayList<Gene> selectedGenes, Dataset dataset, SelectedGenesData selectedGenesData) {	
		return new FunctionView(functions.get(i),selectedGenes,dataset, selectedGenesData);
	}
	

}
