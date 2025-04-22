package Poubelle;

public class Bac {
    private int poidsActuel;
    private int capaciteMax;
    private TypeDechet typeDechet;

    public void ajouterDechet(int poids) {
        if (poidsActuel + poids <= capaciteMax) {
            poidsActuel += poids;
        } else {
            System.out.println("Le bac est plein !");
        }
    }

    public boolean estPlein() {
        return poidsActuel >= capaciteMax;
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

    public TypeDechet getTypeDechet() {
        return typeDechet;
    }

    public void setTypeDechet(TypeDechet typeDechet) {
        this.typeDechet = typeDechet;
    }
}

