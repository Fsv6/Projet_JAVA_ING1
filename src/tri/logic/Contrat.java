package tri.logic;

import java.time.LocalDate;
import java.util.List;

public class Contrat {

    private int id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<String> listeCatProduits; // catégories de produits éligibles aux bons

    public Contrat(int id, LocalDate dateDebut, LocalDate dateFin, List<String> listeCatProduits) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.listeCatProduits = listeCatProduits;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<String> getListeCatProduits() {
        return listeCatProduits;
    }

    public void setListeCatProduits(List<String> listeCatProduits) {
        this.listeCatProduits = listeCatProduits;
    }

    public boolean estActif() {
        LocalDate now = LocalDate.now();
        return (now.isEqual(dateDebut) || now.isAfter(dateDebut)) &&
                (now.isEqual(dateFin) || now.isBefore(dateFin));
    }


    public boolean estProduitEligible(Produit produit) {
        return listeCatProduits.contains(produit.getCategorie());
    }

    @Override
    public String toString() {
        return "Contrat ID: " + id + ", valide du " + dateDebut + " au " + dateFin;
    }
}

