package projet.tri;

import java.util.Date;
import java.util.Objects;

public class Depot {
	private int poidsDechet;
	private int pointsAttribues;
	private Date dateDepot;
	
	
	public Depot () {}
	
	
	public Depot(int poidsDechet, int pointsAttribues, Date dateDepot) {
		super();
		this.poidsDechet = poidsDechet;
		this.pointsAttribues = pointsAttribues;
		this.dateDepot = dateDepot;
	}
	
	public int getPoidsDechet() {
		return poidsDechet;
	}
	
	public void setPoidsDechet(int poidsDechet) {
		this.poidsDechet = poidsDechet;
	}
	
	public int getPointsAttribues() {
		return pointsAttribues;
	}
	
	public void setPointsAttribues(int pointsAttribues) {
		this.pointsAttribues = pointsAttribues;
	}
	
	public Date getDateDepot() {
		return dateDepot;
	}
	
	public void setDateDepot(Date dateDepot) {
		this.dateDepot = dateDepot;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(dateDepot, poidsDechet, pointsAttribues);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Depot other = (Depot) obj;
		return Objects.equals(dateDepot, other.dateDepot) && poidsDechet == other.poidsDechet
				&& pointsAttribues == other.pointsAttribues;
	}
	
	
	
}
