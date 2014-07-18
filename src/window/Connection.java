package window;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.mobile.letsbook.R;
import communication.ServerService;
import communication.WebService;

import element.Memory;

/**
 * Created by julien on 14-06-08.
 */
public class Connection extends DialogFragment implements
        ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES 		= 1;
    private static final int REQUEST_CODE_SIGN_IN 					= 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES 	= 2;
    
    private static final int REQUEST_CODE_RESOLVE_ERR 				= 9000;
    private static final String TAG									= "Message Connexion";

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    private SignInButton signInButton;
    private Button btnInscription;
    private Button btnSignOutButton;
    private Button btnConnection;
    private Button btnAnnuler;
    
    private View rootView;
    
    private TextView txtLogin;
    private TextView txtPassword;
    
    private Inscription dialogInscription;
    
    private Memory memo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	rootView 					= inflater.inflate(R.layout.fragment_connection, container, false);
    	super.onCreateView(inflater, null, savedInstanceState);
    	mConnectionProgressDialog	= new ProgressDialog(this.getActivity());
    	mConnectionProgressDialog.setMessage("Signing in ... ");
    	dialogInscription = new Inscription();
    	
    	mPlusClient	= new PlusClient.Builder(this.getActivity(), this, this)
    								.setScopes(Scopes.PLUS_LOGIN)
    								.build();
    	
    	signInButton 		= (SignInButton) rootView.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        btnSignOutButton	= (Button) rootView.findViewById(R.id.sign_out_button);
        btnSignOutButton.setOnClickListener(this);
        btnInscription		= (Button) rootView.findViewById(R.id.btnInscription);
        btnInscription.setOnClickListener(this);
        btnConnection		= (Button) rootView.findViewById(R.id.btnConnection);
        btnConnection.setOnClickListener(this);
        btnAnnuler			= (Button) rootView.findViewById(R.id.btnConnectionCancel);
        btnAnnuler.setOnClickListener(this);
        
        txtLogin			= (TextView) rootView.findViewById(R.id.txtLoginConnection);
        txtPassword			= (TextView) rootView.findViewById(R.id.txtPasswordConnection);
        
        memo				= Memory.getMemory();
        
        return rootView;
    }

    @Override
    public void onStart(){
    	super.onStart();
    	mPlusClient.connect();
    }
    
    @Override
    public void onStop(){
    	mPlusClient.disconnect();
    	super.onStop();
    	
    }
    

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    	
    	if (mConnectionProgressDialog.isShowing()){
    		
    		if(result.hasResolution()){
    			
    			try{
    				result.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLVE_ERR);
    			}
    			
    			catch(SendIntentException e){
    				mPlusClient.connect();
    			}
    		}
    	}
    	
        signInButton.setVisibility(View.VISIBLE);
        btnSignOutButton.setVisibility(View.INVISIBLE);
    	mConnectionResult = result;

    }
    
    @Override
	public void onActivityResult(int requestCode, int responseCode, Intent intent){
    
    	if(requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == 0){
    		mConnectionResult = null;
    		mPlusClient.connect();
    	}
    }
    
	@Override
	public void onConnected(Bundle connectionHist) {
		
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(rootView.getContext(), accountName + " is connected.", Toast.LENGTH_LONG).show();
        mConnectionProgressDialog.dismiss();
        signInButton.setVisibility(View.INVISIBLE);
        btnSignOutButton.setVisibility(View.VISIBLE);
	}

    @Override
    public void onDisconnected() {
        //Log.d(TAG, "disconnected");
    	signInButton.setVisibility(View.VISIBLE);
        btnSignOutButton.setVisibility(View.INVISIBLE);
    }
    
    @Override
    public void onClick(View view) {
    	
    	switch(view.getId()){
    	
    		case R.id.sign_in_button:
    			int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(rootView.getContext());
    			
    			if(available != ConnectionResult.SUCCESS){
    				Toast.makeText(rootView.getContext(), DIALOG_GET_GOOGLE_PLAY_SERVICES, 2);
    				return;
    			}
    			
    			if (!mPlusClient.isConnected()) {
    		        if (mConnectionResult == null) {
    		            mConnectionProgressDialog.show();
    		            mPlusClient.connect();
    		        } else {
    		            try {
    		                mConnectionResult.startResolutionForResult(this.getActivity(), REQUEST_CODE_SIGN_IN);
    		                //String accountName = mPlusClient.getAccountName();
    		                //Toast.makeText(rootView.getContext(), accountName + " is connected.", Toast.LENGTH_LONG).show();
    		               
    		            } catch (SendIntentException e) {
    		                // Nouvelle tentative de connexion
    		                mConnectionResult = null;
    		                mPlusClient.connect();
    		            }
    		        }
    		    }
    			
    			break;
    	
    		case R.id.sign_out_button:
    			
    			if(mPlusClient.isConnected()){
                    mPlusClient.clearDefaultAccount();
                    mPlusClient.disconnect();
                    
                    Toast.makeText(rootView.getContext(), "User is deconnected.", Toast.LENGTH_LONG).show();
    			}
    			
    			break;
    		
    		case R.id.btnConnection:
    			new WebService(ServerService.SERVICE_USER, 
    						   ServerService.METHOD_GET, 
    						   rootView.getContext(),
    						   new Communicator())
    						   .execute("email=" + txtLogin.getText(),
    								    "password=" + txtPassword.getText()
    								   );
    			
    			break;
    			
    		case R.id.btnInscription:
    			
    			dialogInscription.show(getFragmentManager(), "dialog_inscription");
    			this.dismiss();
    			break;
    			
    		case R.id.btnConnectionCancel:
    			this.dismiss();
    			break;
    			
    	}
    	
    	// Cache le keyboard
		InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(rootView.getContext().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    				
    }
    
    private Connection dialogConnection(){
    	return this;
    }
    public class Communicator extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String intentMessage = intent.getStringExtra(WebService.JSON);
			
			if(intentMessage.equals(ServerService.SERVER_ERROR)){
				Toast.makeText(context, "L'identification a echouee", Toast.LENGTH_SHORT).show();
				return;
			}
			
			memo.setUser(intentMessage);
			Toast.makeText(context, "Bienvenue : " + memo.getUser().getFirstName() + ", " + memo.getUser().getLastName(), Toast.LENGTH_LONG).show();
			
	    	// Cache le keyboard
			InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(rootView.getContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
			
			dialogConnection().dismiss();
		}
    	
    }
}