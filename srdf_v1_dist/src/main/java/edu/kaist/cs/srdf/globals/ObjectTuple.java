package main.java.edu.kaist.cs.srdf.globals;

public class ObjectTuple {
	String object;
	String postposition;
	int objectPosition;
	int postpositionPosition;

	public int getObjectPosition() {
		return objectPosition;
	}

	public void setObjectPosition(int objectPosition) {
		this.objectPosition = objectPosition;
	}

	public int getPostpositionPosition() {
		return postpositionPosition;
	}

	public void setPostpositionPosition(int postpositionPosition) {
		this.postpositionPosition = postpositionPosition;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getPostposition() {
		return postposition;
	}

	public void setPostposition(String postposition) {
		this.postposition = postposition;
	}

	public ObjectTuple(String o, String p) {
		this.object = o;
		this.postposition = p;
	}
	
	public ObjectTuple(String o, String p, int op, int pp) {
		this.object = o;
		this.postposition = p;
		this.objectPosition = op;
		this.postpositionPosition = pp;
	}

}
