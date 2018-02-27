package fr.slaynash.creatibox.client.vrguipages;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import slaynash.sgengine.gui.text2d.Text2d;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.utils.MatrixUtils;

public class TextVR extends Text2d{
	public Matrix4f parentTransform = new Matrix4f();
	
	public TextVR(String text, String fontLocation, float fontSize, Vector2f position, float maxLineLength, boolean centered){
		super(text, fontLocation, fontSize, new Vector2f(position.x+1, -position.y+1-Display.getHeight()), maxLineLength, centered, null);
	}
	
	@Override
	public void render(){
		ShaderManager.shader_setTextMode();
		ShaderManager.shader_loadColor(getColour());
		ShaderManager.shader_loadTranslation(new Vector2f(getPosition().x, getPosition().y));
		ShaderManager.shader_loadTransformationMatrix(Matrix4f.mul(MatrixUtils.createTransformationMatrix(new Vector3f(0,0,0.001f), 0, 0, 0, 1), parentTransform, null));
		
		getModel().render();
		
		ShaderManager.shader_loadTranslation(new Vector2f());
		ShaderManager.shader_exitTextMode();
	}
	
	public void setParentTransform(Matrix4f transform) {
		parentTransform = transform;
	}
	
}
