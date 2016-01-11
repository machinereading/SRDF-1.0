package main.java.edu.kaist.cs.srdf.weka.classifiers;

class RelationWekaClassifier {

	public static double classify(Object[] i) throws Exception {

		double p = Double.NaN;
		p = RelationWekaClassifier.N4f00398a0(i);
		return p;
	}

	static double N4f00398a0(Object[] i) {
		double p = Double.NaN;
		if (i[3] == null) {
			p = 1;
		} else if (((Double) i[3]).doubleValue() <= 0.0) {
			p = RelationWekaClassifier.Ndf924ed1(i);
		} else if (((Double) i[3]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}

	static double Ndf924ed1(Object[] i) {
		double p = Double.NaN;
		if (i[4] == null) {
			p = 1;
		} else if (((Double) i[4]).doubleValue() <= 0.0) {
			p = RelationWekaClassifier.N3aa62ad02(i);
		} else if (((Double) i[4]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}

	static double N3aa62ad02(Object[] i) {
		double p = Double.NaN;
		if (i[5] == null) {
			p = 1;
		} else if (((Double) i[5]).doubleValue() <= 0.0) {
			p = 1;
		} else if (((Double) i[5]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}
}