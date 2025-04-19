package projet.tri;

public class Dechet_test {
    public static void main(String[] args) {
        Dechet d = new Dechet();
        d.setType(TypeDechet.CARTON);
        d.setPoids(12);

        if (d.getType() == TypeDechet.CARTON && d.getPoids() == 12) {
            System.out.println("Test Dechet valide");
        } else {
            System.out.println("Test Dechet non valide");
        }
    }
}
