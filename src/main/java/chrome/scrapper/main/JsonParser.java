package chrome.scrapper.main;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonParser {
	private static JSONArray foundArray;
	private static List<Favorites> returnList = new ArrayList<Favorites>();

	public static JSONArray getFoundArray() {
		return foundArray;
	}

	public static void setFoundArray(JSONArray foundArray) {
		JsonParser.foundArray = foundArray;
	}

	public static List<Favorites> getReturnList() {
		return returnList;
	}

	public static void setReturnList(List<Favorites> returnList) {
		JsonParser.returnList = returnList;
	}

	/*
	 * This is the read-in method for JSON
	 */
	public static void readInFile(String filePath, String searchMaterials, String startPoint,String firstKey,String secondKey, String urlChange) {
		returnList.clear();
		try (FileReader reader = new FileReader(filePath)) {
			// get the json file
			JSONObject jsonObject = null;
			jsonObject = (JSONObject) new JSONParser().parse(reader);
			
			// get to specified starting point in the json file
			String[] MoveThrough = startPoint.split("->");
			int size = MoveThrough.length;
			JSONArray child = null;
			if (size > 0) {
				for (int spot = 0; spot < size; spot++) {
					if (spot < (size - 1))
						jsonObject = (JSONObject) jsonObject.get(MoveThrough[spot]);
					else
						child = (JSONArray) jsonObject.get(MoveThrough[spot]);
				}
			}
			// get the folder(s) to search through and how to search
			String[] folders = searchMaterials.split(";");
			int folderNum = folders.length;
			if (folderNum > 0) {
				for (int x = 0; x < folderNum; x++) {
					String[] folder = folders[x].split("->");
					findJsonArray(child, folder[0], folder[1], folder[2]);
					retrieveData(foundArray, folder[0], folder[1],firstKey, secondKey,urlChange);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 *  Finds specified folder via findKey and keyIndicator from childrens array
	 */
	public static void findJsonArray(JSONArray childrens, String findKey, String arrayIndicator, String keyIndicator) {
		JSONObject temp = null;
		int size = childrens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				// get object using index from children's array
				temp = (JSONObject) childrens.get(i);
				if (temp.containsKey(arrayIndicator)) {
					findJsonArray((JSONArray) temp.get(arrayIndicator), findKey, arrayIndicator, keyIndicator);
				}
				// get url
				String key = (String) temp.get(keyIndicator);
				if (key.equals(findKey)) {
					foundArray = (JSONArray) temp.get(arrayIndicator);
					break;
				}
			}
		}
	}
	/*
	 *  stores the nodes within childrens into returnList
	 */
	public static void retrieveData(JSONArray childrens, String childName, String arrayIndicator, String firstKey, String secondKey, String urlChange) {
		JSONObject temp = null;
		int size = childrens.size();
		int counter=0;
		String[] changeList= urlChange.split(";");
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				// get object using index from childrens array
				temp = (JSONObject) childrens.get(i);
				String name = (String) temp.get(firstKey);
				if (temp.containsKey(arrayIndicator)) {
					retrieveData((JSONArray) temp.get(arrayIndicator), (childName + "\\" + name), arrayIndicator,firstKey, secondKey,urlChange);
				}
				// get data
				String key = (String) temp.get(secondKey);
				if (key != null) {
					Favorites node = new Favorites();
					node.setName(name);
					// searching through url change to update urls
					counter=0;
					while(counter<changeList.length) {
						String change[] = changeList[counter].split("->");
						if(key.contains(change[0])) {
							key=(key.replace(change[0], change[1]));
						}
						counter++;
					}
					node.setURL(key);
					node.setDirectory(childName);
					node.setTemplateId(-1);
					returnList.add(node);
				}
			}
		}
	}
}
