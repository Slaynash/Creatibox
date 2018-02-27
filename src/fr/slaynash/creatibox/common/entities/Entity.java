package fr.slaynash.creatibox.common.entities;

public abstract class Entity {
	
	private int id;
	
	private float posX;
	private float posY;
	private float posZ;

	private float angX;
	private float angY;
	private float angZ;
	
	private EntityDef def;
	
	public Entity(int id, float posX, float posY, float posZ, float angX, float angY, float angZ, EntityDef def) {
		this.id = id;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.angX = angX;
		this.angY = angY;
		this.angZ = angZ;
		this.def = def;
	}
	
	public abstract void update();
	public abstract void render3D();
	public abstract void renderVR(int eye);
	public abstract void destroy();

	public int getId() {
		return id;
	}

	public void setPosition(float posX, float posY, float posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public void setRotation(float rotX, float rotY, float rotZ) {
		this.angX = rotX;
		this.angY = rotY;
		this.angZ = rotZ;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public float getPosZ() {
		return posZ;
	}

	public float getAngX() {
		return angX;
	}

	public float getAngY() {
		return angY;
	}

	public float getAngZ() {
		return angZ;
	}

	public EntityDef getDef() {
		return def;
	}
	
	
	
}
