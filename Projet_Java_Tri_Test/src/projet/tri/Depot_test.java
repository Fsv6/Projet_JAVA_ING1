package projet.tri;
import java.util.Date;

public class Depot_test {
    public static void main(String[] args) {
        Depot depot = new Depot();

        depot.setPoidsDechet(20);
        depot.setPointsAttribues(10);
        Date date = new Date();
        depot.setDateDepot(date);

        boolean ok = true;

        if (depot.getPoidsDechet() != 20) ok = false;
        if (depot.getPointsAttribues() != 10) ok = false;
        if (!depot.getDateDepot().equals(date)) ok = false;

        if (ok) {
            System.out.println("Test Depot valide");
        } else {
            System.out.println("Test Depot non valide");
        }
    }
}
