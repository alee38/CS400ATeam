package CS400ATeam.application;

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

public class Inventory {
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

	private HashMap<String, HashNode> table;
	
	// constructor
	private Inventory() {
		this.table = new HashMap<String,HashNode>();
	}

	// read json file and add to inventoryTable
	private void readFile(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {

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


	
	private void writeJSON() {
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

	// test
	private void printTable() {
		for (String key : table.keySet()) {
			System.out.println(key);
			System.out.println((int) Math.abs(key.hashCode() % 11));
			//System.out.println(table.get(key));
			//System.out.println(table.get(key)[1]);
			System.out.println();
			
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		// TODO Auto-generated method stub
		String test = "/Users/qye/eclipse-workspace/TeamProject/CS400ATeam/application/test.json";
		Inventory inventory = new Inventory();
		inventory.readFile(test);
		//inventory.printTable();
		inventory.writeJSON();
		
	}

}
