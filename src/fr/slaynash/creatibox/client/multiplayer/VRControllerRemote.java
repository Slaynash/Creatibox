package fr.slaynash.creatibox.client.multiplayer;

import org.lwjgl.util.vector.Vector3f;

import fr.slaynash.creatibox.common.EntityMatrixUtils;
import slaynash.sgengine.shaders.ShaderManager;

public class VRControllerRemote {
	
public float id;
	
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
	
	
	public void render3D() {
		ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), rotX, rotY, rotZ, 1));
		Player.modelVRController.render();
	}
	
	public void renderVR() {
		ShaderManager.shader_loadTransformationMatrix(EntityMatrixUtils.createEntityTransformationMatrix(new Vector3f(posX, posY, posZ), rotX, rotY, rotZ, 1));
		Player.modelVRController.render();
	}
	
}
