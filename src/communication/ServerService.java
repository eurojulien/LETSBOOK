package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

//import org.apache.http.NameValuePair;
// http://docs.oracle.com/javase/7/docs/api/java/net/URL.html#URL(java.lang.String)

public class ServerService {

	private final static String SERVICE_HOST 	= "http://letsweb.eurojulien.webfactional.com/letsbook/";
	private final static int BUFFER_SIZE		= 1000;
	private final static String PARAM			= "/?";
	private final static String AND				= "&";
	
	public final static String SERVICE_BOOK		= "book";
	public final static String SERVICE_USER		= "user";
	
	public final static String METHOD_GET		= "GET";
	public final static String METHOD_PUT		= "PUT";
	public final static String METHOD_DELETE	= "DELETE";
	
	public final static String SERVER_ERROR		= "SERVER 500";
	
	private String host;
	private InputStream inputStream;
	
	public ServerService (){
		host = SERVICE_HOST;
	}

	public String useService (String serviceName, String methodName, String... parameters){
		
		InputStreamReader reader = null;
		
		String fullRequest = host + serviceName;
		
		for (int index = 0; index < parameters.length; index++){
		
			if (index == 0){
				fullRequest += PARAM;
			}
			
			fullRequest += parameters[index];
			
			if ((index + 1) < parameters.length){
				fullRequest += AND;
			}
		}
		
		
		try {
			inputStream = sendRequest(new URL(fullRequest), methodName);
			
			if (inputStream != null && methodName == METHOD_GET){
			
				reader = new InputStreamReader(inputStream);
				int lengthRead = 0;
				
				char[] buffer = new char[BUFFER_SIZE];
				String response = new String();
				
				do{
					lengthRead = reader.read(buffer);
					response += new String (buffer).trim();
					
				}while(lengthRead > 0);

				return response.trim();
				
			}
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
			return SERVER_ERROR;
		}
		
		return null;
	}
	
	/*public List<User> serviceUser (String methodName, String... parameters){
		
		InputStreamReader reader = null;
		
		try {
			inputStream = sendRequest(new URL(host + SERVICE_USER + "?id=1"), methodName);
			
			if (inputStream != null){
			
				reader = new InputStreamReader(inputStream);
				
				User user = gson.fromJson(reader, new TypeToken<User>(){}.getType());
				ArrayList<User> users = new ArrayList<User>();
				users.add(user);
				
				return users;
				
			}
		
		// il s'agit d'une liste, pas d'un seul objet
		} catch (JsonSyntaxException e){
		
			return gson.fromJson(reader, new TypeToken<List<User>>(){}.getType());
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	private InputStream sendRequest(URL url, String methodName) throws Exception{
		
		try{
	           // Ouverture de la connexion
	           HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	           
	           // Type de connexion
	           urlConnection.setRequestMethod(methodName);
	           
	           // Connexion à l'URL
	           urlConnection.connect();
	           
	           return urlConnection.getInputStream();
			
		}
		
		catch(Exception e){
			
			throw new Exception("Serveur : 500");
		}
	}
	
	/* public static void main(String[] args) {

		Service service = new Service();
		Gson gson = new Gson();
		String response = service.useService(SERVICE_USER, METHOD_GET, "id=1");
		JsonReader jsonreader = new JsonReader(new StringReader(response));
		jsonreader.setLenient(true);
		
		
		System.out.println("GET : " + response); 
		System.out.println("GET JSON : " + ((User) gson.fromJson(jsonreader, User.class)).getEmail());
	} */

}
