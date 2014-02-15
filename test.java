import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientURI;

import java.util.Arrays;

public class test{
      public static void main(String[] args){
         try{
            String connectionstring="mongodb://pennapps:pennapps@ds027509.mongolab.com:27509/scav";
            MongoClientURI uri  = new MongoClientURI(connectionstring);
            MongoClient client = new MongoClient(uri);
            
            DB db = client.getDB("scav");
            
            boolean auth = db.authenticate("pennapps", "pennapps".toCharArray());
            
            System.out.println(auth);
            
            DBCollection coll = db.getCollection("testCollection");
            
            BasicDBObject doc = new BasicDBObject("name", "MongoDB").
				append("type", "database").
				append("count", 1).
				append("info", new BasicDBObject("x", 203).append("y", 102));

		      coll.insert(doc);
            
            /*BasicDBObject query = new BasicDBObject("name", "MongoDB");
            
            
            DBCursor cursor = coll.find(query);
            
            try {
               while(cursor.hasNext()) {
                   System.out.println(cursor.next());
               }
            } finally {
               cursor.close();
            }
            */

         }
         catch (Exception e){
            System.out.println("exce");
         }
      }

}