package projet.tri;


public class Dechet {
	private TypeDechet type;
	private int poids;
	
	public Dechet() {}
	
	public Dechet(TypeDechet type, int poids) {
		super();
		this.type = type;
		this.poids = poids;
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
