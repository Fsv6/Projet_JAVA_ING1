package tri.logic;

import java.util.ArrayList;
import java.util.List;

public class Compte {

	private int nbPointsFidelite;
    private int id;
    private String nom;
    private String prenom;
    private List<Depot> historiqueDepot = new ArrayList<>();
	private List<BonAchat> listBonAchat = new ArrayList<>();

	public Compte() {}

    public Compte(int id, String nom, String prenom, int codeAcces, int nbPointsFidelite) {
        this.id = id; //automatiser la création de l'id sans le mettre en paramètre du constructeur
        this.nom = nom;
        this.prenom = prenom;
		this.nbPointsFidelite = nbPointsFidelite;
		this.historiqueDepot = new ArrayList<>();
		this.listBonAchat = new ArrayList<>();
    }


	public int getNbPointsFidelite() {
		return nbPointsFidelite;
	}

	public void setNbPointsFidelite(int nbPointsFidelite) {
		this.nbPointsFidelite = nbPointsFidelite;
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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setHistoriqueDepot(ArrayList<Depot> historiqueDepot) {
		this.historiqueDepot = historiqueDepot;
	}

	public List<Depot> getHistoriqueDepot() {
		return historiqueDepot;
	}

	public List<BonAchat> getListBonAchat() {
		return listBonAchat;
	}
	public void setListBonAchat(List<BonAchat> listBonAchat) {
		this.listBonAchat = listBonAchat;
	}

	public void realiserDepot(Depot depot, PoubelleIntelligente poubelle, Bac bac) {

		poubelle.attribuerPoint(depot, bac);
		historiqueDepot.add(depot);
		nbPointsFidelite += depot.getPointsAttribues();

		System.out.println("Dépôt effectué. Points gagnés : " + depot.getPointsAttribues() + ", total fidélité : " + nbPointsFidelite);
	}

	public void convertirEnBonAchat() {
		int seuilPoints = 60;
		int montantBon = 5;

		if (nbPointsFidelite >= seuilPoints) {
			nbPointsFidelite -= seuilPoints;
			BonAchat bon = new BonAchat(montantBon);
			listBonAchat.add(bon);
			System.out.println("Bon d'achat de " + montantBon + "€ généré. Points restants : " + nbPointsFidelite);
		} else {
			System.out.println("Vous n'avez pas assez de points pour convertir un bon d'achat.");
		}
	}
	public void utiliserBonAchat(BonAchat bonAchat, Commerce commerce, List<Produit> panier) {

		boolean bonValide = commerce.vérifierBonAchat(panier);

		if (bonValide) {
			listBonAchat.remove(bonAchat);
			System.out.println("Bon d'achat utilisé avec succès ! Réduction appliquée.");
		} else {
			System.out.println("Ce bon d'achat n'est pas valable pour les produits choisis.");
		}
	}

	/*@Override
	public int hashCode() {
		return Objects.hash(codeAcces, historiqueDepot, id, nbPointsFidelite, nom, prenom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compte other = (Compte) obj;
		return codeAcces == other.codeAcces && Objects.equals(historiqueDepot, other.historiqueDepot) && id == other.id
				&& nbPointsFidelite == other.nbPointsFidelite && Objects.equals(nom, other.nom)
				&& Objects.equals(prenom, other.prenom);
	}*/
    
}
