package main.java.edu.kaist.cs.srdf.globals;

public class PositionTuple {
	String lemma;
	String position;
	ObjectTuple ot;

	public ObjectTuple getOt() {
		return ot;
	}

	public void setOt(ObjectTuple ot) {
		this.ot = ot;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public PositionTuple(String o, String p) {
		this.lemma = o;
		this.position = p;
	}
	
	public PositionTuple(ObjectTuple ot, String p) {
		this.ot = ot;
		this.position = p;
	}

}
