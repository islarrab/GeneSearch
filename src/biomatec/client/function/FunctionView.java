package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.user.client.ui.Composite;

public class FunctionView extends Composite {
	
	private Function function;
	private ArrayList<Gene> selectedGenes;
	private Dataset dataset;
	private ArrayList<String[]> parameters;
	private String parsedUrl;
	
	public FunctionView(Function f, ArrayList<Gene> sg, Dataset ds) {
		this.function = f;
		this.selectedGenes = sg;
		this.dataset = ds;
		this.parameters = null;
		parseURL();
	}
	
	private void parseURL() {
		String newUrl = function.getUrl();
		
		//Parse for dataset key
		newUrl.replaceAll("<dsk>", dataset.getDatasetKey()+"");
		
		//Parse for unifeature keys
		String ufk = "";
		if (function.getFunctionType() == 'M') {
			ufk = selectedGenes.get(0).getUnifeatureKey()+"";
			for (int i=1; i<selectedGenes.size(); i++) {
				ufk += "+" + selectedGenes.get(i).getUnifeatureKey();
			}
			newUrl.replaceAll("<ufk>", ufk);
		}
		
		this.parsedUrl = newUrl;
	}
}
