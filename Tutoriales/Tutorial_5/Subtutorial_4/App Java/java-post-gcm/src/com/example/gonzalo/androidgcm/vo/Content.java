package com.example.gonzalo.androidgcm.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {

//	private List<String> registration_ids;
//	private Map<String, String> data;
	public List<String> registration_ids;
	public Map<String, String> data;
	
	public void addRegId(String regId) {
		if (registration_ids == null) {
			registration_ids = new LinkedList<String>();
		}//if
		registration_ids.add(regId);
	}
	
	public void createData(String title, String message) {
		if (data == null) {
			data = new HashMap<String, String>();
		}//if
		data.put("title", title);
		data.put("message", message);
	}

}
