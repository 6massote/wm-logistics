package br.com.walmart.freight.models;

public class Freight {

	private Float amount;

	public Freight() {
	
	}
	
	public Freight(final Float amount) {
		this.setAmount(amount);
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
	public boolean isValid() {
		return new Float("0").compareTo(getAmount()) <= 0;
	}

}
