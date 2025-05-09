/*
Fonctionnalités testées :


tester la conversion d'un bon d'achat en points de fidélité
(tester l'utilisation valide d'un bon d'achat)
tester le depot d'un depot conforme et l'attribution de points de fidélité
tester le depot d'un depot non conforme et l'attribution de points de fidélité


 */

package tri.main;

import tri.dao.*;
import tri.exception.DechetNonConformeException;
import tri.logic.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

    public class MainTest {

        static CentreTriDAO centreDAO;
        static PoubelleIntelligenteDAO poubelleDAO;
        static BacDAO bacDAO;
        static CommerceDAO commerceDAO;
        static CompteDAO compteDAO;
        static BonAchatDAO bonAchatDAO;
        static ContratDAO contratDAO;
        static ProduitDAO produitDAO;
        static VendreDAO vendreDAO;
        static DechetDAO dechetDAO;
        static DepotDAO depotDAO;

        static CentreTri centreTri;
        static PoubelleIntelligente poubelle;
        static Bac bacBleu;
        static Bac bacJaune;
        static Bac bacVert;
        static Bac bacNoir;
        static Commerce commerce1;
        static Commerce commerce2;

        public static void main(String[] args) {
            System.out.println("=== Début des tests ===");

            initialiserDAOs();
            initialiserDonneesGlobales();


            testConversionPointsEnBon();
            //testUtilisationValideBon();
            testDepotConforme();
            testDepotNonConforme();

            System.out.println("=== Fin des tests ===");
        }


        public static void initialiserDAOs() {
            centreDAO = new CentreTriDAO();
            poubelleDAO = new PoubelleIntelligenteDAO();
            bacDAO = new BacDAO();
            commerceDAO = new CommerceDAO();
            compteDAO = new CompteDAO();
            bonAchatDAO = new BonAchatDAO();
            contratDAO = new ContratDAO();
            produitDAO = new ProduitDAO();
            vendreDAO = new VendreDAO();
            dechetDAO = new DechetDAO();
            depotDAO = new DepotDAO();
        }


        public static void initialiserDonneesGlobales() {
            try {
                centreTri = new CentreTri(1, "Cy TECH", 1, "Chemin des Paradis", "Cergy", 95000);
                try {
                    CentreTri centreExistant = centreDAO.getCentreTriById(centreTri.getIdCentreTri());
                    centreTri = centreExistant;
                    System.out.println("CentreTri déjà existant.");
                } catch (Exception e) {
                    centreDAO.insertCentreTri(centreTri);
                    System.out.println("CentreTri inséré.");
                }

                poubelle = new PoubelleIntelligente(1, "Quartier Centre", 48.85f, 2.35f);
                try {
                    PoubelleIntelligente poubelleExistante = poubelleDAO.getPoubelleById(poubelle.getId());
                    poubelle = poubelleExistante;
                    System.out.println("Poubelle déjà existante.");
                } catch (Exception e) {
                    poubelleDAO.insertPoubelle(poubelle, centreTri.getIdCentreTri());
                    System.out.println("Poubelle insérée.");
                }
                centreTri.ajouterPoubelle(poubelle);

                bacBleu = new Bac(1, 100, List.of(TypeDechet.PAPIER));
                bacJaune = new Bac(2, 100, List.of(TypeDechet.CARTON, TypeDechet.METAL, TypeDechet.PLASTIQUE));
                bacVert = new Bac(3, 100, List.of(TypeDechet.VERRE));
                bacNoir = new Bac(4, 100, List.of(TypeDechet.AUTRE));

                try {
                    Bac bacExist = bacDAO.getBacById(bacBleu.getIdBac());
                    bacBleu = bacExist;
                    System.out.println("Bac Bleu déjà existant.");
                } catch (Exception e) {
                    bacDAO.insertBac(bacBleu, poubelle.getId());
                    System.out.println("Bac Bleu inséré.");
                }

                try {
                    Bac bacExist = bacDAO.getBacById(bacJaune.getIdBac());
                    bacJaune = bacExist;
                    System.out.println("Bac Jaune déjà existant.");
                } catch (Exception e) {
                    bacDAO.insertBac(bacJaune, poubelle.getId());
                    System.out.println("Bac Jaune inséré.");
                }

                try {
                    Bac bacExist = bacDAO.getBacById(bacVert.getIdBac());
                    bacVert = bacExist;
                    System.out.println("Bac Vert déjà existant.");
                } catch (Exception e) {
                    bacDAO.insertBac(bacVert, poubelle.getId());
                    System.out.println("Bac Vert inséré.");
                }

                try {
                    Bac bacExist = bacDAO.getBacById(bacNoir.getIdBac());
                    bacNoir = bacExist;
                    System.out.println("Bac Ordure déjà existant.");
                } catch (Exception e) {
                    bacDAO.insertBac(bacNoir, poubelle.getId());
                    System.out.println("Bac Ordure inséré.");
                }

                poubelle.getBacs().addAll(List.of(bacBleu, bacJaune, bacVert, bacNoir));

                commerce1 = new Commerce(1, "Supermarché Bio");
                try {
                    Commerce commerceExistant1 = commerceDAO.getCommerceById(commerce1.getIdCommerce());
                    commerce1 = commerceExistant1;
                    System.out.println("Commerce 1 déjà existant.");
                } catch (Exception e) {
                    commerceDAO.insertCommerce(commerce1);
                    System.out.println("Commerce 1 inséré.");
                }

                commerce2 = new Commerce(2, "ElectroMarket");
                try {
                    Commerce commerceExistant2 = commerceDAO.getCommerceById(commerce2.getIdCommerce());
                    commerce2 = commerceExistant2;
                    System.out.println("Commerce 2 déjà existant.");
                } catch (Exception e) {
                    commerceDAO.insertCommerce(commerce2);
                    System.out.println("Commerce 2 inséré.");
                }
                System.out.println("Données globales initialisées correctement.");

            } catch (Exception e) {
                System.out.println("Erreur pendant initialisation globale : " + e.getMessage());
                e.printStackTrace();
            }
        }





        public static void testConversionPointsEnBon() {
            System.out.println("\n[Test] Conversion progressive de deux bons d'achat");

            try {
                Compte compte = new Compte(10, "Jean", "Dupont", 130);
                try {
                    Compte compteExistant = compteDAO.getCompteById(compte.getId());
                    compteDAO.updatePointsFidelite(compteExistant.getId(), 130);
                    compte = compteExistant;
                    System.out.println("Compte déjà existant : ID " + compte.getId());
                } catch (Exception e) {
                    compteDAO.insertCompte(compte);
                    System.out.println("Compte inséré : ID " + compte.getId());
                }

                compte.convertirEnBonAchat();
                BonAchat bon1 = compte.getListBonAchat().get(0);

                try {
                    BonAchat bonExistant1 = bonAchatDAO.getBonAchatById(bon1.getId());
                    System.out.println("Premier bon déjà existant, création d'un nouveau bon.");
                    bon1 = new BonAchat(bon1.getId() + 100, bon1.getMontant());
                } catch (Exception e) {
                    System.out.println("Premier bon non existant, insertion normale.");
                }
                bonAchatDAO.insertBonAchat(bon1);
                System.out.println("Premier bon inséré : ID " + bon1.getId());

                compteDAO.updatePointsFidelite(compte.getId(), compte.getNbPointsFidelite());

                compte.convertirEnBonAchat();
                BonAchat bon2 = compte.getListBonAchat().get(1);

                try {
                    BonAchat bonExistant2 = bonAchatDAO.getBonAchatById(bon2.getId());
                    System.out.println("Deuxième bon déjà existant, création d'un nouveau bon.");
                    bon2 = new BonAchat(bon2.getId() + 100, bon2.getMontant());
                } catch (Exception e) {
                    System.out.println("Deuxième bon non existant, insertion normale.");
                }
                bonAchatDAO.insertBonAchat(bon2);
                System.out.println("Deuxième bon inséré : ID " + bon2.getId());

                compteDAO.updatePointsFidelite(compte.getId(), compte.getNbPointsFidelite());

                try {
                    Compte compteFinal = compteDAO.getCompteById(compte.getId());
                    if (compteFinal.getNbPointsFidelite() == 10) {
                        System.out.println("Test réussi : le compte a bien 10 points de fidélité restants.");
                    } else {
                        System.out.println("Erreur : le compte a " + compteFinal.getNbPointsFidelite() + " points au lieu de 10 !");
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la récupération finale du compte : " + e.getMessage());
                }

            } catch (Exception e) {
                System.out.println("Erreur dans testConversionPointsEnBon : " + e.getMessage());
                e.printStackTrace();
            }
        }
/*
        public static void testUtilisationValideBon() {
            System.out.println("\n[Test] Utilisation valide d'un bon d'achat");

            try {
                Compte compte = new Compte(20, "Alice", "Martin", 70);
                try {
                    Compte compteExistant = compteDAO.getCompteById(compte.getId());
                    compteDAO.updatePointsFidelite(compteExistant.getId(), 70);
                    compte = compteExistant;
                    System.out.println("Compte déjà existant : ID " + compte.getId());
                } catch (Exception e) {
                    compteDAO.insertCompte(compte);
                    System.out.println("Compte inséré : ID " + compte.getId());
                }

                compte.convertirEnBonAchat();
                BonAchat bon = compte.getListBonAchat().get(0);

                bonAchatDAO.insertBonAchat(bon);

                try {
                    Commerce commerceExistant = commerceDAO.getCommerceById(commerce1.getIdCommerce());
                    commerce1 = commerceExistant;
                    System.out.println("Commerce déjà existant : ID " + commerce1.getIdCommerce());
                } catch (Exception e) {
                    commerceDAO.insertCommerce(commerce1);
                    System.out.println("Commerce inséré : ID " + commerce1.getIdCommerce());
                }

                List<String> categoriesEligibles = new ArrayList<>();
                categoriesEligibles.add("Alimentaire");

                centreTri.signerContrat(commerce1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), categoriesEligibles);
                Contrat contrat = centreTri.getContrats().get(centreTri.getContrats().size() - 1);

                try {
                    contratDAO.getContrat(centreTri.getIdCentreTri(), commerce1.getIdCommerce());
                    System.out.println("Contrat déjà existant : ID " + contrat.getId());
                } catch (Exception e) {
                    contratDAO.insertContrat(contrat, commerce1.getIdCommerce(), bon.getId());
                    System.out.println("Contrat inséré : ID " + contrat.getId());
                }

                Produit produit = new Produit(1, "Alimentaire", "Pâtes", 1.99);
                try {
                    Produit produitExistant = produitDAO.getProduitById(produit.getIdProduit());
                    produit = produitExistant;
                    System.out.println("Produit déjà existant : ID " + produit.getIdProduit());
                } catch (Exception e) {
                    produitDAO.insertProduit(produit);
                    System.out.println("Produit inséré : ID " + produit.getIdProduit());
                }

                commerce1.ajouterProduit(produit);

                try {
                    vendreDAO.getCommercesByProduit(produit.getIdProduit());
                    System.out.println("Vente déjà existante entre le commerce et le produit.");
                } catch (Exception e) {
                    vendreDAO.insertVente(commerce1.getIdCommerce(), produit.getIdProduit());
                    System.out.println("Vente insérée.");
                }

                List<Produit> panier = new ArrayList<>();
                panier.add(produit);

                compte.utiliserBonAchat(bon, commerce1, panier);

                if (!compte.getListBonAchat().contains(bon)) {
                    System.out.println("Erreur : bon non supprimé après utilisation valide.");
                } else {
                    System.out.println("Utilisation valide confirmée : bon consommé.");
                }

            } catch (Exception e) {
                System.out.println("Erreur dans testUtilisationValideBon : " + e.getMessage());
                e.printStackTrace();
            }
        }
*/

        public static void testDepotConforme() {
            System.out.println("\n[Test] Dépôt conforme et attribution de points de fidélité");

            try {
                Compte compte = new Compte(30, "Paul", "Durand", 0);
                try {
                    Compte compteExistant = compteDAO.getCompteById(compte.getId());
                    compte = compteExistant;
                    System.out.println("Compte déjà existant : ID " + compte.getId());
                } catch (Exception e) {
                    compteDAO.insertCompte(compte);
                    System.out.println("Compte inséré : ID " + compte.getId());
                }


                PoubelleIntelligente poubelle = new PoubelleIntelligente(5, "Quartier Test", 48.85f, 2.35f);
                try {
                    PoubelleIntelligente poubelleExistante = poubelleDAO.getPoubelleById(poubelle.getId());
                    poubelle = poubelleExistante;
                    System.out.println("Poubelle déjà existante : ID " + poubelle.getId());
                } catch (Exception e) {
                    poubelleDAO.insertPoubelle(poubelle, centreTri.getIdCentreTri());
                    System.out.println("Poubelle insérée : ID " + poubelle.getId());
                }

                Bac bacPapier = new Bac(5, 100, List.of(TypeDechet.PAPIER));
                try {
                    Bac bacExist = bacDAO.getBacById(bacPapier.getIdBac());
                    bacPapier = bacExist;
                    System.out.println("Bac déjà existant : ID " + bacPapier.getIdBac());
                } catch (Exception e) {
                    bacDAO.insertBac(bacPapier, poubelle.getId());
                    System.out.println("Bac inséré : ID " + bacPapier.getIdBac());
                }

                poubelle.ajouterBac(bacPapier);

                Dechet papier = new Dechet(10, TypeDechet.PAPIER, 1); // 1 kg de papier

                ArrayList<Dechet> dechets = new ArrayList<>();
                dechets.add(papier);
                Depot depot = new Depot(1, 0, new Date(), dechets);

                try {
                    poubelle.ajouterDepot(depot, bacPapier);
                    System.out.println("Dépôt effectué sans erreur.");
                } catch (DechetNonConformeException e) {
                    System.out.println("Erreur : dépôt non conforme alors qu'il devait l'être !");
                    e.printStackTrace();
                }

                // Attribution de points et vérification
                if (depot.getPointsAttribues() == 3) {
                    System.out.println("Test réussi : 3 points attribués pour dépôt conforme.");
                } else {
                    System.out.println("Erreur : mauvais nombre de points (" + depot.getPointsAttribues() + ")");
                }

            } catch (Exception e) {
                System.out.println("Erreur générale dans testDepotConforme : " + e.getMessage());
                e.printStackTrace();
            }
        }

        public static void testDepotNonConforme() {
            System.out.println("\n[Test] Dépôt non conforme et attribution de points de pénalité");

            try {
                Compte compte = new Compte(31, "Lucie", "Martin", 0);
                try {
                    Compte compteExistant = compteDAO.getCompteById(compte.getId());
                    compte = compteExistant;
                    System.out.println("Compte déjà existant : ID " + compte.getId());
                } catch (Exception e) {
                    compteDAO.insertCompte(compte);
                    System.out.println("Compte inséré : ID " + compte.getId());
                }

                PoubelleIntelligente poubelle = new PoubelleIntelligente(6, "Quartier NonConforme", 48.86f, 2.34f);
                try {
                    PoubelleIntelligente poubelleExistante = poubelleDAO.getPoubelleById(poubelle.getId());
                    poubelle = poubelleExistante;
                    System.out.println("Poubelle déjà existante : ID " + poubelle.getId());
                } catch (Exception e) {
                    poubelleDAO.insertPoubelle(poubelle, centreTri.getIdCentreTri());
                    System.out.println("Poubelle insérée : ID " + poubelle.getId());
                }

                Bac bacPapier = new Bac(6, 100, List.of(TypeDechet.PAPIER));
                try {
                    Bac bacExist = bacDAO.getBacById(bacPapier.getIdBac());
                    bacPapier = bacExist;
                    System.out.println("Bac déjà existant : ID " + bacPapier.getIdBac());
                } catch (Exception e) {
                    bacDAO.insertBac(bacPapier, poubelle.getId());
                    System.out.println("Bac inséré : ID " + bacPapier.getIdBac());
                }

                poubelle.ajouterBac(bacPapier);

                Dechet plastique = new Dechet(11, TypeDechet.PLASTIQUE, 1);

                ArrayList<Dechet> dechets = new ArrayList<>();
                dechets.add(plastique);
                Depot depot = new Depot(2, 0, new Date(), dechets);

                boolean exceptionAttrapee = false;
                try {
                    poubelle.ajouterDepot(depot, bacPapier);
                    System.out.println("Erreur : Dépôt non conforme accepté alors qu'il aurait dû échouer !");
                } catch (DechetNonConformeException e) {
                    System.out.println("Exception capturée correctement : dépôt non conforme.");
                    exceptionAttrapee = true;
                }

                if (exceptionAttrapee) {
                    if (depot.getPointsAttribues() == -2) {
                        System.out.println("Test réussi : -2 points attribués pour déchet non conforme.");
                    } else {
                        System.out.println("Erreur : mauvais nombre de points (" + depot.getPointsAttribues() + ")");
                    }
                }

            } catch (Exception e) {
                System.out.println("Erreur générale dans testDepotNonConforme : " + e.getMessage());
                e.printStackTrace();
            }
        }




    }


