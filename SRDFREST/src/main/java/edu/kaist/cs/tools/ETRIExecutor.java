package main.java.edu.kaist.cs.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ETRIExecutor {
	public String getResult(String input) throws Exception {
		StringBuffer sb = new StringBuffer();

		InetAddress ia = null;

		
		//60, 187
		String serverIp = "143.248.135.187";

		try {
			ia = InetAddress.getByName(serverIp);
			Socket soc = new Socket(ia, 10010);

			// 문장을 서버로 보냄
			// 소켓의 출력 스트림
			OutputStream os = soc.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);

			bos.write((input).getBytes()); // input+"\n"
			bos.flush();

			// ETRI 처리 결과를 서버로 부터 받음
			// 소켓의 입력 스트림
			InputStream is = soc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				line = line.trim();
				if (line.equals(""))
					continue;

				sb.append(line);
				sb.append("\n");
			}
			bos.close();
			br.close();

		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		}

		return sb.toString();
	}

	public ArrayList<String> readFile() {
		ArrayList<String> bufList = new ArrayList<String>();
		String buf;
		try {
			BufferedReader filebr = new BufferedReader(
					new FileReader(
							"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_training_set.txt"));

			// "C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_training_set.txt"
			// "D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_training_set.txt"

			while ((buf = filebr.readLine()) != null) {
				bufList.add(buf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufList;
	}

	public static void main(String[] args) {
		ETRIExecutor ex = new ETRIExecutor();
		
		try {
			String output = ex.getResult("2012년 12월 19일 실시된 제18대 대선에서 51.6%의 득표율로 박근혜는 민주통합당의 문재인을 누르고 당선되었다.");
			System.out.println(output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ArrayList<String> inputSet = ex.readFile();
//		try {
//
//			BufferedWriter filebw = null;
//
//			for (int i = 0; i < inputSet.size(); i++) {
//
//				filebw = new BufferedWriter(new OutputStreamWriter(
//						new FileOutputStream(
//								"C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\Wiki_ETRI\\Wiki_ETRI"
//								 + i + ".txt"), "UTF8"));
//
//				// "D:\\KAIST\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI\\NLQ_ETRI"
//				// + i + ".txt"
//				// "C:\\Users\\sangha\\Dropbox\\KAIST\\진행중인 연구\\SentenceRDF\\data\\NLQ_ETRI\\NLQ_ETRI"
//				// + i + ".txt"
//
//				String output = ex.getResult(inputSet.get(i));
//				filebw.write(output);
//				filebw.close();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
}