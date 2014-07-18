package window;

import java.io.StringReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mobile.letsbook.R;

import communication.ServerService;
import communication.WebService;
import element.Memory;
import element.User;

public class Inscription extends DialogFragment implements View.OnClickListener {

	private View rootView;
	private Button btnInscription;
	private Button btnAnnuler;
	private TextView txtFirstName;
	private TextView txtLastName;
	private TextView txtEmail;
	private TextView txtPassword;
	private DialogFragment frag;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	
	    rootView = inflater.inflate(R.layout.fragment_inscription, container, false);
	    super.onCreateView(inflater, null, savedInstanceState);
	    
	    // Cache le clavier Virtuel
	    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	    
	    // Controles
	    btnInscription	= (Button) rootView.findViewById(R.id.btnInscription);
	    btnInscription.setOnClickListener(this);
	    btnAnnuler		= (Button) rootView.findViewById(R.id.btnInscriptionCancel);
	    btnAnnuler.setOnClickListener(this);
	    
	    txtFirstName 	= (TextView) rootView.findViewById(R.id.txtFirstName);
	    txtLastName 	= (TextView) rootView.findViewById(R.id.txtLastName);
	    txtEmail 		= (TextView) rootView.findViewById(R.id.txtEmail);
	    txtPassword		= (TextView) rootView.findViewById(R.id.txtPassword);
	    frag			= this;
	    return rootView;
	}
	
	@Override
    public void onClick(View view) {
    	
    	switch(view.getId()){
    		
    		case R.id.btnInscription:
    			
        		if (txtFirstName.getText().length() > 0 &&
        			txtLastName.getText().length() > 0 &&
        			txtEmail.getText().length() > 0 &&
        			txtPassword.getText().length() > 0){
        			
        			new WebService(ServerService.SERVICE_USER, ServerService.METHOD_PUT, rootView.getContext(), new Communicator())
    				.execute("fname=" + txtFirstName.getText(),
    						 "lname=" + txtLastName.getText(),
    						 "email=" + txtEmail.getText(),
    						 "password=" + txtPassword.getText()
    						 );
        		}
        		
        		else{
        			Toast.makeText(rootView.getContext(), "Information(s) manquante(s)", Toast.LENGTH_SHORT).show();
        		}
    			
    			    			
    			break;
    			
    		case R.id.btnInscriptionCancel:
    			this.dismiss();
    			break;
    	}
    
	    // Cache le clavier Virtuel
	    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
    }
	
	public class Communicator extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			switch(intent.getAction()){
			
				case ServerService.SERVICE_USER + "_" + ServerService.METHOD_GET:
					
					Gson gson = new Gson();
					JsonReader jsonreader = new JsonReader(new StringReader(intent.getStringExtra(WebService.JSON)));
					jsonreader.setLenient(true);
					User user = gson.fromJson(jsonreader, User.class);
					
					txtFirstName.setText(user.getFirstName());
					txtLastName.setText(user.getLastName());
					txtEmail.setText(user.getEmail());
					
					break;
					
				case ServerService.SERVICE_USER + "_" + ServerService.METHOD_PUT:
					
					if (intent.getExtras().containsKey(WebService.JSON) && intent.getStringExtra(WebService.JSON) == ServerService.SERVER_ERROR){
						Toast.makeText(context, "L'usager existe deja", Toast.LENGTH_SHORT).show();
					}
					
					else{
						Toast.makeText(context, "Bienvenue " + txtFirstName.getText() + ", " + txtLastName.getText(), Toast.LENGTH_LONG).show();
						
						user = new User();
						user.setEmail(txtEmail.getText().toString());
						user.setFirstName(txtFirstName.getText().toString());
						user.setLastName(txtLastName.getText().toString());
						user.setPassword(txtPassword.getText().toString());
						Memory.getMemory().setUser(user);
						
						frag.dismiss();
					}
			}
			
		    // Cache le clavier Virtuel
		    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
	}
}
