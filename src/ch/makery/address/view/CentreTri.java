package ch.makery.address.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.time.LocalDate;

public class CentreTri {

	private int idCentreTri;
	private String nomCentreTri;
	private int numRue;
	private String nomRue;
	private String ville;
	private int codePostal;
	private List<PoubelleIntelligente> poubelles = new ArrayList<>();
	private List<Contrat> contrats = new ArrayList<>();

	public CentreTri(int idCentreTri, String nomCentreTri, int numRue, String nomRue, String ville, int codePostal) {
		this.idCentreTri = idCentreTri;
		this.nomCentreTri = nomCentreTri;
		this.numRue = numRue;
		this.nomRue = nomRue;
		this.ville = ville;
		this.codePostal = codePostal;
	}

	public int getIdCentreTri() {
		return idCentreTri;
	}

	public String getNomCentreTri() {
		return nomCentreTri;
	}

	public int getNumRue() {
		return numRue;
	}

	public String getNomRue() {
		return nomRue;
	}

	public String getVille() {
		return ville;
	}

	public int getCodePostal() {
		return codePostal;
	}

	public List<PoubelleIntelligente> getPoubelles() {
		return poubelles;
	}

	public List<Contrat> getContrats() {
		return contrats;
	}
	public void ajouterPoubelle(PoubelleIntelligente poubelle) {
		poubelles.add(poubelle);
	}

	public void retirerPoubelle(PoubelleIntelligente poubelle) {
		poubelles.remove(poubelle);
	}

public void afficherStatistiques() {

	}

	public void viderBac(PoubelleIntelligente poubelle, Bac bac) {
		if (poubelles.contains(poubelle) && poubelle.getBacs().contains(bac)) {
			bac.vider();
			System.out.println("Bac " + bac.getTypesDechets() + " vidé dans la poubelle #" + poubelle.getId() + ".");
		} else {
			System.out.println("Erreur : bac ou poubelle non reconnu par ce centre de tri.");
		}
	}

	public void signerContrat(Commerce commerce, LocalDate dateDebut, LocalDate dateFin, List<String> listeCatProduits) {
		Contrat nouveauContrat = new Contrat(dateDebut, dateFin, listeCatProduits);
		commerce.ajouterContrat(nouveauContrat);
		this.contrats.add(nouveauContrat);
		System.out.println("Contrat signé avec le commerce : " + commerce.getNom());
	}

	public void renouvelerContrat(int idContrat, LocalDate nouvelleDateFin) {
		for (Contrat c : contrats) {
			if (c.getId() == idContrat) {
				c.setDateFin(nouvelleDateFin);
				System.out.println("Contrat #" + idContrat + " renouvelé jusqu'au " + nouvelleDateFin);
				return;
			}
		}
		System.out.println("Erreur : contrat avec ID " + idContrat + " non trouvé.");
	}

	public void resilierContrat(int idContrat) {
		Iterator<Contrat> it = contrats.iterator();
		while (it.hasNext()) {
			Contrat c = it.next();
			if (c.getId() == idContrat) {
				it.remove();
				System.out.println("Contrat #" + idContrat + " résilié.");
				return;
			}
		}
		System.out.println("Erreur : contrat avec ID " + idContrat + " non trouvé.");
	}

	public void afficherContrats() {
		System.out.println("Contrats associés au centre de tri " + this.nomCentreTri + " :");
		for (Contrat contrat : contrats) {
			System.out.println("- Contrat #" + contrat.getId() + " du " + contrat.getDateDebut() +
					" au " + contrat.getDateFin() + " | Produits : " + contrat.getListeCatProduits());
		}
	}

}
