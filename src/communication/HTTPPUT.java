package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HTTPPUT extends AsyncTask<NameValuePair, Void, Boolean>{

	public static final String SERVICE_BOOK = "book/";
	public static final String SERVICE_USER = "user/";
	
	private Context context;
	private String host;
	
	public HTTPPUT(Context appContext, String host){
		
		context 	= appContext;
		this.host 	= host;
	}
	
	
	/**
	 * Recoit les parametres a envoyer au serveur
	 */
	@Override
	protected Boolean doInBackground(NameValuePair... parameters){
	
		// Connexion au serveur
		// TODO : BasicHttpParams is Deprecated -> Use org.apache.http.client.config instead
		HttpParams httpParams = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
		HttpConnectionParams.setSoTimeout(httpParams, 15000);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);
		
		String result = "";
		int statusCode = 500;
		
		HttpPut putRequest = new HttpPut(host);;
		HttpResponse putResponse;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		for (NameValuePair param : parameters){
			params.add(param);
		}
		
		try {
			
			putRequest.setEntity(new UrlEncodedFormEntity(params));
			putResponse = client.execute(putRequest);
			
			statusCode = putResponse.getStatusLine().getStatusCode();
			
			if (statusCode == 200){
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(putResponse.getEntity().getContent(), "UTF-8"));
				result = reader.readLine();
				return true;
			}
			
			else{
				return false;
			}
		}
		
		catch(Exception e){
			return false;
		}
	}
}

