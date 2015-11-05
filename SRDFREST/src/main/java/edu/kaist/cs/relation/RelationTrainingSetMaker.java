package main.java.edu.kaist.cs.relation;

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

public class RelationTrainingSetMaker {

	private static BufferedWriter filebw = null;
	private static ArrayList<String> allOfLemma = new ArrayList<String>();
	private static ArrayList<String> allOfType = new ArrayList<String>();
	private static ArrayList<String> allOfTrainingSet = new ArrayList<String>();
	private static ArrayList<Integer> removePoint = new ArrayList<Integer>();

	public void make() {
		try {
			filebw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\relation\\NLQ_relation_training.txt"),
							"UTF8"));

			FileReader reader = new FileReader(
					"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI_training_set.json");

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONArray jsonTraining = (JSONArray) jsonObject.get("training");

			Iterator<?> t = jsonTraining.iterator();

			while (t.hasNext()) {
				JSONObject sentence = (JSONObject) t.next();
				JSONArray jsonSentence = (JSONArray) sentence.get("sentence");

				allOfLemma.clear();
				allOfType.clear();
				removePoint.clear();
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

					allOfLemma.add(lemma);
					allOfType.add(type);

				}

				for (int i = 0; i < allOfType.size(); i++) {

					String temp = allOfType.get(i);

					String trainingData = "";

					String lemma = "";
					String type = "";
					String postposition = "";
					String relation = "";

					if (temp.equals("JKS") || temp.equals("JKC")
							|| temp.equals("JKG") || temp.equals("JKO")
							|| temp.equals("JKB") || temp.equals("JKV")
							|| temp.equals("JKQ") || temp.equals("JX")
							|| temp.equals("JC") || temp.equals("XSN")
							|| temp.equals("XSA") || temp.equals("XSA")
							|| temp.equals("XSB")) {
						postposition = "yes";
					} else {
						postposition = "no";
					}

					if (!temp.equals("VCP") && !temp.equals("XSV")
							&& !temp.equals("VV")) {
						lemma = allOfLemma.get(i);
						type = temp;
						relation = "other";
					} else {
						if (temp.equals("XSV") || temp.equals("VV")) {

							if (i > 0) {
								String prevType = allOfType.get(i - 1);
								if (prevType.equals("NNG")) {
									lemma = allOfLemma.get(i - 1)
											+ allOfLemma.get(i);
									type = allOfType.get(i - 1) + "+" + temp;
									removePoint.add(i - 1);
								} else {
									lemma = allOfLemma.get(i);
									type = temp;
								}

								relation = "relation";

								if (temp.equals("XSV")) {
									postposition = "yes";
								}
							} else {
								lemma = allOfLemma.get(i);
								type = temp;
								relation = "relation";
							}

						} else if (temp.equals("VCP")) {

							String postType = allOfType.get(i + 1);

							lemma = allOfLemma.get(i);
							type = temp;

							if (postType.equals("ETM")) {
								relation = "other";
							} else {
								relation = "relation";
							}
						} else if (temp.equals("VNP")) {

							String postType = allOfType.get(i + 1);

							lemma = allOfLemma.get(i);
							type = temp;

							if (postType.equals("ETM")) {
								relation = "other";
							} else {
								relation = "relation";
							}
						}
					}

					trainingData = "'" + lemma + "','" + type + "',"
							+ postposition + "," + relation;
					allOfTrainingSet.add(trainingData);
				}

				for (int i = removePoint.size() - 1; i > 0; i--) {
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
		RelationTrainingSetMaker tsm = new RelationTrainingSetMaker();
		tsm.make();
		try {
			filebw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
