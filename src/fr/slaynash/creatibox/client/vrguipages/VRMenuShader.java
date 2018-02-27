package fr.slaynash.creatibox.client.vrguipages;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import slaynash.sgengine.Configuration;
import slaynash.sgengine.LogSystem;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.shaders.ShaderProgram;

public class VRMenuShader extends ShaderProgram {
	
	private int textureDiffuse_location;
	
	public VRMenuShader() {
		super(Configuration.getAbsoluteInstallPath()+Configuration.getRelativeShaderPath(), "vrmenu.vs", "vrmenu.fs");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void connectTextureUnits() {
		GL20.glUniform1i(textureDiffuse_location, ShaderManager.TEXTURE_COLOR);
	}

	@Override
	protected void getAllUniformLocations() {
		getUniformLocation("mMatrix");
		getUniformLocation("vMatrix");
		getUniformLocation("pMatrix");
		getUniformLocation("translation");
		
		getUniformLocation("colour");
		textureDiffuse_location = getUniformLocation("textureDiffuse");
		getUniformLocation("textmode");
	}

	@Override
	public void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_CULL_FACE);
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
    	//GL11.glEnable(GL11.GL_BLEND);
    	//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void stop() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
    	//GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	public void bindModel(int modelID) {
		GL30.glBindVertexArray(modelID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
	}
	
	@Override
	protected int getLocation(String string) {
		Integer key = locations.get(string);
		if(key == null){
			LogSystem.err_println("Couldn't find location "+string);
			return -1;
		}
		return key;
	}

}
