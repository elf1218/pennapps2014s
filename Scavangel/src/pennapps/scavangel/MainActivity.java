package pennapps.scavangel;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	DB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*String connectionstring="mongodb://pennapps:pennapps@ds027509.mongolab.com:27509/scav";
		MongoClientURI uri  = new MongoClientURI(connectionstring);
		try {
			MongoClient client = new MongoClient(uri);
			db = client.getDB("scav");
		} catch (UnknownHostException e) {
			System.out.println("ERRORJOQRIJOQWRJIQ");
			System.exit(0);
		}*/

		
		setContentView(R.layout.activity_main);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onLoginButtonClick(View v){
		EditText userName = (EditText)findViewById(R.id.editText1);
		EditText pwd = (EditText)findViewById(R.id.editText2);
		String userNameOut = userName.getText().toString();
		String pwdOut = pwd.getText().toString();
		
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
			DBCollection coll = db.getCollection("loginDB");
			
			BasicDBObject query = new BasicDBObject("username", userNameOut);
			DBCursor cursor = coll.find(query);
			
			if(!cursor.hasNext()){
				BasicDBObject createAcc = new BasicDBObject("username", 
						userNameOut).append("password", pwdOut);
				coll.insert(createAcc);
				Toast.makeText(
		                this, 
		                "Account created!", 
		                Toast.LENGTH_LONG)
		                .show();
				goNext(userNameOut);
			}
			else{
				DBObject info = cursor.next();
				String pwdStored = (String) info.get("password");
				if(pwdStored.equals(pwdOut)){
					Toast.makeText(
			                this, 
			                "Login Success!", 
			                Toast.LENGTH_LONG)
			                .show();
					goNext(userNameOut);
				}
				else{
					Toast.makeText(
			                this, 
			                "Login Fail!", 
			                Toast.LENGTH_LONG)
			                .show();
				}
			}

		}
	}
	
	public void goNext(String username){
		Intent i = new Intent(this, Activity_Form.class);
		i.putExtra("username", username);
		startActivityForResult(i, 1);
	}
	
/*	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		Toast.makeText(
                this, 
                "Thanks for using!", 
                Toast.LENGTH_LONG)
                .show();
	}*/
/*
	public void onTestButtonOneClick(View v){
		boolean auth = db.authenticate("pennapps", "pennapps".toCharArray());

		System.out.println(auth);

		DBCollection coll = db.getCollection("testCollection");

		BasicDBObject doc = new BasicDBObject("name", "MongoDB").
				append("type", "database").
				append("count", 1).
				append("info", new BasicDBObject("x", 203).append("y", 102));

		coll.insert(doc);

		System.out.println("STEP 1 SUCCESSSS???@?!?!?!");

	}

	public void onTestButtonTwoClick(View v){
		boolean auth = db.authenticate("pennapps", "pennapps".toCharArray());

		System.out.println(auth);

		DBCollection coll = db.getCollection("testCollection");

		BasicDBObject query = new BasicDBObject("name", "MongoDB");

		DBCursor cursor = coll.find(query);

		try {
			while(cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}

	}
*/
}
