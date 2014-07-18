package communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

public class WebService extends AsyncTask <String, Void, String>{

	public final static String JSON = "json";
	
	Context context;
	
	private String serviceName;
	private String methodName;
	private BroadcastReceiver receiver;
	
	
	
	public WebService(String serviceName, String methodName, Context appContext, BroadcastReceiver receiver){
		
		this.serviceName 	= serviceName;
		this.methodName		= methodName;
		this.receiver		= receiver;
		this.context 		= appContext;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		context.registerReceiver(receiver, new IntentFilter(serviceName + "_" + methodName));
		return new ServerService().useService(serviceName, methodName, params);

	}
	
	@Override
	protected void onPostExecute(String result) {
		
		super.onPostExecute(result);
		
		Intent intent = new Intent(serviceName + "_" + methodName);
		intent.setAction(serviceName + "_" + methodName);
		intent.putExtra(JSON, result);
		
		context.sendBroadcast(intent);
		
	}
	
}
