package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class FunctionDictionary {
	
	private ArrayList<Function> functions = new ArrayList<Function>();
	
	public FunctionDictionary() {
		functions.add(new Function("Heatmap",
				"http://biomatec.mty.itesm.mx:8080/Biomatec1/getRowData.jsp?dsk=<dsk>&unifeaturekey=<ufk>&format=text&type=%%Type%%",
				's',
				'M'));
		
		functions.add(new Function("Wikigenes.com",
				"http://www.wikigenes.org/e/gene/e/<ufk>.html",
				'w',
				'S'));
		
	}
	
	/**
	 * used to dynamically generate the functions in a given list
	 * @param lb the list to be populated with functions
	 */
	public void generateList(ListBox lb) {
		for (int i=0; i<functions.size(); i++) {
			lb.addItem(functions.get(i).getName(), i+"");
		}
	}

	public Composite getView(int i, ArrayList<Gene> selectedGenes, Dataset dataset) {	
		return new FunctionView(functions.get(i),selectedGenes,dataset);
	}
}
