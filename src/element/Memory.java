package element;

import java.io.StringReader;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Memory {

	private static Memory memo = null;
	private User user;
	private Gson gson;
	
	private Memory(){
		
		user = null;
		gson = new Gson();
	}
	
	public static Memory getMemory(){
		
		if (memo == null){
			memo = new Memory();
		}
		
		return memo;
	}
	
	public void setUser(User user){
		
		this.user = user;
	}
	
	public void setUser(String jsonUser){
		
		JsonReader jsonreader = new JsonReader(new StringReader(jsonUser));
		jsonreader.setLenient(true);
		this.user = gson.fromJson(jsonreader, User.class);
	}
	
	public User getUser(){
		return this.user;
	}
	
}
