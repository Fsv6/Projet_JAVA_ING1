package ch.makery.address.view;

import java.util.ArrayList;
import java.util.Date;

public class Depot {

	private int idDepot;
	private int pointsAttribues;
	private Date dateDepot;
	private ArrayList<Dechet> listeDechet = new ArrayList<>();

	public Depot(String string, String string2, String string3, String string4, String string5) {}

	public Depot(int idDepot, int pointsAttribues, Date dateDepot, ArrayList<Dechet> listeDechet) {
		this.idDepot = idDepot;
		this.pointsAttribues = pointsAttribues;
		this.dateDepot = dateDepot;
		this.listeDechet = listeDechet;
	}

	public int getPoidsDechet() {
		int total = 0;
		for (Dechet d : listeDechet) {
			total += d.getPoids();
		}
		return total;
	}

	public int getIdDepot() {
		return idDepot;
	}

	public void setIdDepot(int idDepot) {
		this.idDepot = idDepot;
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

	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}


