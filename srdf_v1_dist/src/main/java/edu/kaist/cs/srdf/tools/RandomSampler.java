package main.java.edu.kaist.cs.srdf.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RandomSampler {

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
			for (int i = 0; i < allFileList.size(); i++) {

				String filename = allFileList.get(i);

				System.out.println(path + filename);

				FileReader reader = new FileReader(path + filename);

				JSONParser jsonParser = new JSONParser();

				JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

				JSONArray sentence = (JSONArray) jsonObject.get("sentence");

				Iterator<?> s = sentence.iterator();
			}
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

	private ArrayList<Integer> sampledNLQ = new ArrayList<Integer>();

	public void preSampledNLQ() {
		sampledNLQ.add(1);
		sampledNLQ.add(2);
		sampledNLQ.add(3);
		sampledNLQ.add(14);
		sampledNLQ.add(19);
		sampledNLQ.add(24);
		sampledNLQ.add(28);
		sampledNLQ.add(41);
		sampledNLQ.add(47);
		sampledNLQ.add(59);
		sampledNLQ.add(77);
		sampledNLQ.add(80);
		sampledNLQ.add(86);
		sampledNLQ.add(87);
		sampledNLQ.add(88);
		sampledNLQ.add(93);
		sampledNLQ.add(103);
		sampledNLQ.add(111);
		sampledNLQ.add(114);
		sampledNLQ.add(134);
		sampledNLQ.add(135);
		sampledNLQ.add(142);
		sampledNLQ.add(144);
		sampledNLQ.add(145);
		sampledNLQ.add(157);
		sampledNLQ.add(162);
		sampledNLQ.add(166);
		sampledNLQ.add(171);
		sampledNLQ.add(181);
		sampledNLQ.add(201);
		sampledNLQ.add(211);
		sampledNLQ.add(212);
		sampledNLQ.add(226);
		sampledNLQ.add(227);
		sampledNLQ.add(237);
		sampledNLQ.add(238);
		sampledNLQ.add(239);
		sampledNLQ.add(244);
		sampledNLQ.add(248);
	}


	public void sampleInNLQ() {
		preSampledNLQ();
		while (sampledNLQ.size() < 150) {
			int random = (int) (Math.random() * 250);
			if (!sampledNLQ.contains(random)) {
				sampledNLQ.add(random);
			}

		}
		System.out.println(sampledNLQ);
	}
	
	// A, B, C, D, E, F, G, H, I, J, K, L, M, N
	// 14
	private ArrayList<String> sampledWiki = new ArrayList<String>();
	public void sampleInWiki(){
		while (sampledWiki.size() < 150){
			int alpha = (int) (Math.random() * 14);
			int beta = (int) (Math.random() * 99);

			String alphaStr = "";
			switch(alpha){
			case 0:
				alphaStr = "AA";
				break;
			case 1:
				alphaStr = "AB";
				break;
			case 2:
				alphaStr = "AC";
				break;
			case 3:
				alphaStr = "AD";
				break;
			case 4:
				alphaStr = "AE";
				break;
			case 5:
				alphaStr = "AF";
				break;
			case 6:
				alphaStr = "AG";
				break;
			case 7:
				alphaStr = "AH";
				break;
			case 8:
				alphaStr = "AI";
				break;
			case 9:
				alphaStr = "AJ";
				break;
			case 10:
				alphaStr = "AK";
				break;
			case 11:
				alphaStr = "AL";
				break;
			case 12:
				alphaStr = "AM";
				break;
			case 13:
				alphaStr = "AN";
				break;
			}
			
			//AA_wiki_00
			String fileName = alphaStr + "_wiki_" + beta;
			sampledWiki.add(fileName);
			
		}
		System.out.println(sampledWiki);
	}

	public static void main(String[] ar) {
		RandomSampler pp = new RandomSampler();
//		path = "D:\\KAIST\\KnowledgeBase\\ETRI_output\\extracted_ETRI\\phase1\\";
//		pp.subDirList(path);
//		pp.parse();
		
//		pp.sampleInNLQ();
		
		pp.sampleInWiki();
	}
}
