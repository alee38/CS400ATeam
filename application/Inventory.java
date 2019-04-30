package application;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Inventory {

    private InventoryTable table;
    
    // constructor
    private Inventory() {
        this.table=new InventoryTable();
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
            table.add(name, location, date);
            
        }
    }
    
    private void updateJSON(Inventory table) {
    	Gson gsonBuilder = new GsonBuilder().create();
    	String jsonFromList = gsonBuilder.toJson(table);
    	System.out.println(jsonFromList);
    }
    
    
    //test
    private void printTable() {
        System.out.println(table.get("computer-222")[1]);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
        // TODO Auto-generated method stub
        String test="C:/Users/Andrew/eclipse-workspace/ATeam/CS400ATeam/items.json";
        Inventory inventory=new Inventory();
        inventory.readFile(test);
       // inventory.updateJSON(inventory);
        inventory.printTable();
        

    }

}
