package pennapps.scavangel;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Hunt extends Activity {
	
	String username;
	int id;
	DB db;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_hunt);
		Intent intent = getIntent();
		username = intent.getExtras().getString("username");
		
		String connectionstring="mongodb://pennapps:pennapps@ds027509.mongolab.com:27509/scav";
		MongoClientURI uri  = new MongoClientURI(connectionstring);
		boolean connected = true;
		try {
			MongoClient client = new MongoClient(uri);
			db = client.getDB("scav");
		} catch (UnknownHostException e) {
			Toast.makeText(
                this, 
                "Couldn't access database - Check your internet connection!", 
                Toast.LENGTH_LONG)
                .show();
			connected = false;
		}
		
		if(connected){
			boolean auth = db.authenticate("pennapps", "pennapps".toCharArray());
			System.out.println(auth);
			DBCollection coll = db.getCollection("huntDB");
			DBCollection admin = db.getCollection("huntAdmin");
			
			BasicDBObject query = new BasicDBObject("currentHuntID", 
									new BasicDBObject("$gt", -1));
			DBCursor cursor = coll.find(query);
			if(!cursor.hasNext()){
				BasicDBObject newID = new BasicDBObject ("currentHuntID", 1);
				coll.insert(newID);
				id = 1;
			}
			else{
				DBObject info = cursor.next();
				id = (Integer) info.get("currentHuntID") + 1;
				BasicDBObject newID = new BasicDBObject("currentHuntID", id);
				coll.update(info, newID);
			}
			BasicDBObject adminData = new BasicDBObject("huntID", 
					id).append("username", username);
			admin.insert(adminData);
			TextView idTextView = (TextView)findViewById(R.id.huntid);
			idTextView.setText(Integer.toString(id));
		}
	}
	
	public void onCreateButtonPress(View v){
		EditText location = (EditText)findViewById(R.id.editText2);
		EditText hint = (EditText)findViewById(R.id.editText3);
		String locationOut = location.getText().toString().toLowerCase().trim();
		String hintOut = hint.getText().toString();
		
		String connectionstring="mongodb://pennapps:pennapps@ds027509.mongolab.com:27509/scav";
		MongoClientURI uri  = new MongoClientURI(connectionstring);
		boolean connected = true;
		try {
			MongoClient client = new MongoClient(uri);
			db = client.getDB("scav");
		} catch (UnknownHostException e) {
			Toast.makeText(
                this, 
                "Couldn't access database - Check your internet connection!", 
                Toast.LENGTH_LONG)
                .show();
			connected = false;
		}
		
		if(connected){
			DBCollection coll = db.getCollection("huntDB");
			BasicDBObject locationObj = new BasicDBObject("location", locationOut);
			BasicDBObject huntDBObj = new BasicDBObject("location", 
					locationOut).append("huntID", 
							id).append("hint", hintOut);
			DBCursor cursor = coll.find(locationObj);
			if(cursor.hasNext()){
				coll.update(locationObj, huntDBObj);
			}
			else{
				coll.insert(huntDBObj);
			}
			Toast.makeText(
	                this, 
	                "Location and hint pair added!", 
	                Toast.LENGTH_LONG)
	                .show();
			location.setText("");
			hint.setText("");
		}
		
	}
	public void onInitButtonPress(View v){
		EditText initHint = (EditText)findViewById(R.id.editText1);
		String initHintOut = initHint.getText().toString().toLowerCase().trim();
		String connectionstring="mongodb://pennapps:pennapps@ds027509.mongolab.com:27509/scav";
		MongoClientURI uri  = new MongoClientURI(connectionstring);
		boolean connected = true;
		try {
			MongoClient client = new MongoClient(uri);
			db = client.getDB("scav");
		} catch (UnknownHostException e) {
			Toast.makeText(
                this, 
                "Couldn't access database - Check your internet connection!", 
                Toast.LENGTH_LONG)
                .show();
			connected = false;
		}
		if(connected){
			DBCollection coll = db.getCollection("huntDB");
			BasicDBObject hintObj = new BasicDBObject("initHint", initHintOut);
			coll.save(hintObj.append("initHintID", id));
			Toast.makeText(
	                this, 
	                "Initial hint added!", 
	                Toast.LENGTH_LONG)
	                .show();
			initHint.setText("");
		}
	}
}
