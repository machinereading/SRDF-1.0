package main.java.edu.kaist.cs.srdf.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

public class WikiJsonParser {

	private static ArrayList<String> allFileList = new ArrayList<String>();
	private static String postfix1 = "";
	private static String postfix2 = "AO";
	private static String path = "";
	private static BufferedWriter filebw = null;
	private static int foldTotal = 0;
	private static int phase1FoldTotal = 0;
	private static int under3 = 0;
	private static int over10 = 0;
	private static int others = 0;

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
			// read the json file

			filebw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							"D:\\KAIST\\KnowledgeBase\\ETRI_output\\extracted_ETRI\\phase1\\"
									+ postfix2 + "_" + postfix1 + ".json"),
					"UTF8"));

			int totalCount = 0;
			int phase1Count = 0;
			boolean containsVP = false;

//			System.out.println(allFileList.size());
			for (int i = 0; i < allFileList.size(); i++) {

				String filename = allFileList.get(i);

				System.out.println(path + filename);
				// filebw.write(path + filename + "\n");

				FileReader reader = new FileReader(path + filename);

				JSONParser jsonParser = new JSONParser();

				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

				JSONArray jsonSentence = (JSONArray) jsonObject.get("sentence");
				
				Iterator<?> s = jsonSentence.iterator();
				
				while (s.hasNext()) {
					
					containsVP = false;

					totalCount++;
					foldTotal++;

					JSONObject innerObj = (JSONObject) s.next();
					JSONArray word = (JSONArray) innerObj.get("word");
					JSONArray ne = (JSONArray) innerObj.get("NE");
					JSONArray dependency = (JSONArray) innerObj.get("dependency");
					
					Iterator di = dependency.iterator();
					while(di.hasNext()){
						JSONObject innerDI = (JSONObject) di.next();
						String temp = (String) innerDI.get("label");
//						System.out.println(temp);
						if(temp.equals("VP") || temp.equals("VNP")){
							containsVP = true;
						}
					}
					

					if ((word.size() >= 3 && word.size() <= 10)
							&& (ne.size() >= 2) && containsVP) {
						JSONObject jsonObjWriter = new JSONObject();
						JSONArray jsonMorpWriter = new JSONArray();
						JSONArray jsonNEWriter = new JSONArray();
						JSONArray writer = new JSONArray();
						JSONObject jsonText = new JSONObject();
						JSONObject jsonMorp = new JSONObject();
						JSONObject jsonNE = new JSONObject();

						long id = (long) innerObj.get("id");
						String text = (String) innerObj.get("text");
						jsonObjWriter.put("filePath", path + filename);
						jsonText.put("text", text);
						writer.add(jsonText);
						// jsonObjWriter.put("text", text);
//						System.out.println(id + "\t" + text);
						// filebw.write(id + "\t" + text + "\n");

						JSONArray morp = (JSONArray) innerObj.get("morp");
						Iterator m = morp.iterator();

						while (m.hasNext()) {
							jsonMorpWriter.add(m.next());
							// filebw.write(m.next().toString() + "\n");
						}
						jsonMorp.put("morp", jsonMorpWriter);
						writer.add(jsonMorp);

						Iterator n = ne.iterator();

						while (n.hasNext()) {
							jsonNEWriter.add(n.next());
							// filebw.write(n.next().toString() + "\n");
						}
						jsonNE.put("NE", jsonNEWriter);
						writer.add(jsonNE);

						// jsonObjWriter.put("morp", jsonMorpWriter);
						// jsonObjWriter.put("NE", jsonNEWriter);
						jsonObjWriter.put("sentence", writer);

						Gson gson = new GsonBuilder().setPrettyPrinting()
								.create();
						String jsonOutput = gson.toJson(jsonObjWriter);

						filebw.write(jsonOutput);

						phase1Count++;
						phase1FoldTotal++;
					} else if(word.size() < 3){
						under3++;
					} else if(word.size() > 10){
						over10++;
					} else{
						others++;
					}

				}
			}
//			System.out.println(totalCount);
//			System.out.println(phase1Count);

			filebw.write("\n\n" + "Total : " + totalCount + "\n");
			filebw.write("Phase1 : " + phase1Count);

			filebw.close();

		} catch (FileNotFoundException ex) {

			ex.printStackTrace();

		} catch (IOException ex) {

			ex.printStackTrace();

		} catch (NullPointerException ex) {

			ex.printStackTrace();

		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] ar) {
		WikiJsonParser wjp = new WikiJsonParser();
		
	
		String postfix1Attach = "";
		for(int i = 0; i < 79; i++){
			if(i < 10){
				postfix1Attach = "0" + i;
			}
			else {
				postfix1Attach = "" + i;
			}
			
			postfix1 = "wiki_" + postfix1Attach; 
		
			path = "D:\\KAIST\\KnowledgeBase\\ETRI_output\\extracted_ETRI\\"
					+ postfix2 + "\\" + postfix1 + "\\";
			wjp.subDirList(path);
			wjp.parse();
		}
		
		System.out.println("Fold Total : " + foldTotal);
		System.out.println("Phase1 Fold Total : " + phase1FoldTotal);
		System.out.println("Under 3 words : " + under3);
		System.out.println("Over 10 words : " + over10);
		System.out.println("Others : " + others);
		
	}
}
