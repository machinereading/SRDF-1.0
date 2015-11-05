package main.java.edu.kaist.cs.globals;

public class ObjectTuple {
	String object;
	String postposition;

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
	

}
