package biomatec.javaBeans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class SearchSuggestion implements IsSerializable, Suggestion {

    private String s;
    
    // Required for IsSerializable to work
    public SearchSuggestion() {
    	
    }

    // Convenience method for creation of a suggestion
    public SearchSuggestion(String s) {
       this.s = s;
    }

    public String getDisplayString() {
        return s;
    }

    public String getReplacementString() {
        return s;
    }
 }