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
import android.widget.Toast;

public class Edit_Hunt extends Activity {

	String username;
	int id;
	DB db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_hunt);
		Intent intent = getIntent();
		username = intent.getExtras().getString("username");
	}	
	
	public void onEditClick(View v){
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
			DBCollection admin = db.getCollection("huntAdmin");
			
			EditText requestedEditHuntID = (EditText)findViewById(R.id.editText1);
			int reqID = Integer.parseInt(requestedEditHuntID.getText().toString());
			BasicDBObject query = new BasicDBObject("huntID", reqID);
			DBCursor cursor = admin.find(query);
			if(!cursor.hasNext()){
				Toast.makeText(
		                this, 
		                "HuntID doesn't exist!", 
		                Toast.LENGTH_LONG)
		                .show();
			}
			else{
				DBObject info = cursor.next();
				String huntAdmin = (String)info.get("username");
				//id = (Integer)info.get("huntID");
				if (!huntAdmin.equals(username)) {
					Toast.makeText(
			                this, 
			                "You are not the admin of this hunt!", 
			                Toast.LENGTH_LONG)
			                .show();
				} else {
					Intent i = new Intent(this, Edit_Selected_Hunt.class);
					i.putExtra("huntID", reqID);
					startActivityForResult(i, 1);
				}
			}
		}
	}
}
