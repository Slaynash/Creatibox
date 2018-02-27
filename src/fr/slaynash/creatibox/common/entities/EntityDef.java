package fr.slaynash.creatibox.common.entities;

public abstract class EntityDef {
	
	private String name;
	
	public EntityDef(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract void addParameter(String line);
	public abstract Entity createEntity(int id, float posX, float posY, float posZ, float angX, float angY, float angZ);
	
}
