package Utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serial {
	public static byte[] serializeParams(Object[] params) {
		ByteArrayOutputStream bs= new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream (bs);
			os.writeObject(params); 
			os.close();
		} catch (IOException e) {  e.printStackTrace(); }
		return bs.toByteArray(); 
	}
		
	public static DataSet deserializeDS(byte[] b) {
		try {
			ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(b));
			DataSet response = (DataSet) is.readObject();
			is.close();
			return response;
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error: " + e); 
			e.printStackTrace(); 
			return null; 
		}
	}
}