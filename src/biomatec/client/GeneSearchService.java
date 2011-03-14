package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Int;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("search")
public interface GeneSearchService extends RemoteService {
	ArrayList<Gene> geneSearch(String gene) throws IllegalArgumentException;
	ArrayList<Dataset> geneDetailSearch(ArrayList<Int> input) throws IllegalArgumentException;
	Int generateHeatMap(ArrayList<Int> genes, Dataset dataset) throws IOException;
}

