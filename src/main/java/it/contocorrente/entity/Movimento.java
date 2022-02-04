package it.contocorrente.entity;

public class Movimento {
	
	private String data;
	private String iban;
	private double importo;
	private Operazione tipo;
	
	
	
	//setter e getter
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public Operazione getTipo() {
		return tipo;
	}
	public void setTipo(Operazione tipo) {
		this.tipo = tipo;
	}
	
	
	

}
