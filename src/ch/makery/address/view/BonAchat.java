package ch.makery.address.view;

import java.util.concurrent.atomic.AtomicInteger;

public class BonAchat {

    private static final AtomicInteger compteurId = new AtomicInteger(0);

    private final int id;
    private final int montant;

    public BonAchat(int montant) {
        this.id = compteurId.incrementAndGet();
        this.montant = montant;
    }

    public BonAchat(int id, int montant) {
        this.id = id;
        this.montant = montant;
        compteurId.updateAndGet(current -> Math.max(current, id));
    }

    public int getId() {
        return id;
    }

    public int getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        return "BonAchat{id=" + id + ", montant=" + montant + "}";
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

