package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Double;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("search")
public interface GeneSearchService extends RemoteService {
	ArrayList<Gene> geneSearch(String gene) throws IllegalArgumentException;
	ArrayList<Dataset> geneDetailSearch(ArrayList<Gene> input) throws IllegalArgumentException;
	ArrayList<ArrayList<Double>> generateHeatMap(ArrayList<Gene> genes, Dataset dataset) throws IOException;
	String columnsType(int datasetKey) throws IOException;
	ArrayList<Function> functions() throws IOException;
	String getData(String endpoint, String requestParameters) throws IOException;
}

