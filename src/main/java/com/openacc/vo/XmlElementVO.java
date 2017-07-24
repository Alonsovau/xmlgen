package com.openacc.vo;

public class XmlElementVO {

	String seqno;
	String processid;
	String processdate;
	String processtype;
	String clienttype;
	String clientregion;
	String nationality;
	String excompanyid;
	String exclientidtype;
	String exclientid;
	String clientname;
	String idtype;
	String id_transformed;
	String classify;
	String option_id;
	String nocid;
	String exclientidrefname;
	String clientname_eng;
	String nocid_extracode;
	String department_code;
	String clientnum;
	String xmlnum;

	public String getSeqno() {
		return seqno;
	}

	public long getSeqnoInt(){
		try{
			return Long.valueOf(seqno);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}	
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getProcessid() {
		return processid;
	}

	public long getProcessidInt(){
		try{
			return Long.valueOf(processid);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}	
	
	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public String getProcessdate() {
		return processdate;
	}

	public void setProcessdate(String processdate) {
		this.processdate = processdate;
	}

	public String getProcesstype() {
		return processtype;
	}

	public void setProcesstype(String processtype) {
		this.processtype = processtype;
	}

	public String getClienttype() {
		return clienttype;
	}

	public void setClienttype(String clienttype) {
		this.clienttype = clienttype;
	}

	public String getClientregion() {
		return clientregion;
	}

	public void setClientregion(String clientregion) {
		this.clientregion = clientregion;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getExcompanyid() {
		return excompanyid;
	}

	public void setExcompanyid(String excompanyid) {
		this.excompanyid = excompanyid;
	}

	public String getExclientidtype() {
		return exclientidtype;
	}

	public void setExclientidtype(String exclientidtype) {
		this.exclientidtype = exclientidtype;
	}

	public String getExclientid() {
		return exclientid;
	}

	public void setExclientid(String exclientid) {
		this.exclientid = exclientid;
	}

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getNocid() {
		return nocid;
	}
	
	public long getNocidInt(){
		try{
			return Long.valueOf(nocid);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}	
	
	public void setNocid(String nocid) {
		this.nocid = nocid;
	}
	
	public String getId_transformed() {
		return id_transformed;
	}
	
	public long getId_transformedInt(){
		try{
			return Long.valueOf(id_transformed);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}	
	
	public void setId_transformed(String id_transformed) {
		this.id_transformed = id_transformed;
	}	

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getOption_id() {
		return option_id;
	}

	public void setOption_id(String option_id) {
		this.option_id = option_id;
	}
	public String getExclientidrefname() {
		return exclientidrefname;
	}

	public void setExclientidrefname(String exclientidrefname) {
		this.exclientidrefname = exclientidrefname;
	}

	public String getClientname_eng() {
		return clientname_eng;
	}

	public void setClientname_eng(String clientname_eng) {
		this.clientname_eng = clientname_eng;
	}

	public String getNocid_extracode() {
		return nocid_extracode;
	}

	public void setNocid_extracode(String nocid_extracode) {
		this.nocid_extracode = nocid_extracode;
	}

	public String getDepartment_code() {
		return department_code;
	}

	public void setDepartment_code(String department_code) {
		this.department_code = department_code;
	}
	public String getClientnum() {
		return clientnum;
	}
	
	public int getClientnumInt(){
		try{
			return Integer.valueOf(clientnum);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}	

	public void setClientnum(String clientnum) {
		this.clientnum = clientnum;
	}

	public String getXmlnum() {
		return xmlnum;
	}
	
	public int getXmlnumInt(){
		try{
			return Integer.valueOf(xmlnum);
		}catch(Exception e){
			//null
			//默认数是1
			return 1;
		}
	}

	public void setXmlnum(String xmlnum) {
		this.xmlnum = xmlnum;
	}

}
