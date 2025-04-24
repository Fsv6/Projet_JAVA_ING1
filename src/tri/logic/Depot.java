package tri.logic;

import java.util.ArrayList;
import java.util.Date;

public class Depot {

	private int pointsAttribues;
	private Date dateDepot;
	private ArrayList<Dechet> listeDechet = new ArrayList<>();

	public Depot() {}

	public Depot(int pointsAttribues, Date dateDepot, ArrayList<Dechet> listeDechet) {
		this.pointsAttribues = pointsAttribues;
		this.dateDepot = dateDepot;
		this.listeDechet = listeDechet;
	}

	public int getPoidsDechet() {
		// Calcul dynamique du poids total
		int total = 0;
		for (Dechet d : listeDechet) {
			total += d.getPoids();
		}
		return total;
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

	public ArrayList<Dechet> getListeDechet() {
		return listeDechet;
	}

	public void ajouterDechet(Dechet dechet) {
		this.listeDechet.add(dechet);
	}

	public void supprimerDechet(Dechet dechet) {
		this.listeDechet.remove(dechet);
	}
}

