package ch.makery.tri.model;

public class Produit {

    private int idProduit;
    private String categorie;
    private String nom;
    private double prix;

    public Produit() {}

    public Produit(int idProduit, String categorie, String nom, double prix) {
        this.idProduit = idProduit;
        this.categorie = categorie;
        this.nom = nom;
        this.prix = prix;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
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

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return nom + " (" + categorie + ") - " + prix + "€";
    }
}

