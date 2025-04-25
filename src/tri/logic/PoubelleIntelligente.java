package Poubelle;

import java.util.ArrayList;
import java.util.List;

public class PoubelleIntelligente {
    private int id;
    private String nomQuartier;
    private float longitudeEmplacement;
    private float latitudeEmplacement;
    private List<Bac> bacs = new ArrayList<>();
	private int pointsDepot=0;

    public int getId() {
        return id;
    }

    public String getNomQuartier() {
        return nomQuartier;
    }

    public float getLatitudeEmplacement() {
        return latitudeEmplacement;
    }

    public float getLongitudeEmplacement() {
        return longitudeEmplacement;
    }

    public void setLatitudeEmplacement(float latitudeEmplacement) {
        this.latitudeEmplacement = latitudeEmplacement;
    }

    public void setLongitudeEmplacement(float longitudeEmplacement) {
        this.longitudeEmplacement = longitudeEmplacement;
    }

    public void notifierEtat() {
        // pas sur de comment faire
    }

    public void verifierDroitAcces() {
        // pas sur comment faire
    }


    public void vérifierBacPlein() {
        for (Bac bac : bacs) {
            if (bac.estPlein()) {
                System.out.println("Un bac est plein !");
            }
        }
    }

    public void ajouterDepot(Depot depot) {
        int pointsGagnes = 1;

        // ❗ Ici on récupère les déchets depuis le dépôt 
        for (Dechet dechet : depot.getListeDechet()) {
            for (Bac bac : bacs) {
                if (bac.getTypeDechet() == dechet.getType() &&
                    bac.getPoidsActuel() + dechet.getPoids() <= bac.getCapaciteMax()) {

                    bac.ajouterDechet(dechet.getPoids());
                    pointsGagnes += dechet.getPoids();
                    break;
                }
            }
        }

        this.pointsDepot += pointsGagnes;
        System.out.println("Points gagnés pour ce dépôt : " + pointsGagnes);
    }

	public int getPointsDepot() {
		return pointsDepot;
	}

	public void setPointsDepot(int pointsDepot) {
		this.pointsDepot = pointsDepot;
	}
}
