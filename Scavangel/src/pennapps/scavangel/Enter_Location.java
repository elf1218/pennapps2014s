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

public class Enter_Location extends Activity {
	
	int id;
	DB db;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_location);
		Intent i = getIntent();
		id = i.getExtras().getInt("id");
		TextView hintTextView = (TextView)findViewById(R.id.hint);
		
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
			BasicDBObject hintquery = new BasicDBObject("initHintID", id);
			DBCursor cursor = coll.find(hintquery);
			if(cursor.hasNext()){
				String initHint = (String)cursor.next().get("initHint");
				hintTextView.setText(initHint);
			}
		}
	}
	
	public void onSubmitButtonClick(View v){
		EditText guess = (EditText)findViewById(R.id.locationguess);
		String locationGuessOut = guess.getText().toString().toLowerCase().trim();
		TextView hintTextView = (TextView)findViewById(R.id.hint);
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
			BasicDBObject query = new BasicDBObject("location", locationGuessOut);
			DBCursor cursor = coll.find(query);
			
			if(!cursor.hasNext()){
				Toast.makeText(
		                this, 
		                "Incorrect location - try again!", 
		                Toast.LENGTH_LONG)
		                .show();
			}
			else{
				while(cursor.hasNext()){
					DBObject obj = cursor.next();
					if(((Integer)obj.get("huntID")).intValue() == id){
						String locationHint = (String)obj.get("hint");
						hintTextView.setText(locationHint);
					}
				}
			}
		}

	}
}