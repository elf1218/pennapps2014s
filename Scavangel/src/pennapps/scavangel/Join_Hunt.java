package pennapps.scavangel;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Join_Hunt extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_hunt);

	}
	
	public void onJoinButtonClick(View v){
		EditText huntID = (EditText)findViewById(R.id.editText1);
		int huntIDOut = Integer.parseInt(huntID.getText().toString());
		DB db = null;
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
			BasicDBObject query = new BasicDBObject("huntID", huntIDOut);
			DBCursor cursor = coll.find(query);
			if(!cursor.hasNext()){
				Toast.makeText(
		                this, 
		                "Event #" + huntIDOut + " does not exist!", 
		                Toast.LENGTH_LONG)
		                .show();
			}
			else{
				Intent i = new Intent(this, Enter_Location.class);
				i.putExtra("id", huntIDOut);
				startActivityForResult(i, 1);
			}	
		}
	}
}
