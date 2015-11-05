package main.java.edu.kaist.cs.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ETRIReducer {

	private static BufferedWriter filebw = null;
	private static ArrayList<String> allFileList = new ArrayList<String>();
	private static String path = "";

	public void subDirList(String source) {
		allFileList.clear();
		File dir = new File(source);
		File[] fileList = dir.listFiles();
		try {
			for (int i = 0; i < fileList.length; i++) {
				File file = fileList[i];
				if (file.isFile()) {
					allFileList.add(file.getName());
				} else if (file.isDirectory()) {
					subDirList(file.getCanonicalPath().toString());
				}
			}
		} catch (IOException e) {

		}
	}

	public void parse() {
		try {

			filebw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI_training_set.json"),
							"UTF8"));

			// "D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI_training_set.json"

			for (int i = 0; i < allFileList.size(); i++) {

				String filename = allFileList.get(i);

				// System.out.println(path + filename);
				// filebw.write(path + filename + "\n");

				FileReader reader = new FileReader(path + filename);

				JSONParser jsonParser = new JSONParser();

				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

				JSONArray jsonSentence = (JSONArray) jsonObject.get("sentence");

				Iterator<?> s = jsonSentence.iterator();

				while (s.hasNext()) {

					JSONObject innerObj = (JSONObject) s.next();
					JSONArray word = (JSONArray) innerObj.get("word");

					if ((word.size() >= 3)) {
						JSONObject jsonObjWriter = new JSONObject();
						JSONArray jsonMorpWriter = new JSONArray();
						JSONArray writer = new JSONArray();
						JSONObject jsonText = new JSONObject();
						JSONObject jsonMorp = new JSONObject();

						long id = (long) innerObj.get("id");
						String text = (String) innerObj.get("text");
						jsonObjWriter.put("filePath", path + filename);
						jsonText.put("text", text);
						writer.add(jsonText);

						System.out.println(text);
						// jsonObjWriter.put("text", text);
						// System.out.println(id + "\t" + text);
						// filebw.write(id + "\t" + text + "\n");

						JSONArray morp = (JSONArray) innerObj.get("morp");
						Iterator m = morp.iterator();

						while (m.hasNext()) {
							jsonMorpWriter.add(m.next());
							// filebw.write(m.next().toString() + "\n");
						}
						jsonMorp.put("morp", jsonMorpWriter);
						writer.add(jsonMorp);

						// jsonObjWriter.put("morp", jsonMorpWriter);
						// jsonObjWriter.put("NE", jsonNEWriter);
						jsonObjWriter.put("sentence", writer);

						Gson gson = new GsonBuilder().setPrettyPrinting()
								.create();
						String jsonOutput = gson.toJson(jsonObjWriter);

						filebw.write(jsonOutput);

					}

				}
			}
			filebw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] ar) {
		ETRIReducer nlqjp = new ETRIReducer();
		path = "D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI\\";
		// "D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI\\"
		nlqjp.subDirList(path);
		nlqjp.parse();
	}
}
