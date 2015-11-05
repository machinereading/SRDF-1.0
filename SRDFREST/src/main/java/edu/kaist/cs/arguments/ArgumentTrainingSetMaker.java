package main.java.edu.kaist.cs.arguments;

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
									"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\v1\\arguments\\NLQ_arguments_training.txt"),
							"UTF8"));

			FileReader reader = new FileReader(
					"D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\v1\\NLQ_ETRI_training_set.json");

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

				int phaOpen = 0;
				int phaClose = 0;

				int dPhaOpen = 0;
				int dPhaClose = 0;

				for (int i = 0; i < morpLength; i++) {

					String tempType = allOfType.get(i);
					String tempLemma = allOfLemma.get(i);

					if (tempLemma.equals("(")) {
						phaOpen = i;
					}

					if (tempLemma.equals(")")) {
						phaClose = i;
					}

					if (tempLemma.equals("《")) {
						dPhaOpen = i;
					}

					if (tempLemma.equals("》")) {
						dPhaClose = i;
					}

					if (tempType.equals("JKS") || tempType.equals("JKC")
							|| tempType.equals("JKG") || tempType.equals("JKO")
							|| tempType.equals("JKB") || tempType.equals("JKV")
							|| tempType.equals("JKQ") || tempType.equals("JX")
							|| tempType.equals("JC")) {

						indexOfJ.add(i);

						if (tempLemma.equals("의")) {
							if (!allOfType.get(i - 1).equals("NNG")
									&& !allOfType.get(i - 1).equals("NNB")
									&& !allOfType.get(i - 1).equals("NNP")
									&& !allOfType.get(i - 1).equals("NR")
									&& !allOfType.get(i - 1).equals("NP")
									&& !allOfLemma.get(i - 1).equals("》")
									&& !allOfLemma.get(i - 1).equals(")")) {
								indexOfChunk.add(i);
							}
						}

						else if (!tempLemma.equals("의")
								&& !tempLemma.equals("와")
								&& !tempLemma.equals("과")) {

							indexOfChunk.add(i);
						}

					} else if (tempType.equals("XSV") || tempType.equals("VV")) {

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

					} else if (tempType.equals("VCP") || tempType.equals("VCN")) {

						// ex) 남성인 친구
						if (allOfType.get(i + 1).equals("ETM")) {

						} else {
							indexOfV.add(i);
							indexOfChunk.add(i);

						}

					} else if (tempType.equals("EF") || tempType.equals("EP")
							|| tempType.equals("EC") || tempType.equals("ETN")
							|| tempType.equals("ETM") || tempType.equals("SF")
							|| tempType.equals("SE") || tempType.equals("SW")
							|| tempType.equals("SP") || tempType.equals("VX")
							|| tempType.equals("VA")) {

						if (tempType.equals("SP") && tempLemma.equals(".")
								&& allOfType.get(i - 1).equals("SN")
								&& allOfType.get(i + 1).equals("SN")) {

						} else if (tempType.equals("SW") && tempLemma.equals("·")
								&& allOfType.get(i - 1).equals("SN")
								&& allOfType.get(i + 1).equals("SN")) {

						} else if (tempType.equals("ETM")
								&& allOfLemma.get(i).equals("ㄴ")
								&& allOfType.get(i - 1).equals("VCP")) {

						} else if (tempType.equals("ETM")
								&& allOfType.get(i - 1).equals("VA")) {

						} else if (tempType.equals("VA")
								&& allOfType.get(i + 1).equals("ETM")) {

						} else if (tempLemma.equals("%")) {

						} else if (tempLemma.equals("℃")) {

						} else {
							indexOfE.add(i);
							indexOfChunk.add(i);
						}

					}

				}

				for (int i = phaOpen; i <= phaClose; i++) {
					indexOfChunk.remove((Object) i);
				}

				for (int i = dPhaOpen; i <= dPhaClose; i++) {
					indexOfChunk.remove((Object) i);
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

								if (!isSubject) {
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
