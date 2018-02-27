package fr.slaynash.creatibox.client.vrguipages;

import org.lwjgl.util.vector.Matrix4f;

import fr.slaynash.creatibox.client.pages.ConnectPage;
import jopenvr.VREvent_Data_t;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.utils.MatrixUtils;
import slaynash.sgengine.utils.SceneManager;
import slaynash.sgengine.utils.VRUtils;
import slaynash.sgengine.utils.vr.VRController;
import slaynash.sgengine.utils.vr.VRControllerEventListener;
import slaynash.text.utils.Localization;

public class VRMenuGUI implements VRControllerEventListener{
	
	private static VRMenuShader shader;
	private static VR2DButton playb;
	private static VR2DButton quitb;
	private static VRMenuGUI instance = null;
	
	
	public static void init(){
		if(instance == null){
			shader = new VRMenuShader();
			instance = new VRMenuGUI();
		}
		
		if(playb == null) playb = new VR2DButton(Localization.getTranslation("PLAY"), 0f, 1.4f, 0f, 0.3f, 0.08f, 0, 0);
		playb.setVRButtonListener(new VRButtonListener(){
			@Override
			public void onPress(){
				SceneManager.changeScene(ConnectPage.class);
			}
		});
		if(quitb == null) quitb = new VR2DButton(Localization.getTranslation("QUIT"), 0f, 1.3f, 0f, 0.3f, 0.08f, 0, 0);
		quitb.setVRButtonListener(new VRButtonListener(){
			@Override
			public void onPress(){
				SceneManager.close();
			}
		});
		
		for(VRController c:VRUtils.getAllControllers()) c.setVRControllerEventListener(instance);
	}
	
	public static void update() {
		playb.update();
		quitb.update();
	}
	
	public static void render3D() {
		shader.use();
    	
		ShaderManager.shader_loadViewMatrix(VRUtils.getViewMatrix(VRUtils.EYE_CENTER));
		ShaderManager.shader_loadProjectionMatrix(MatrixUtils.createProjectionMatrix(Configuration.getZNear(), Configuration.getZFar(), Configuration.getFOV()));
		ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
		
		playb.render();
		quitb.render();
		
		ShaderManager.stopShader();
	}

	public static void renderVR(int eye) {
		//VRUtils.setCurrentRenderEye(eye);
		shader.use();
		
		ShaderManager.shader_loadViewMatrix(VRUtils.getViewMatrix(eye));
		ShaderManager.shader_loadProjectionMatrix(VRUtils.getProjectionMatrix(eye));
		ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
		
		playb.renderVR(eye);
		quitb.renderVR(eye);
		
		ShaderManager.stopShader();
	}

	public static VRMenuShader getShader() {
		return shader;
	}

	@Override
	public void onEvent(int deviceIndex, int eventType, VREvent_Data_t data) {
		playb.onControllerEvent(deviceIndex, eventType, data);
		quitb.onControllerEvent(deviceIndex, eventType, data);
	}

	public static void destroy() {
		for(VRController c:VRUtils.getAllControllers()) if(c != null) c.setVRControllerEventListener(null);
	}

}