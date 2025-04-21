import tri.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CentreTri {
	
	private int id;
	private String nom;
	private int numRue;
	private String nomRue;
	private String ville;
	private int codePostal;
	
	private List<PoubelleIntelligente> poubelles = new ArrayList<>();
	private List<Contrat> contrats = new ArrayList<>();
	
	public void placerPoubelle(String nomQuartier, int longitude, int latitude) {
		PoubelleIntelligente nouvellePoubelle = new PoubelleIntelligente(nomQuartier, longitude, latitude);
		poubelles.add(nouvellePoubelle);
	}
	
	public void ajouterPoubelle(PoubelleIntelligente poubelleIntelligente) {
		poubelles.add(poubelleIntelligente);
	}
	
	public void retirerPoubelle(PoubelleIntelligente poubelleIntelligente) {
		if(poubelles.remove(poubelleIntelligente)) {
			System.out.println("Poubelle retirée ");
		}
		else {
			System.out.println("Poubelle non trouvée ");
		}
	}
	
	public void collecterDechets(Bac bac) {
		bac.vider();
	}
	public void faireStatistiques() {
		System.out.println("nombre de poubelles : " + poubelles.size());
		System.out.println("nombre de contrats : " + contrats.size());
	}
	public void signerContrat(LocalDate datedebut, LocalDate dateFin, List<String> listeCatProduits) {
		Contrat contrat = new Contrat(datedebut, dateFin, listeCatProduits);
		contrats.add(contrat);
	}
	public void renouvelerContrat(int idContrat, LocalDate nouvelleDateFin) {
		for (Contrat contrat : contrats) {
			if(contrat.getId() == idContrat) {
				contrat.setDateFin(nouvelleDateFin);
			}
		}
		System.out.println("Contrat non trouvé");
	}
	
	public void resilierContrat(int idContrat) {
		contrats.removeIf(contrat -> contrat.getId() == idContrat);
	}
	
	public void recevoirNotifications(PoubelleIntelligente poubelleIntelligente, Bac bac) {
		System.out.println("Notif reçue de la poubelle " + poubelleIntelligente.getId());
		collecterDechets(bac);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getNumRue() {
		return numRue;
	}
	
	public void setNumRue(int numRue) {
		this.numRue = numRue;
	}
	
	public String getNomRue() {
		return nomRue;
	}
	
	public void setNomRue(String nomRue) {
		this.nomRue = nomRue;
	}
	
	public String getVille() {
		return ville;
	}
	
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public int getCodePostal() {
		return codePostal;
	}
	
	public void setCodePostal(int codePostal) {
		this.codePostal = codePostal;
	}
	
	
	public List<PoubelleIntelligente> getPoubelles() {
	    return poubelles;
	}

	public List<Contrat> getContrats() {
	    return contrats;
	}

}
