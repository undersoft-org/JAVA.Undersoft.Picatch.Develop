package pl.undersoft.picatch;

public class PicatchModelDetails {

	private String id = "";
	private String dokid = "";
	private String kod = "";
	private String nazwa = "";
	private String ilosc = "";
	private String stan = "";
	private String cenasp = "";

	public PicatchModelDetails() {
	}

	public PicatchModelDetails(String lId, String lDokid, String lKod, String lNazwa, String lIlosc, String lStan, String lCenasp) {
		id = lId;
		dokid = lDokid;
		kod = lKod;
		nazwa = lNazwa;
		ilosc = lIlosc;
		stan = lStan;
		cenasp = lCenasp;
	}

	public String getId() {
	    return id;
	}

	public void setId(String lId) {
	    id = lId;
	}

	public String getDokid() {
	    return dokid;
	}

	public void setDokid(String lDokid) {
		dokid = lDokid;
	}

	public String getKod() {
	    return kod;
	}

	public void setKod(String lKod) {
		kod = lKod;
	}

	public String getNazwa() {
	    return nazwa;
	}

	public void setNazwa(String lNazwa) {
		nazwa = lNazwa;
	}

	public String getIlosc() {
	    return ilosc;
	}

	public void setIlosc(String lIlosc) {
		ilosc = lIlosc;
	}

	public String getStan() {
	    return stan;
	}

	public void setStan(String lStan) {
		stan = lStan;
	}

	public String getCenasp() {
	    return cenasp;
	}

	public void setCenasp(String lCenasp) {
		cenasp = lCenasp;
	}

}

