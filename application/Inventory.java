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
		String test = "items.json";
		Inventory inventory = new Inventory();
		inventory.readFile(test);
		//inventory.printTable();
		inventory.writeJSON();


	}

}
