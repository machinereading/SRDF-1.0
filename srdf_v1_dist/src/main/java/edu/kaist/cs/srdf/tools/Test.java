package main.java.edu.kaist.cs.srdf.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Test {
	
	private static BufferedWriter filebw = null;
	
	public void doit(){
		try {
			filebw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_training_set.txt"),
							"UTF8"));
			
			//"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_training_set.txt"
			//"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\relation\\NLQ_relation_training.txt"

			FileReader reader = new FileReader(
					"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_ETRI_training_set.json");
			
			//"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_ETRI_training_set.json"
			//"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_ETRI_training_set.json"

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONArray jsonTraining = (JSONArray) jsonObject.get("training");

			Iterator<?> t = jsonTraining.iterator();

			while (t.hasNext()) {
				JSONObject sentence = (JSONObject) t.next();
				JSONArray jsonSentence = (JSONArray) sentence.get("sentence");


				Iterator<?> s = jsonSentence.iterator();

				JSONObject text = (JSONObject) s.next();
				JSONObject morp = (JSONObject) s.next();
				
				filebw.write(text.toString().substring(9, text.toString().length()-2) + "\n");
				
			}
			filebw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] ar){
		Test t = new Test();
		t.doit();
	}
}
