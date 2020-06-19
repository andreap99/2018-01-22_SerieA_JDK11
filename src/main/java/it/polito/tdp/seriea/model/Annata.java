package it.polito.tdp.seriea.model;

public class Annata implements Comparable<Annata>{
	
	private Season stagione;
	private int punti;
	
	public Annata(Season stagione, int punti) {
		super();
		this.stagione = stagione;
		this.punti = punti;
	}

	public Season getStagione() {
		return stagione;
	}

	public int getPunti() {
		return punti;
	}

	public void addPunti(int p) {
		this.punti += p;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + punti;
		result = prime * result + ((stagione == null) ? 0 : stagione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Annata other = (Annata) obj;
		if (punti != other.punti)
			return false;
		if (stagione == null) {
			if (other.stagione != null)
				return false;
		} else if (!stagione.equals(other.stagione))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "stagione " + stagione.getDescription() + " punti=" + punti;
	}

	@Override
	public int compareTo(Annata other) {
		
		return this.stagione.compareTo(other.stagione);
	}
	
	
}
