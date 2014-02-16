package pennapps.scavangel;

import java.net.UnknownHostException;
import java.util.List;

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

public class Edit_Selected_Hunt extends Activity {
	String username;
	int id;
	DB db;
	DBCollection coll;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_selected_hunt);
		Intent intent = getIntent();
		id = intent.getExtras().getInt("huntID");
		
		updateTextView();
	}
	
	private void updateTextView(){
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
			coll = db.getCollection("huntDB");
			
			TextView idDisplay = (TextView) findViewById(R.id.huntid);
			idDisplay.setText(Integer.toString(id));
			
			BasicDBObject query = new BasicDBObject("huntID", id);
			DBCursor cursor = coll.find(query);
			
			String huntInfoString = "Location : Hint";
			
			for(DBObject obj : cursor){
				huntInfoString = huntInfoString + "\n" + 
							obj.get("location") + " : " + obj.get("hint");
			}
			
			TextView huntInfoDisplay = (TextView) findViewById(R.id.huntInfo);
			huntInfoDisplay.setText(huntInfoString);
		}
	}
	
	public void onSubmitButtonClick(View v) {
		EditText oldLocation = (EditText) findViewById(R.id.editTextOldLocation);
		EditText newLocation = (EditText) findViewById(R.id.editTextNewLocation);
		EditText newHint = (EditText) findViewById(R.id.editTextNewHint);
		
		String oldLocationStr = oldLocation.getText().toString();
		String newLocationStr = newLocation.getText().toString();
		String newHintStr = newHint.getText().toString();
		
		BasicDBObject query = new BasicDBObject("location",
				oldLocationStr).append("huntID", id);
		DBCursor cursor = coll.find(query);
		
		if (!cursor.hasNext()) {
			Toast.makeText(
	                this, 
	                "This old location doesn't exist in HuntID " + id + "!", 
	                Toast.LENGTH_LONG)
	                .show();
		} else {
			BasicDBObject newHuntDBObj = new BasicDBObject("location",
					newLocationStr).append("huntID", id).append("hint", newHintStr);
			coll.update(cursor.next(), newHuntDBObj);
			updateTextView();
		}

		
	}
}
