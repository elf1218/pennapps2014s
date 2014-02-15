package pennapps.scavangel;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Create_Hunt extends MainActivity {
	
	String username;
	int id;
	
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
			
			BasicDBObject query = new BasicDBObject("currentHuntID", 
									new BasicDBObject("$gt", -1));
			DBCursor cursor = coll.find(query);
			if(!cursor.hasNext()){
				//TODO
			}
			else{
				DBObject info = cursor.next();
				id = (Integer) info.get("currentHuntID") + 1;
				BasicDBObject newID = new BasicDBObject("currentHuntID", id);
			}
		}
	}
}
