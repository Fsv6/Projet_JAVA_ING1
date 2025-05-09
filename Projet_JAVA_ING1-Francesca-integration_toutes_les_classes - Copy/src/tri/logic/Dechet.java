package tri.logic;

public class Dechet {

	private int idDechet;
	private TypeDechet type;
	private int poids;

	public Dechet() {}

	public Dechet(int idDechet, TypeDechet type, int poids) {
		this.idDechet = idDechet;
		this.type = type;
		this.poids = poids;
	}

	public int getIdDechet() {
		return idDechet;
	}

	public void setIdDechet(int idDechet) {
		this.idDechet = idDechet;
	}

	public TypeDechet getType() {
		return type;
	}

	public void setType(TypeDechet type) {
		this.type = type;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}
}
