package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class FunctionDictionary {
	
	private final int HEATMAP = 0;
	private final int WIKIGENES = 1;
	
	public FunctionDictionary() {
		
	}
	
	public void generateList(ListBox lb) {
		lb.addItem("Heatmap", HEATMAP+"");
		lb.addItem("Wikigenes.com", WIKIGENES+"");
		lb.addItem("View3","2");
		lb.addItem("View4","3");
		lb.addItem("View5","4");
		
		
	}

	public Widget getView(int x, ArrayList<Gene> selectedGenes, Dataset dataset) {
		switch(x) {
		case HEATMAP:
			return new Heatmap(selectedGenes, dataset);
		case WIKIGENES:
			return new WikiGenes(selectedGenes);
		}
		return null;
	}
}
