package tri.logic;

import java.util.ArrayList;
import java.util.List;

public class Commerce {

    private int idCommerce;
    private String nom;
    private List<Produit> produits;
    private List<Contrat> contrats;

    public Commerce(int idCommerce, String nom) {
        this.idCommerce = idCommerce;
        this.nom = nom;
        this.produits = new ArrayList<>();
        this.contrats = new ArrayList<>();
    }

    public int getIdCommerce() {
        return idCommerce;
    }

    public void setIdCommerce(int idCommerce) {
        this.idCommerce = idCommerce;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public List<Contrat> getContrats() {
        return contrats;
    }

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }

    public void ajouterContrat(Contrat contrat) {
        contrats.add(contrat);
    }

    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    public void supprimerContrat(Contrat contrat) {
        contrats.remove(contrat);
    }

    public boolean verifierBonAchat(List<Produit> panier) {
        for (Produit produit : panier) {
            for (Contrat contrat : contrats) {
                if (contrat.estProduitEligible(produit)) {
                    return true;
                }
            }
        }
        return false;
    }
}

