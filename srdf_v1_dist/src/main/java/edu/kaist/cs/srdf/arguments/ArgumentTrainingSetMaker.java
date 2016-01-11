package main.java.edu.kaist.cs.srdf.arguments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ArgumentTrainingSetMaker {
	private static BufferedWriter filebw = null;

	private static ArrayList<String> allOfLemma = new ArrayList<String>();
	private static ArrayList<String> allOfType = new ArrayList<String>();
	private static ArrayList<Long> allOfPosition = new ArrayList<Long>();
	// private static ArrayList<Integer> allOfSentenceLength = new
	// ArrayList<Integer>();
	// private static ArrayList<Integer> allOfStartPoint = new
	// ArrayList<Integer>();
	// private static ArrayList<Integer> allOfEndPoint = new
	// ArrayList<Integer>();
	// private static ArrayList<String> allOfPostLemma = new
	// ArrayList<String>();
	// private static ArrayList<String> allOfPostType = new ArrayList<String>();
	private static ArrayList<String> allOfTrainingSet = new ArrayList<String>();
	private static ArrayList<Integer> removePoint = new ArrayList<Integer>();
	private static ArrayList<Integer> indexOfJ = new ArrayList<Integer>();
	private static ArrayList<Integer> indexOfV = new ArrayList<Integer>();
	private static ArrayList<Integer> indexOfE = new ArrayList<Integer>();
	private static ArrayList<Integer> indexOfChunk = new ArrayList<Integer>();

	public void make() {
		try {
			filebw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\arguments\\Wiki_arguments_training.txt"),
							"UTF8"));

			FileReader reader = new FileReader(
					"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_ETRI_training_set.json");

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONArray jsonTraining = (JSONArray) jsonObject.get("training");

			Iterator<?> t = jsonTraining.iterator();

			while (t.hasNext()) {
				JSONObject sentence = (JSONObject) t.next();
				JSONArray jsonSentence = (JSONArray) sentence.get("sentence");

				allOfLemma.clear();
				allOfType.clear();
				allOfPosition.clear();
				removePoint.clear();
				indexOfJ.clear();
				indexOfV.clear();
				indexOfE.clear();
				indexOfChunk.clear();
				allOfTrainingSet.clear();

				Iterator<?> s = jsonSentence.iterator();

				JSONObject text = (JSONObject) s.next();
				JSONObject morp = (JSONObject) s.next();

				JSONArray morpArr = (JSONArray) morp.get("morp");
				Iterator<?> m = morpArr.iterator();

				while (m.hasNext()) {
					JSONObject mo = (JSONObject) m.next();

					String lemma = (String) mo.get("lemma");
					String type = (String) mo.get("type");
					long position = (Long) mo.get("position");

					allOfLemma.add(lemma);
					allOfType.add(type);
					allOfPosition.add(position);

				}

				int morpLength = allOfLemma.size();

				String trainingData = "";

				String lemma = "";
				String type = "";
				String sentenceLength = "";
				String startPosition = "";
				String endPosition = "";
				String postLemma = "";
				String postType = "";
				String subject = "";

				for (int i = 0; i < morpLength; i++) {

					String temp = allOfType.get(i);
					

					if (temp.equals("JKS") || temp.equals("JKC")
							|| temp.equals("JKG") || temp.equals("JKO")
							|| temp.equals("JKB") || temp.equals("JKV")
							|| temp.equals("JKQ") || temp.equals("JX")
							|| temp.equals("JC")) {

						indexOfJ.add(i);
						indexOfChunk.add(i);

					} else if (temp.equals("XSV") || temp.equals("VV")) {

						if (i > 0) {
							String prevType = allOfType.get(i - 1);
							if (prevType.equals("NNG")) {
								indexOfV.add(i - 1);
								indexOfV.add(i);

								indexOfChunk.add(i - 1);
								indexOfChunk.add(i);
							} else {
								indexOfV.add(i);
								indexOfChunk.add(i);
							}
						}

					} else if (temp.equals("VCP") || temp.equals("VCN")) {

						// ex) 남성인 친구
						if (allOfType.get(i + 1).equals("ETM")) {

						} else {
							indexOfV.add(i);
							indexOfChunk.add(i);

						}

					} else if (temp.equals("EF") || temp.equals("EP")
							|| temp.equals("EC") || temp.equals("ETN")
							|| temp.equals("ETM") || temp.equals("SF")
							|| temp.equals("SE") || temp.equals("SW")
							|| temp.equals("VX") || temp.equals("VA")) {

						if (temp.equals("ETM") && allOfLemma.get(i).equals("ㄴ")
								&& allOfType.get(i - 1).equals("VCP")) {
							
						} else{
							indexOfE.add(i);
							indexOfChunk.add(i);
						}
						
					}

				}

				int startPoint = 0;
				boolean isSubject = false;

				for (int i = 0; i < indexOfChunk.size(); i++) {

					int positionOfChunk = indexOfChunk.get(i);

					if (positionOfChunk > 0 && positionOfChunk < morpLength) {

						String compositionOfLemma = "";
						String compositionOfType = "";

						boolean plus = false;

						for (int j = startPoint; j < positionOfChunk; j++) {

							compositionOfLemma += allOfLemma.get(j);

							if (plus == false) {
								compositionOfType += allOfType.get(j);
								plus = true;
							} else {
								compositionOfType += "+" + allOfType.get(j);
							}

						}
						lemma = compositionOfLemma;
						type = compositionOfType;
						sentenceLength = String.valueOf(allOfPosition
								.get(allOfPosition.size() - 1));
						startPosition = String.valueOf(allOfPosition
								.get(startPoint));
						endPosition = String.valueOf(allOfPosition
								.get(positionOfChunk) - 1);
						postLemma = allOfLemma.get(positionOfChunk);
						postType = allOfType.get(positionOfChunk);

						if (!lemma.equals("") && !postType.equals("NNG")) {

							if (postLemma.equals("은") || postLemma.equals("는")
									|| postLemma.equals("이")
									|| postLemma.equals("가")) {
								
								if(!isSubject){
									if (!postType.equals("VCP")) {
										subject = "subject";
										isSubject = true;
									} else {
										subject = "object";
									}
								}
								
							} else {
								subject = "object";
							}

							if (postLemma.equals("\\")) {
								postLemma = "\\\\";
							}

							trainingData = "'" + lemma + "','" + type + "',"
									+ sentenceLength + "," + startPosition
									+ "," + endPosition + ",'" + postLemma
									+ "','" + postType + "'," + subject;

							System.out.println(trainingData);

							allOfTrainingSet.add(trainingData);
						}

						startPoint = positionOfChunk + 1;
					}
				}

				// trainingData = "'" + lemma + "','" + type + "',"
				// + sentenceLength + "," + startPosition + ","
				// + endPosition + ",'" + postLemma + "','" + postType
				// + "'," + subject;
				// allOfTrainingSet.add(trainingData);

				for (int i = 0; i < removePoint.size(); i++) {
					int remove = removePoint.get(i);
					allOfTrainingSet.remove(remove);
				}
				for (int i = 0; i < allOfTrainingSet.size(); i++) {
					filebw.write(allOfTrainingSet.get(i) + "\n");

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] ar) {
		ArgumentTrainingSetMaker stsm = new ArgumentTrainingSetMaker();
		stsm.make();
		try {
			filebw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
