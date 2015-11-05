package main.java.edu.kaist.cs.globals;

public class FactorOfVP {
	String argRel;
	String lemma;
	String postposition;
	String type;
	String position;

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

	public String getArgRel() {
		return argRel;
	}

	public void setArgRel(String argRel) {
		this.argRel = argRel;
	}

	public String getPostposition() {
		return postposition;
	}

	public void setPostposition(String postposition) {
		this.postposition = postposition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FactorOfVP(String a, String p, String t, String position) {
		this.argRel = a;
		this.postposition = p;
		this.type = t;
		this.position = position;
	}

	public FactorOfVP(String a, String l, String p, String t, String position) {
		this.argRel = a;
		this.lemma = l;
		this.postposition = p;
		this.type = t;
		this.position = position;
	}

	public String show() {
		return "[" + argRel + "/" + lemma + "/" + postposition + "/" + type
				+ "]";
	}
	public String showAll() {
		return "[" + argRel + "/" + lemma + "/" + postposition + "/" + type
				+ "/" + position + "]";
	}
	
}
