package fr.slaynash.creatibox.client.vrguipages;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import fr.slaynash.creatibox.common.LogTimer;
import jopenvr.JOpenVRLibrary;
import jopenvr.VREvent_Data_t;
import slaynash.sgengine.LogSystem;
import slaynash.sgengine.models.Renderable2dModel;
import slaynash.sgengine.models.utils.VaoManager;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.textureUtils.TextureManager;
import slaynash.sgengine.utils.MatrixUtils;
import slaynash.sgengine.utils.VRUtils;
import slaynash.sgengine.utils.vr.VRController;

public class VR2DButton{

	private Matrix4f matrix;
	private float width;
	private float height;
	private boolean isHovering = false;
	
	TextVR text;
	
	private float border = 0.01f;
	
	boolean[] isControllerIn = new boolean[JOpenVRLibrary.k_unMaxTrackedDeviceCount];
	private VRButtonListener listener = null;
	private Renderable2dModel buttonModel;
	private Renderable2dModel borderModel;

	public VR2DButton(String text, float x, float y, float z, float w, float h, float pitch, float yaw) {
		matrix = MatrixUtils.createTransformationMatrix(new Vector3f(x, y, z), pitch, yaw, 0, 1);
		this.width = w;
		this.height = h;

		this.text = new TextVR(text, "tahoma", 1f, new Vector2f(-width*.5f, height*.5f), width*.5f, true);
		this.text.setParentTransform(matrix);
		
		for(int i=0;i<isControllerIn.length;i++) isControllerIn[i] = false;
		
		
		float[] borderVertices = new float[] {
				-width*.5f, height*.5f,
				width*.5f, height*.5f,
				width*.5f, height*.5f+border,
				width*.5f, height*.5f+border,
				-width*.5f, height*.5f+border,
				-width*.5f, height*.5f,
				
				-width*.5f, -height*.5f-border,
				width*.5f, -height*.5f-border,
				width*.5f, -height*.5f,
				width*.5f, -height*.5f,
				-width*.5f, -height*.5f,
				-width*.5f, -height*.5f-border,
				
				-width*.5f-border, -height*.5f-border,
				-width*.5f, -height*.5f-border,
				-width*.5f, height*.5f+border,
				-width*.5f, height*.5f+border,
				-width*.5f-border, height*.5f+border,
				-width*.5f-border, -height*.5f-border,
				
				width*.5f, -height*.5f-border,
				width*.5f+border, -height*.5f-border,
				width*.5f+border, height*.5f+border,
				width*.5f+border, height*.5f+border,
				width*.5f, height*.5f+border,
				width*.5f, -height*.5f-border
		};
		
		float[] borderUvs = new float[] {
				0,0,1,0,1,1,1,1,0,1,0,0,
				0,0,1,0,1,1,1,1,0,1,0,0,
				0,0,1,0,1,1,1,1,0,1,0,0,
				0,0,1,0,1,1,1,1,0,1,0,0
		};
		
		borderModel = new Renderable2dModel(VaoManager.loadToVao2d(borderVertices, borderUvs), TextureManager.getTextureDef("res/textures/yellowbutton.png", TextureManager.TEXTURE_DIFFUSE));
		
		
		float[] buttonVertices = new float[] {
				-width*.5f, -height*.5f,
				width*.5f, -height*.5f,
				width*.5f, height*.5f,
				width*.5f, height*.5f,
				-width*.5f, height*.5f,
				-width*.5f, -height*.5f,
		};
		
		float[] buttonUvs = new float[] {
				0,0,1,0,1,1,1,1,0,1,0,0
		};
		
		
		buttonModel = new Renderable2dModel(VaoManager.loadToVao2d(buttonVertices, buttonUvs), TextureManager.getTextureDef("res/textures/graybutton.png", TextureManager.TEXTURE_DIFFUSE));
		
		
	}
	
	public void update() {
		isHovering = false;
		//System.out.println(VRUtils.getValidControllers().length);
		for(VRController c:VRUtils.getValidControllers()){
			if(!c.isPoseValid()) continue;
			
			Matrix4f rcm = Matrix4f.mul(
					MatrixUtils.inverse(matrix, new Matrix4f()),
					MatrixUtils.copy(c.getPose(), new Matrix4f()).translate(new Vector3f(0, -0.030f, -0.020f)),
					new Matrix4f()
			);
			
			float rcpX = rcm.m30;
			float rcpY = rcm.m31;
			float rcpZ = rcm.m32;
			
			if( -width*.5f < rcpX && rcpX < width*.5f &&
					-height*.5f < rcpY && rcpY < height*.5f &&
					-.02f < rcpZ && rcpZ < .02f
				){
					isHovering = true;
					isControllerIn[c.getId()] = true;
				}
				else isControllerIn[c.getId()] = false;
		}
	}
	
	public void render() {
		//ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(0, 0, 0), 0, 0, 0, 1));
		ShaderManager.shader_loadTransformationMatrix(matrix);
		
		if(isHovering) borderModel.render();
		buttonModel.render();
		LogSystem.out_println("BUTTON RANDERED AT "+matrix.m30+"_"+matrix.m31+"_"+matrix.m32);
		
		text.render();
	}

	public void renderVR(int eye) {
		//ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(0, 0, 0), 0, 0, 0, 1));
		ShaderManager.shader_loadTransformationMatrix(matrix);
		
		if(isHovering) borderModel.renderVR(eye);
		buttonModel.renderVR(eye);
		
		text.render();
		
	}
	
	public void onControllerEvent(int deviceIndex, int eventType, VREvent_Data_t data) {//FIXME VREvent_Data_t.controller.button is wrong ! (return always 0)
		if(isControllerIn[deviceIndex] && eventType == JOpenVRLibrary.EVREventType.EVREventType_VREvent_ButtonPress){
			System.out.println(LogTimer.getTimeForLog()+"[VR2DButton] Controller button pressed: "+data.controller.button);
			if(listener != null) listener.onPress();
			/*
			if(data.controller.button == JOpenVRLibrary.EVRButtonId.){
				System.out.println("Button "+text.getTextString()+" pressed !");
			}
			*/
		}
	}

	public void setVRButtonListener(VRButtonListener vrButtonListener) {
		listener = vrButtonListener;
	}

}
