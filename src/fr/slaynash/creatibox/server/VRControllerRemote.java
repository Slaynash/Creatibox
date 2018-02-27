package fr.slaynash.creatibox.server;

public class VRControllerRemote {
	public int id;
	
	public float posX;
	public float posY;
	public float posZ;
	public float rotX;
	public float rotY;
	public float rotZ;
	public boolean valid = false;
	
	public VRControllerRemote(int id) {
		this.id = id;
	}
	
	
}
