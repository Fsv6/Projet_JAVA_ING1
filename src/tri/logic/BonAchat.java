package tri.logic;
import java.util.concurrent.atomic.AtomicInteger;

public class BonAchat {

    private static final AtomicInteger compteurId = new AtomicInteger(0); // auto-incrément

    private final int id;
    private final int montant;

    public BonAchat(int montant) {
        this.id = compteurId.incrementAndGet();
        this.montant = montant;
    }

    public int getId() {
        return id;
    }

    public int getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        return "BonAchat{id=" + id + ", montant=" + montant + "€}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BonAchat)) return false;
        BonAchat other = (BonAchat) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
