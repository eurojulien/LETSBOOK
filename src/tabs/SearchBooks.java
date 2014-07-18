package tabs;

import java.io.StringReader;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mobile.letsbook.R;
import communication.ServerService;
import communication.WebService;

import element.Book;
import element.BooksList;

/**
 * Created by julien on 14-06-08.
 */
public class SearchBooks extends Fragment implements View.OnClickListener{
	
	private View rootView;
	private TextView txtKeyword;
	private Button btnSearch;
	private ListView listBooks;
	
	private ArrayAdapter<Book> adapter;
	private ArrayList<Book> books;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	rootView 	= inflater.inflate(R.layout.fragment_search, container, false);
    	super.onCreateView(inflater, null, savedInstanceState);
    	
    	books		= new ArrayList<Book>();
    	txtKeyword 	= (TextView)	rootView.findViewById(R.id.txtSearch);
    	btnSearch	= (Button)		rootView.findViewById(R.id.btnSearch);
    	btnSearch.setOnClickListener(this);
    	listBooks	= (ListView)	rootView.findViewById(R.id.listBookSearch);
    	adapter		= new ArrayAdapter<Book>(rootView.getContext(), android.R.layout.simple_list_item_1, books);
    	listBooks.setAdapter(adapter);
    	
        return rootView;
    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btnSearch:
			
			if (txtKeyword.getText().length() == 0){
				Toast.makeText(rootView.getContext(), "Aucun parametre specifié", 4).show();
			}
			
			else{
						
				new WebService(ServerService.SERVICE_BOOK, 
							   ServerService.METHOD_GET,
							   rootView.getContext(),
							   new Communicator()).
							   execute("keyword=" + txtKeyword.getText());
			}
			
			break;
			
		}
	}
	
	public class Communicator extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String intentMessage = intent.getStringExtra(WebService.JSON);
			
			if (intentMessage == ServerService.SERVER_ERROR){
				Toast.makeText(getActivity(), "Erreur lors de la recherche", 4).show();
			}
			
			else{
				JsonReader jsonreader = new JsonReader(new StringReader(intentMessage));
				jsonreader.setLenient(true);
				BooksList lbooks = new Gson().fromJson(jsonreader, BooksList.class);
				
				books.clear();
				adapter.clear();
				adapter.notifyDataSetChanged();
				
				if (lbooks.getBooks().size() == 0){
					Toast.makeText(context, "Aucun livre ne correspond a vos critère de recherche", 4).show();
				}
				
				for (Book book : lbooks.getBooks())
					adapter.add(book);
			}
			
			// Cache le keyboard
			InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
			
		}
		
	}
}