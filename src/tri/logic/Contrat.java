package tri.logic;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Contrat {

    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private int id;
    private int idCentreTri;
    private int idCommerce;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<String> listeCatProduits; // catégories de produits éligibles aux bons

    public Contrat(LocalDate dateDebut, LocalDate dateFin, List<String> listeCatProduits) {
        this.id = idCounter.getAndIncrement();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.listeCatProduits = listeCatProduits;
    }
    public Contrat(int idCentreTri, int idCommerce, LocalDate dateDebut, LocalDate dateFin, List<String> listeCatProduits) {
        this.id = idCounter.getAndIncrement();
        this.idCentreTri = idCentreTri;
        this.idCommerce = idCommerce;
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
    public int getIdCentreTri() {
        return idCentreTri;
    }

    public int getIdCommerce() {
        return idCommerce;
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

