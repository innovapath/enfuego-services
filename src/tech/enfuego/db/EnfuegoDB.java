package tech.enfuego.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import tech.enfuego.Constants;

/**
 * 
 * @author abhishek
 * 
 * <p>
 * In this class we create a connection object to the Enfuego DB which is hosted on AWS and return the object.
 * We use an additional layer of POJO mapper which is called as Morphia(A POJO mapper for MongoDB).
 * 
 * <p>
 * Thus using the Mongo DB client connection object we create a <code>org.mongodb.morphia.Datastore</code> object and return that.
 * 
 * <p>
 * Both the objects of types <code>com.mongodb.MongoClient</code> and <code>org.mongodb.morphia.Datastore</code> are Singleton and thread-fail safe
 *
 */
public class EnfuegoDB {
	
	private static MongoClient DBConnection = null;
	private static Object mutex  = new Object();
	private static Morphia morphia = null;
	private static Datastore datastore;
	
	private EnfuegoDB(){
		
	}
	
	public static MongoClient getInstance(){
		if(DBConnection == null){
			synchronized (mutex) {
				if(DBConnection == null) DBConnection = new MongoClient(
						new MongoClientURI("mongodb://ec2-54-91-241-235.compute-1.amazonaws.com:27017"));
				
			}
		}
		return DBConnection;
	}
	
	
	public static Datastore getDataStore() {
		
		if(morphia == null){
			synchronized (mutex) {
				if(morphia == null) 
				{
					morphia = new Morphia();
					morphia.mapPackage("tech.enfuego.models");
				}
				
			}
		}
		
		if(datastore == null){
			synchronized (mutex) {
				if(datastore == null) 
				{
					datastore = morphia.createDatastore(getInstance(), Constants.DBNAME);
					datastore.ensureIndexes();
				}
				
			}
		}
		
		return datastore;
		
	}
	
	
	
	

	
//	public static void main(String [] args){
//		MongoClient client = new MongoClient(
//				new MongoClientURI("mongodb://ec2-54-227-218-201.compute-1.amazonaws.com:27017"));
//		MongoDatabase database = client.getDatabase("Enfuego");
//		MongoCollection<Document> collection = database.getCollection("Resume");
//		FindIterable<Document> doc = collection.find();
//		Iterator<Document> docIterator = doc.iterator();
//		while(docIterator.hasNext()){
//			Document d = docIterator.next();
//			collection.deleteOne(d);
//			System.out.println(d);
//			
//		}
//		//JSONObject obj = new JSONObject();
//		//obj.put("b", 4555);
//		//Document testDoc = new Document();
//		//testDoc.putAll(obj);
//		//collection.insertOne(testDoc);
//
//	}
}
