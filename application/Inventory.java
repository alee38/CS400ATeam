package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * This class represents the hash table used to store each item
 * with a node containing its name, location, and date. We also read
 * and write to a JSON file with this class.
 */
public class Inventory {
	
	// Inner class for the hash table
	class HashNode {
		String key;
		String location;
		String date;
		//private Node next;

		// Initialize a hashNode with given key and value
		// Key cannot be null, or else throw IllegalNullKeyException
		HashNode(String key, String location, String date) {
			if (key != null) {

				this.key = key;
				this.location = location;
				this.date=date;
				//this.next = null;
			}
		}
	}

	public HashMap<String, HashNode> table;

	// constructor
	public Inventory() {
		this.table = new HashMap<String,HashNode>();
	}

	// read json file and add to inventoryTable
	public void readFile(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {

		Object object = new JSONParser().parse(new FileReader(jsonFilepath));
		JSONObject jObject = (JSONObject) object;
		JSONArray packages = (JSONArray) jObject.get("itemArray");
		for (int i = 0; i < packages.size(); i++) {
			JSONObject item = (JSONObject) packages.get(i);
			String name = (String) item.get("itemName");
			String location = (String) item.get("itemRoom");
			String date = (String) item.get("itemDate");
			//System.out.println((int) Math.abs(name.hashCode() % 11));
			HashNode node=new HashNode(name, location, date);

			table.put(name, node);
		}
	}


	/*
	 * Writes JSON file to "output.json" from hash table.
	 */
	public void writeJSON() {
		JSONObject allitems = new JSONObject();
		JSONArray itemArray = new JSONArray();
		allitems.put("itemArray", itemArray);

		Set<String> keys = table.keySet();

		for (String key : keys) {
			String loc = table.get(key).location;
			String date = table.get(key).date;
			JSONObject item = new JSONObject();
			item.put("itemName", key);
			item.put("itemRoom", loc);
			item.put("itemDate", date);
			itemArray.add(item);

			// Write JSON file
			try (FileWriter file = new FileWriter("output.json")) {

				file.write(allitems.toJSONString());
				file.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//	// test
	//	public void printTable() {
	//		for (String key : table.keySet()) {
	//			System.out.println(key);
	//			System.out.println((int) Math.abs(key.hashCode() % 11));
	//			//System.out.println(table.get(key));
	//			//System.out.println(table.get(key)[1]);
	//			System.out.println();
	//			
	//		}
	//	}

	/*
	 * Writes JSON to console. 
	 * NOT NECESSARY
	 */
	public void updateJSON(Inventory table) {
		Gson gsonBuilder = new GsonBuilder().create();
		String jsonFromList = gsonBuilder.toJson(table);
		System.out.println(jsonFromList);
	}

	/*
	 * Getter for the hash table
	 */
	public HashMap<String, HashNode> getTable() {
		return this.table;
	}

	/*
	 * Used for testing functionality
	 * NOT NECESSARY
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		String test = "C:/Users/Andrew/eclipse-workspace/ATeam/CS400ATeam/items.json";
		Inventory inventory = new Inventory();
		inventory.readFile(test);
		//inventory.printTable();
		inventory.writeJSON();
		inventory.updateJSON(inventory);

	}

}
