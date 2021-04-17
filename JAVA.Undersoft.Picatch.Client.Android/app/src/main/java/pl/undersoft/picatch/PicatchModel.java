package pl.undersoft.picatch;

public class PicatchModel {

	private String id = "";
	private String name = "";
	private String typ = "";
	private String data = "";
	private String send = "";

	public PicatchModel() {
	}

	public PicatchModel(String lId, String lName, String lTyp, String lData, String lSend) {
		id = lId;
		name = lName;
		typ = lTyp;
		data = lData;
		send = lSend;
	}

	public String getId() {
	    return id;
	}

	public void setId(String lId) {
	    id = lId;
	}

	public String getName() {
	    return name;
	}

	public void setName(String lName) {
	    name = lName;
	}

	public String getTyp() {
	    return typ;
	}

	public void setTyp(String lTyp) {
		typ = lTyp;
	}

	public String getData() {
	    return data;
	}

	public void setData(String lData) {
		data = lData;
	}

	public String getSend() {
	    return send;
	}

	public void setSend(String lSend) {
		send = lSend;
	}

}
