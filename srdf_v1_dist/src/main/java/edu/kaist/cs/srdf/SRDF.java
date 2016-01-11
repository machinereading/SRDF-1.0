package main.java.edu.kaist.cs.srdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

import main.java.edu.kaist.cs.srdf.globals.ObjectTuple;
import main.java.edu.kaist.cs.srdf.globals.PositionTuple;
import main.java.edu.kaist.cs.srdf.globals.Triple;
import main.java.edu.kaist.cs.srdf.tools.ETRIExecutor;
import main.java.edu.kaist.cs.srdf.weka.classifiers.ArgumentWekaWrapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SRDF {

	ArrayList<String> classifiedRelations = new ArrayList<String>();
	ArrayList<String> classifiedArguments1 = new ArrayList<String>();
	ArrayList<String> classifiedArguments2 = new ArrayList<String>();

	public String inputSentence() {

		String sentence;

		scan = new Scanner(System.in);

		System.out.println("문장을 입력하세요:");

		sentence = scan.nextLine();

		return sentence;
	}

	public String execETRI(String sentence) {
		ETRIExecutor etriE = new ETRIExecutor();
		String resultOfETRI = null;
		try {
			resultOfETRI = etriE.getResult(sentence);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultOfETRI;
	}

	public String extractPOS(String resultOfETRI) {

		String jsonOutput = null;

		try {
			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(resultOfETRI);

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
					jsonObjWriter.put("filePath", "user input");
					jsonText.put("text", text);
					writer.add(jsonText);

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

					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					jsonOutput = gson.toJson(jsonObjWriter);

				}

			}
		} catch (Exception e) {

		}

		return jsonOutput;
	}

	public ArrayList<String> extractRelations(String extractedPOS) {

		positionOfRelation.clear();

		ArrayList<String> allOfLemma = new ArrayList<String>();
		ArrayList<String> allOfType = new ArrayList<String>();
		ArrayList<String> allOfPosition = new ArrayList<String>();
		ArrayList<String> allOfTrainingSet = new ArrayList<String>();
		ArrayList<Integer> removePoint = new ArrayList<Integer>();

		try {

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(extractedPOS);

			JSONArray jsonSentence = (JSONArray) jsonObject.get("sentence");

			Iterator<?> s = jsonSentence.iterator();

			JSONObject text = (JSONObject) s.next();
			JSONObject morp = (JSONObject) s.next();

			JSONArray morpArr = (JSONArray) morp.get("morp");
			Iterator<?> m = morpArr.iterator();

			while (m.hasNext()) {
				JSONObject mo = (JSONObject) m.next();

				String lemma = (String) mo.get("lemma");
				String type = (String) mo.get("type");
				long position = (long) mo.get("position");

				allOfLemma.add(lemma);
				allOfType.add(type);
				allOfPosition.add(String.valueOf(position));

			}

			for (int i = 0; i < allOfType.size(); i++) {

				String temp = allOfType.get(i);

				String trainingData = "";

				String lemma = "";
				String type = "";
				String postposition = "";
				String relation = "";

				if (temp.equals("JKS") || temp.equals("JKC") || temp.equals("JKG") || temp.equals("JKO")
						|| temp.equals("JKB") || temp.equals("JKV") || temp.equals("JKQ") || temp.equals("JX")
						|| temp.equals("JC") || temp.equals("XSN") || temp.equals("XSA") || temp.equals("XSA")
						|| temp.equals("XSB")) {
					postposition = "yes";
				} else {
					postposition = "no";
				}

				if (!temp.equals("VCP") && !temp.equals("XSV") && !temp.equals("VV")) {
					lemma = allOfLemma.get(i);
					type = temp;
					relation = "other";
				} else {
					if (temp.equals("XSV") || temp.equals("VV")) {

						if (i > 0) {
							String prevType = allOfType.get(i - 1);
							if (prevType.equals("NNG")) {
								lemma = allOfLemma.get(i - 1) + allOfLemma.get(i);
								type = allOfType.get(i - 1) + "+" + temp;
								removePoint.add(i - 1);
							} else {
								lemma = allOfLemma.get(i);
								type = temp;
							}

							relation = "relation";
							positionOfRelation.add(allOfPosition.get(i));

							if (temp.equals("XSV")) {
								postposition = "yes";
							}
						} else {
							lemma = allOfLemma.get(i);
							type = temp;
							relation = "relation";
							positionOfRelation.add(allOfPosition.get(i));
						}

					} else if (temp.equals("VCP")) {

						String postType = allOfType.get(i + 1);

						lemma = allOfLemma.get(i);
						type = temp;

						if (postType.equals("ETM")) {
							relation = "other";
						} else {
							relation = "relation";
							positionOfRelation.add(allOfPosition.get(i));
						}
					}
				}

				trainingData = "'" + lemma + "','" + type + "'," + postposition + "," + relation;
				allOfTrainingSet.add(trainingData);
			}

			for (int i = removePoint.size() - 1; i > 0; i--) {
				int remove = removePoint.get(i);
				allOfTrainingSet.remove(remove);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allOfTrainingSet;
	}

	public ArrayList<String> extractArguments(String extractedPOS) {

		ArrayList<String> allOfLemma = new ArrayList<String>();
		ArrayList<String> allOfType = new ArrayList<String>();
		ArrayList<Long> allOfPosition = new ArrayList<Long>();
		ArrayList<String> allOfTrainingSet = new ArrayList<String>();
		ArrayList<Integer> removePoint = new ArrayList<Integer>();
		ArrayList<Integer> indexOfJ = new ArrayList<Integer>();
		ArrayList<Integer> indexOfV = new ArrayList<Integer>();
		ArrayList<Integer> indexOfE = new ArrayList<Integer>();
		ArrayList<Integer> indexOfChunk = new ArrayList<Integer>();

		try {

			JSONParser jsonParser = new JSONParser();

			JSONObject jsonObject = (JSONObject) jsonParser.parse(extractedPOS);

			JSONArray jsonSentence = (JSONArray) jsonObject.get("sentence");

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

				if (temp.equals("JKS") || temp.equals("JKC") || temp.equals("JKG") || temp.equals("JKO")
						|| temp.equals("JKB") || temp.equals("JKV") || temp.equals("JKQ") || temp.equals("JX")
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

				} else if (temp.equals("EF") || temp.equals("EP") || temp.equals("EC") || temp.equals("ETN")
						|| temp.equals("ETM") || temp.equals("SF") || temp.equals("SE") || temp.equals("VX")
						|| temp.equals("VA")) {

					if (temp.equals("ETM") && allOfLemma.get(i).equals("ㄴ") && allOfType.get(i - 1).equals("VCP")) {

					} else {
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
					sentenceLength = String.valueOf(allOfPosition.get(allOfPosition.size() - 1));
					startPosition = String.valueOf(allOfPosition.get(startPoint));
					endPosition = String.valueOf(allOfPosition.get(positionOfChunk) - 1);
					postLemma = allOfLemma.get(positionOfChunk);
					postType = allOfType.get(positionOfChunk);

					if (!lemma.equals("") && !postType.equals("NNG")) {

						if (postLemma.equals("은") || postLemma.equals("는") || postLemma.equals("이")
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

						trainingData = "'" + lemma + "','" + type + "'," + sentenceLength + "," + startPosition + ","
								+ endPosition + ",'" + postLemma + "','" + postType + "'," + subject;

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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allOfTrainingSet;
	}

	public BufferedWriter filebw = null;

	public void writeRelationTestingFile(ArrayList<String> relationFeatures) {
		try {
			filebw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testing/relation.arff")));

			filebw.write("@RELATION relation" + "\n" + "@ATTRIBUTE lemma	string" + "\n" + "@ATTRIBUTE pos 	string"
					+ "\n" + "%조사 + 접미사 = postposition" + "\n" + "@ATTRIBUTE postposition 	{yes,no}" + "\n"
					+ "@ATTRIBUTE class 	{relation,other}" + "\n" + "@DATA" + "\n");
			for (int i = 0; i < relationFeatures.size(); i++) {
				filebw.write(relationFeatures.get(i) + "\n");
			}

			filebw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeArgumentTestingFile(ArrayList<String> argumentFeatures) {
		try {
			filebw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testing/arguments.arff")));

			filebw.write("@RELATION subject" + "\n" + "@ATTRIBUTE lemma	string" + "\n" + "@ATTRIBUTE pos 	string"
					+ "\n" + "@ATTRIBUTE sentencelength 	numeric" + "\n" + "@ATTRIBUTE startposition 	numeric"
					+ "\n" + "@ATTRIBUTE endposition 	numeric" + "\n" + "@ATTRIBUTE postlemma 	string" + "\n"
					+ "@ATTRIBUTE postpos 	string" + "\n" + "@ATTRIBUTE class 	{subject,object}" + "\n" + "@DATA"
					+ "\n");
			for (int i = 0; i < argumentFeatures.size(); i++) {
				filebw.write(argumentFeatures.get(i) + "\n");
			}

			filebw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Instances relationTraining = null;
	public Instances argumentTraining = null;
	public Instances relationTrainingSet = null;
	public Instances argumentTrainingSet = null;
	public StringToWordVector relationFilter = null;
	public StringToWordVector argumentFilter = null;
	public Classifier relationClassifier = null;
	public Classifier argumentClassifier = null;

	public void relationLearning() {
		try {

			// 6.read training
			ArffLoader loaderTraining = new ArffLoader();
			loaderTraining.setFile(new File("training/relation.arff"));
			relationTraining = loaderTraining.getDataSet();
			relationTraining.setClassIndex(relationTraining.numAttributes() - 1);

			// 7.preprocess strings (almost no classifier supports them)
			relationFilter = new StringToWordVector();
			relationTrainingSet = relationTraining;
			relationFilter.setInputFormat(relationTrainingSet);
			relationTrainingSet = Filter.useFilter(relationTrainingSet, relationFilter);

			// 8.build classifier
			relationClassifier = new J48();
			relationClassifier.buildClassifier(relationTrainingSet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void argumentLearning() {
		try {

			// 6.read training
			ArffLoader loaderTraining = new ArffLoader();
			loaderTraining.setFile(new File("training/arguments.arff"));
			argumentTraining = loaderTraining.getDataSet();
			argumentTraining.setClassIndex(argumentTraining.numAttributes() - 1);

			// 7.preprocess strings (almost no classifier supports them)
			argumentFilter = new StringToWordVector();
			argumentTrainingSet = argumentTraining;
			argumentFilter.setInputFormat(argumentTrainingSet);
			argumentTrainingSet = Filter.useFilter(argumentTrainingSet, argumentFilter);

			// 8.build classifier
			argumentClassifier = new J48();
			argumentClassifier.buildClassifier(argumentTrainingSet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> relationClassify() {
		lemmaOfRelation.clear();
		try {

			// 6-1.read testing
			ArffLoader loaderTesting = new ArffLoader();
			loaderTesting.setFile(new File("testing/relation.arff"));
			Instances dataTesting = loaderTesting.getDataSet();
			dataTesting.setClassIndex(dataTesting.numAttributes() - 1);

			Instances dataTestingSet = dataTesting;
			dataTestingSet = Filter.useFilter(dataTestingSet, relationFilter);

			// //9.save classifier
			// OutputStream os = new FileOutputStream(file);
			// ObjectOutputStream objectOutputStream = new
			// ObjectOutputStream(os);
			// objectOutputStream.writeObject(classifier);
			//
			// //10. read classifier back
			// InputStream is = new FileInputStream(file);
			// ObjectInputStream objectInputStream = new ObjectInputStream(is);
			// classifier = (Classifier) objectInputStream.readObject();
			// objectInputStream.close();

			// 12.classify
			// result
			for (int i = 0; i < dataTestingSet.numInstances(); i++) {

				Instance inst = dataTesting.instance(i);

				if (relationClassifier.classifyInstance(dataTestingSet.instance(i)) == 0) {

					lemmaOfRelation.add(inst.stringValue(0));
					// positionOfRelation이 여기에 포함되어야 함

//					System.out.println("relation: " + inst.stringValue(0));
					classifiedRelations.add(inst.stringValue(0));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	ArrayList<String> lemmaOfSubject = new ArrayList<String>();
	ArrayList<ObjectTuple> lemmaOfObject = new ArrayList<ObjectTuple>();
	ArrayList<String> lemmaOfRelation = new ArrayList<String>();

	ArrayList<String> positionOfSubject = new ArrayList<String>();
	ArrayList<String> positionOfObject = new ArrayList<String>();
	ArrayList<String> positionOfRelation = new ArrayList<String>();
	private Scanner scan;

	public void argumentClassify() {

		lemmaOfSubject.clear();
		lemmaOfObject.clear();
		positionOfSubject.clear();
		positionOfObject.clear();
		try {

			ArgumentWekaWrapper classifier = new ArgumentWekaWrapper();

			// 6-1.read testing
			ArffLoader loaderTesting = new ArffLoader();
			loaderTesting.setFile(new File("testing/arguments.arff"));
			Instances dataTesting = loaderTesting.getDataSet();
			dataTesting.setClassIndex(dataTesting.numAttributes() - 1);

			// 7.preprocess strings (almost no classifier supports them)
			Instances dataTestingSet = dataTesting;
			dataTestingSet = Filter.useFilter(dataTestingSet, argumentFilter);

			// 12.classify
			// result

			for (int i = 0; i < dataTestingSet.numInstances(); i++) {
				Instance inst = null;

				if (classifier.classifyInstance(dataTestingSet.instance(i)) == 0) {

					inst = dataTesting.instance(i);
					lemmaOfSubject.add(inst.stringValue(0));

					positionOfSubject.add(String.valueOf(inst.value(4)));

//					System.out.println("subject: " + inst.stringValue(0));
					classifiedArguments1.add(inst.stringValue(0));
				} else {

					inst = dataTesting.instance(i);
					lemmaOfObject.add(new ObjectTuple(inst.stringValue(0), inst.stringValue(5)));
					positionOfObject.add(String.valueOf(inst.value(4)));

//					System.out.println("object: " + inst.stringValue(0) + " + " + inst.stringValue(5));
					classifiedArguments2.add(inst.stringValue(0));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Triple> makeSRDF() {

		// for (int s = 0; s < positionOfSubject.size(); s++) {
		// System.out.println(lemmaOfSubject.get(s));
		// System.out.println(positionOfSubject.get(s));
		// }
		//
		// for (int r = 0; r < positionOfRelation.size(); r++) {
		// System.out.println(lemmaOfRelation.get(r));
		// System.out.println(positionOfRelation.get(r));
		// }
		//
		// for (int o = 0; o < positionOfObject.size(); o++) {
		// System.out.println(lemmaOfObject.get(o).getObject() + "+"
		// + lemmaOfObject.get(o).getPostposition());
		// System.out.println(positionOfObject.get(o));
		// }

		ArrayList<Triple> srdf = new ArrayList<Triple>();

		ArrayList<PositionTuple> sbjStack = new ArrayList<PositionTuple>();
		ArrayList<PositionTuple> objStack = new ArrayList<PositionTuple>();
		ArrayList<PositionTuple> relStack = new ArrayList<PositionTuple>();

		for (int i = 0; i < lemmaOfSubject.size(); i++) {
			sbjStack.add(new PositionTuple(lemmaOfSubject.get(i), positionOfSubject.get(i)));
		}

		for (int i = 0; i < lemmaOfRelation.size(); i++) {
			relStack.add(new PositionTuple(lemmaOfRelation.get(i), positionOfRelation.get(i)));
		}

		for (int i = 0; i < lemmaOfObject.size(); i++) {
			objStack.add(new PositionTuple(lemmaOfObject.get(i), positionOfObject.get(i)));
		}

		Triple t = null;
		boolean vcpReification = false;
		boolean relReification = false;
		boolean objReification = false;
		boolean reification = false;
		String postLemma = "";

		if (sbjStack.size() != 0) {

			for (int i = sbjStack.size() - 1; i >= 0; i--) {
				PositionTuple subject = sbjStack.get(i);
				String sbjLemma = subject.getLemma();
				// double sbjPosition =
				// Double.parseDouble(subject.getPosition());

				for (int j = relStack.size() - 1; j >= 0; j--) {
					PositionTuple relation = relStack.get(j);
					String relLemma = relation.getLemma();
					int relPosition = (int) Double.parseDouble(relation.getPosition());
					int preRelPosition = 0;

					if (j > 0) {
						PositionTuple preRelation = relStack.get(j - 1);
						// String preRelLemma = relation.getLemma();
						preRelPosition = (int) Double.parseDouble(preRelation.getPosition());
					} else {
						preRelPosition = 0;
					}

					vcpReification = false;
					reification = false;

					for (int k = objStack.size() - 1; k >= 0; k--) {
						PositionTuple object = objStack.get(k);
						ObjectTuple objTuple = object.getOt();
						int objPosition = (int) Double.parseDouble(object.getPosition());

						String objPost = objTuple.getPostposition();
						String objLemma = objTuple.getObject();

						if (relPosition > objPosition && preRelPosition < objPosition) {

							if (relLemma.equals("이")) {
								if (!vcpReification) {
									t = new Triple(sbjLemma, relLemma, objLemma, objPosition + relPosition);
									srdf.add(t);

									vcpReification = true;
								} else {
									t = new Triple(relLemma, objPost, objLemma, objPosition + relPosition);

									srdf.add(t);

								}

							} else {

								if (objReification) {
									if (objPost.equals("을") || objPost.equals("를")) {
										t = new Triple(postLemma, relLemma, objLemma, objPosition + relPosition);
										srdf.add(t);
										reification = true;
									} else {
										if (!reification) {
											t = new Triple(postLemma, relLemma, "ANONYMOUS", objPosition + relPosition);
											srdf.add(t);
											reification = true;
										}
										t = new Triple(relLemma, objPost, objLemma, objPosition + relPosition);
										srdf.add(t);
									}

									objReification = false;

								} else {
									if (objPost.equals("을") || objPost.equals("를")) {
										t = new Triple(sbjLemma, relLemma, objLemma, objPosition + relPosition);
										srdf.add(t);
										reification = true;
									} else {
										if (!reification) {
											t = new Triple(sbjLemma, relLemma, "ANONYMOUS", objPosition + relPosition);
											srdf.add(t);
										}
										t = new Triple(relLemma, objPost, objLemma, objPosition + relPosition);
										srdf.add(t);
									}
								}

							}

							relReification = true;

						} else {

							// relation 연속 2번 나올 경우
							if (!relReification) {
								t = new Triple(sbjLemma, relLemma, "ANONYMOUS", objPosition + relPosition);
								srdf.add(t);
								objReification = true;
								relReification = true;
								postLemma = relLemma;
							}

						}

					}

				}
			}
		}

		return srdf;
	}

	public String run(String text) {

		String result = "";
		// String ardetection = "==Relations==\n";
		String ardetection = "==\n";

		String sentence = text;
		String resultOfETRI = execETRI(sentence);
		String extractedPOS = extractPOS(resultOfETRI);
		// System.out.println(extractedPOS);
		ArrayList<String> relationFeatures = extractRelations(extractedPOS);
		ArrayList<String> argumentFeatures = extractArguments(extractedPOS);

		writeRelationTestingFile(relationFeatures);
		relationClassify();

		writeArgumentTestingFile(argumentFeatures);
		argumentClassify();
		ArrayList<Triple> results = makeSRDF();
		ArrayList<Triple> uniqueResults = new ArrayList<Triple>();

		// HashSet<Triple> hs = new HashSet<Triple>(results);
		// Iterator<Triple> it = hs.iterator();
		// while (it.hasNext()) {
		// uniqueResults.add((Triple) it.next());
		// }

		if (results != null) {
			for (int i = 0; i < results.size(); i++) {
				if (!uniqueResults.contains(results.get(i))) {
					uniqueResults.add(results.get(i));
				}
			}
		}

		Collections.sort(uniqueResults, new TripleDescCompare());

		for (int i = 0; i < uniqueResults.size(); i++) {
			// System.out.println(uniqueResults.get(i).show());
			result += uniqueResults.get(i).show();
			if (i < uniqueResults.size() - 1) {
				result += "\n";
			}
		}

		for (int i = 0; i < classifiedRelations.size(); i++) {
			// System.out.print(classifiedRelations.get(i));
			ardetection += classifiedRelations.get(i);
			if (i < classifiedRelations.size() - 1) {
				ardetection += "\n";
			}
		}

		// ardetection += "\n==Argument1==\n";
		ardetection += "\n==\n";

		for (int i = 0; i < classifiedArguments1.size(); i++) {
			// System.out.print(classifiedArguments1.get(i));
			ardetection += classifiedArguments1.get(i);
			if (i < classifiedArguments1.size() - 1) {
				ardetection += "\n";
			}
		}

		// ardetection += "\n==Argument2==\n";
		ardetection += "\n==\n";

		for (int i = 0; i < classifiedArguments2.size(); i++) {
			// System.out.print(classifiedArguments2.get(i));
			ardetection += classifiedArguments2.get(i);
			if (i < classifiedArguments2.size() - 1) {
				ardetection += "\n";
			}
		}

//		System.out.println(extractedPOS + "\n" + ardetection + "\n==\n" + result);
		System.out.println(result);

		// return extractedPOS + "\n" + ardetection + "\n==SRDF==\n" + result;
//		return extractedPOS + "\n" + ardetection + "\n==\n" + result;
		return result;
	}

	public SRDF() {
		argumentLearning();
		relationLearning();
	}

	public static void main(String[] ar) {
		SRDF srdf = new SRDF();

		while (true) {
			String sentence = srdf.inputSentence();
			srdf.run(sentence);

		}
	}

	static class TripleDescCompare implements Comparator<Triple> {

		/**
		 * 내림차순(DESC)
		 */
		@Override
		public int compare(Triple arg0, Triple arg1) {
			// TODO Auto-generated method stub
			return arg0.getPosition() > arg1.getPosition() ? -1 : arg0.getPosition() < arg1.getPosition() ? 1 : 0;
		}

	}
}
