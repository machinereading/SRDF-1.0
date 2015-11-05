package main.java.edu.kaist.cs.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Chunk {

	public void chunker() {
		try {

			String buf = null;
			BufferedWriter filebw = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_training_set1.txt"),
							"UTF8"));
			;
			BufferedReader filebr = new BufferedReader(
					new FileReader(
							"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_training_set.txt"));

			while ((buf = filebr.readLine()) != null) {
				System.out.println(buf);
				StringTokenizer st = new StringTokenizer(buf);
				System.out.println(st.countTokens());
				if (st.countTokens() <= 10) {
					filebw.write(buf+"\n");
				}
			}

			filebr.close();
			filebw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] ar) {
		Chunk c = new Chunk();
		c.chunker();
	}
}
