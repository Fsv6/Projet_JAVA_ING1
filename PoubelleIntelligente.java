package Poubelle;

import java.util.ArrayList;
import java.util.List;

public class PoubelleIntelligente {
    private int id;
    private String nomQuartier;
    private float longitudeEmplacement;
    private float latitudeEmplacement;
    private List<Bac> bacs = new ArrayList<>();

    public void attribuerPoints() {
        // à voir
    }

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

    public void ajouterDepot(Bac bac) {
        if (bacs.size() < 4) {
            bacs.add(bac);
        } else {
            System.out.println("Nombre maximum de bacs atteint !");
        }
    }
}
