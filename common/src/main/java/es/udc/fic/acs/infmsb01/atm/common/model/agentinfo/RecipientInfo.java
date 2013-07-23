package es.udc.fic.acs.infmsb01.atm.common.model.agentinfo;

import java.io.Serializable;

public class RecipientInfo  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	
	public RecipientInfo(String id) {
		this.id = id;
	}
	
	public RecipientInfo() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((RecipientInfo) obj).getId().equals(id);
	}
	
	@Override
	public String toString() {
		return id;
	}
	
//	public static void main(String [] args) {
//		HashMap<String, Integer> test = new HashMap<String, Integer>();
//
//		RecipientInfo ri1 = new RecipientInfo("test");
//		
//		test.put(ri1.getId(), 123);
//		RecipientInfo ri2 = new RecipientInfo("test");
//		
//		System.out.printf("%d", test.get(ri2.getId()));
//	}
	
}
