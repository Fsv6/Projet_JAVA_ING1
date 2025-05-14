package ch.makery.address.view;

import tri.exception.BacPleinException;
import tri.exception.DechetNonConformeException;

import java.util.ArrayList;
import java.util.List;

public class PoubelleIntelligente {
    private int id;
    private String nomQuartier;
    private float longitudeEmplacement;
    private float latitudeEmplacement;
    private List<Bac> bacs = new ArrayList<>();

    public PoubelleIntelligente(int id, String nomQuartier, float latitude, float longitude) {
        this.id = id;
        this.nomQuartier = nomQuartier;
        this.latitudeEmplacement = latitude;
        this.longitudeEmplacement = longitude;
    }


    public List<String> ajouterDepot(Depot depot, Bac bac) {
        attribuerPoint(depot, bac);
        return notifierSiBacPlein(); // notifications prêtes à être captées par l'IHM
    }


    private void attribuerPoint(Depot depot, Bac bac) {
        int points = 0;

        for (Dechet dechet : depot.getListeDechet()) {
            if (bac.getTypesDechets().contains(dechet.getType())) {
                try {
                    bac.ajouterDechet(dechet.getPoids());
                    points += 3;
                } catch (BacPleinException e) {
                    System.out.println("Erreur : bac plein pour le déchet de type " + dechet.getType());
                }
            } else {
                bac.ajouterDechet(dechet.getPoids());
                points -= 2;
                depot.setPointsAttribues(points);
                throw new DechetNonConformeException("Erreur : le déchet de type " + dechet.getType() + " ne correspond pas aux types acceptés par ce bac.");
            }
        }

        depot.setPointsAttribues(points);
    }



    public List<String> notifierSiBacPlein() {
        List<String> messages = new ArrayList<>();
        for (Bac bac : bacs) {
            if (bac.estPlein()) {
                messages.add("Poubelle #" + id + " (" + nomQuartier + ") : le bac " +
                        bac.getTypesDechets() + " est plein.");
            }
        }
        return messages;
    }


    public int getId() { return id; }
    public String getNomQuartier() { return nomQuartier; }
    public float getLatitudeEmplacement() { return latitudeEmplacement; }
    public float getLongitudeEmplacement() { return longitudeEmplacement; }
    public List<Bac> getBacs() { return bacs; }
    public void setLatitudeEmplacement(float latitude) { this.latitudeEmplacement = latitude; }
    public void setLongitudeEmplacement(float longitude) { this.longitudeEmplacement = longitude; }

    public void ajouterBac(Bac bac) { bacs.add(bac); }
}



