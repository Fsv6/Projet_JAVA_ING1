package projet.tri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Compte {
    private int codeAcces;
    private int nbPointsFidelite;
    private int id;
    private String nom;
    private String prenom;

    private List<Depot> historiqueDepot = new ArrayList<>();

    // Constructeur à vérifier
    public Compte() {}

    public Compte(int id, String nom, String prenom, int codeAcces, int nbPointsFidelite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.codeAcces = codeAcces;
        this.nbPointsFidelite = nbPointsFidelite;
    }
    
    

    public int getCodeAcces() {
		return codeAcces;
	}

	public void setCodeAcces(int codeAcces) {
		this.codeAcces = codeAcces;
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

	public List<Depot> getHistoriqueDepot() {
		return historiqueDepot;
	}

	public void setHistoriqueDepot(List<Depot> historiqueDepot) {
		this.historiqueDepot = historiqueDepot;
	}

	// Méthodes supplémentaires
    public void realiserDepot(Depot depot) {
        historiqueDepot.add(depot);
        //nbPointsFidelite += depot.getPointsAttribues();
    }

    public List<Depot> voirHistoriqueDepot() {
        return historiqueDepot;
    }

    public void convertirEnBonAchat() {
        // Implémentation à compléter plus tard
        System.out.println("Conversion en bon d'achat (non implémentée).");
    }

    public void faireAuthentification() {
        System.out.println("Authentification réalisée pour le compte : " + nom);
    }

    public void utiliserBonAchat() {
        System.out.println("Bon d'achat utilisé (non implémenté).");
    }

	@Override
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
	}
    
}
