package main.java.edu.kaist.cs.weka.classifiers;

class ArgumentWekaClassifier {

	public static double classify(Object[] i) throws Exception {

		double p = Double.NaN;
		p = ArgumentWekaClassifier.N35454cbc0(i);
		return p;
	}

	static double N35454cbc0(Object[] i) {
		double p = Double.NaN;
		if (i[56] == null) {
			p = 1;
		} else if (((Double) i[56]).doubleValue() <= 0.0) {
			p = ArgumentWekaClassifier.N743826091(i);
		} else if (((Double) i[56]).doubleValue() > 0.0) {
			p = ArgumentWekaClassifier.N44ec85472(i);
		}
		return p;
	}

	static double N743826091(Object[] i) {
		double p = Double.NaN;
		if (i[55] == null) {
			p = 1;
		} else if (((Double) i[55]).doubleValue() <= 0.0) {
			p = 1;
		} else if (((Double) i[55]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}

	static double N44ec85472(Object[] i) {
		double p = Double.NaN;
		if (i[1] == null) {
			p = 0;
		} else if (((Double) i[1]).doubleValue() <= 6.0) {
			p = ArgumentWekaClassifier.N7ede619b3(i);
		} else if (((Double) i[1]).doubleValue() > 6.0) {
			p = ArgumentWekaClassifier.N545c981b5(i);
		}
		return p;
	}

	static double N7ede619b3(Object[] i) {
		double p = Double.NaN;
		if (i[70] == null) {
			p = 0;
		} else if (((Double) i[70]).doubleValue() <= 0.0) {
			p = 0;
		} else if (((Double) i[70]).doubleValue() > 0.0) {
			p = ArgumentWekaClassifier.N6161e2504(i);
		}
		return p;
	}

	static double N6161e2504(Object[] i) {
		double p = Double.NaN;
		if (i[1050] == null) {
			p = 0;
		} else if (((Double) i[1050]).doubleValue() <= 0.0) {
			p = 0;
		} else if (((Double) i[1050]).doubleValue() > 0.0) {
			p = 1;
		}
		return p;
	}

	static double N545c981b5(Object[] i) {
		double p = Double.NaN;
		if (i[333] == null) {
			p = 0;
		} else if (((Double) i[333]).doubleValue() <= 0.0) {
			p = ArgumentWekaClassifier.N74baf96a6(i);
		} else if (((Double) i[333]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}

	static double N74baf96a6(Object[] i) {
		double p = Double.NaN;
		if (i[510] == null) {
			p = 1;
		} else if (((Double) i[510]).doubleValue() <= 0.0) {
			p = 1;
		} else if (((Double) i[510]).doubleValue() > 0.0) {
			p = 0;
		}
		return p;
	}
}
