package biomatec.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

public class GeneSuggestOracle extends SuggestOracle {

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		SearchSuggestionService.Util.getInstance().getSuggestions(request, new GeneSuggestCallback(request, callback));
	}

	class GeneSuggestCallback implements AsyncCallback<SuggestOracle.Response> {

		private SuggestOracle.Request request;
		private SuggestOracle.Callback callback;

		public GeneSuggestCallback(
				SuggestOracle.Request request,
				SuggestOracle.Callback callback) {
			this.request = request;
			this.callback = callback;
		}

		public void onFailure(Throwable error) {
			callback.onSuggestionsReady(request, new SuggestOracle.Response());
		}

		public void onSuccess(SuggestOracle.Response response) {
			callback.onSuggestionsReady(request, response);
		}
	}

}
