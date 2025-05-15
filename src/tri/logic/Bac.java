package tri.logic;

import tri.exception.BacPleinException;

import java.util.List;

public class Bac {
    private int idBac;
    private int poidsActuel;
    private int capaciteMax;
    private List<TypeDechet> typesDechets;

    public Bac(int idBac, int capaciteMax, List<TypeDechet> typesDechets) {
        this.idBac = idBac;
        this.capaciteMax = capaciteMax;
        this.typesDechets = typesDechets;
        this.poidsActuel = 0;
    }


    public void ajouterDechet(int poids) {
        if (estPlein() || poidsActuel + poids > capaciteMax) {
            throw new BacPleinException("Impossible d'ajouter des déchets : le bac est plein ou sera dépassé.");
        }
        poidsActuel += poids;
    }

    public boolean estPlein() {
        return poidsActuel >= capaciteMax;
    }

    public void vider() {
        poidsActuel = 0;
    }

    // Getter et setter pour idBac
    public int getIdBac() {
        return idBac;
    }

    public void setIdBac(int idBac) {
        this.idBac = idBac;
    }

    public int getPoidsActuel() {
        return poidsActuel;
    }

    public void setPoidsActuel(int poidsActuel) {
        this.poidsActuel = poidsActuel;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public List<TypeDechet> getTypesDechets() {
        return typesDechets;
    }

    public void setTypesDechets(List<TypeDechet> typesDechets) {
        this.typesDechets = typesDechets;
    }

    @Override
    public String toString() {
        return "Bac{" +
                "idBac=" + idBac +
                ", type=" + typesDechets +
                ", poidsActuel=" + poidsActuel +
                ", capaciteMax=" + capaciteMax +
                '}';
    }
}



