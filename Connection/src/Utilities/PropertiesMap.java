package Utilities;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class PropertiesMap {
	private HashMap<String,HashMap<String,String>> maps = new HashMap<String,HashMap<String,String>>();
	
	public HashMap<String,HashMap<String,String>> loadProperties(String path, String ...names) {	
		for(String name : names) { this.saveMap(path, name); }
		return this.maps;
	}
	
	public void loadProperty(String path, String name) { this.saveMap(path, name); }
	public HashMap<String,String> getProperty(String propertiesName) { return this.maps.get(propertiesName); }
	public String getValue(String propertiesName, String query) { return  this.maps.get(propertiesName).get(query); }
	private void saveMap(String path, String name) {
		Properties props = Props.getPropertiesFile(path, name);
		HashMap<String,String> values = new HashMap<String, String>();
		Set<Object> keys = props.keySet();		
		keys.forEach(key -> { values.put(key.toString(), props.getProperty(key.toString())); });
		this.maps.put(name, values);
	}
}