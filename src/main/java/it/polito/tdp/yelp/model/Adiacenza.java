package it.polito.tdp.yelp.model;

public class Adiacenza {
	
	private String bId1;
	private String bId2;
	private Double peso;
	
	public Adiacenza(String bId1, String bId2, Double peso) {
		super();
		this.bId1 = bId1;
		this.bId2 = bId2;
		this.peso = peso;
	}

	public String getbId1() {
		return bId1;
	}

	public void setbId1(String bId1) {
		this.bId1 = bId1;
	}

	public String getbId2() {
		return bId2;
	}

	public void setbId2(String bId2) {
		this.bId2 = bId2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Adiacenza [bId1=" + bId1 + ", bId2=" + bId2 + ", peso=" + peso + "]";
	}
	
	

}
