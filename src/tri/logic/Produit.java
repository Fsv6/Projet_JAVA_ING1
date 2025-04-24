package tri.logic;

public class Produit {

    private String categorie;
    private String nom;
    private int prix;

    public Produit(String categorie, String nom, int prix) {
        this.categorie = categorie;
        this.nom = nom;
        this.prix = prix;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return nom + " (" + categorie + ") - " + prix + "€";
    }

}
