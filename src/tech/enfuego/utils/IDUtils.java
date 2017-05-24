package tech.enfuego.utils;

import org.bson.types.ObjectId;

public class IDUtils {
	
	
	public static ObjectId getId() {
		
		ObjectId objectId = new ObjectId(); 
		String stringValue = objectId.toHexString();
		
		return new ObjectId(stringValue);
	}

}
