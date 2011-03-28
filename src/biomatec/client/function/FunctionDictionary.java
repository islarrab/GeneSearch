package biomatec.client.function;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FunctionDictionary {
	
	private final int HEATMAP = 0;
	
	private Widget widget;
	
	public FunctionDictionary() {
		
	}
	
	public void generateList(ListBox lb) {
		lb.addItem("Heatmap", HEATMAP+"");
		lb.addItem("View2","1");
		lb.addItem("View3","2");
		lb.addItem("View4","3");
		lb.addItem("View5","4");
		
		
	}

	public Widget getView(int x, ArrayList<Gene> selectedGenes, Dataset dataset) {
		switch(x) {
		case HEATMAP:
			return new Heatmap(selectedGenes, dataset);
		}
		return null;
	}
}
