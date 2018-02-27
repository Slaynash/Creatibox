package fr.slaynash.creatibox.client.pages;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import fr.slaynash.creatibox.client.connection.ConnectionManager;
import fr.slaynash.creatibox.client.connection.ConnectionStateHandler;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.gui.GUIManager;
import slaynash.sgengine.gui.GUIText;
import slaynash.sgengine.playercharacters.PlayerCharacterVR;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.utils.MatrixUtils;
import slaynash.sgengine.utils.Scene;
import slaynash.sgengine.utils.SceneManager;
import slaynash.sgengine.utils.UserInputUtil;
import slaynash.sgengine.utils.VRUtils;
import slaynash.sgengine.utils.sceneManagerEvent.SceneManagerAdapter;
import slaynash.sgengine.utils.sceneManagerEvent.SceneManagerEvent;
import slaynash.text.utils.Localization;

public class ConnectPage extends Scene implements ConnectionStateHandler {
	
	protected String status = "";
	private GUIText textstatus;
	private boolean errored = false;
	
	@Override
	public void init() {
		GUIManager.reset();
		if(Configuration.isVR()) {
			GUIManager.setDrawMode(GUIManager.DRAWMODE_MENU);
			Configuration.setPlayerCharacter(new PlayerCharacterVR());
		}
		else{
			GUIManager.setDrawMode(GUIManager.DRAWMODE_GAME);
			GUIManager.setBackground("res/textures/menu/background.png");
			
			textstatus = new GUIText("", "tahoma", 500, 0, Display.getHeight()/2, Display.getWidth(), true, null, GUIManager.ELEMENT_MENU);
			textstatus.setColor(0.624f, 0.137f, 0.137f);
		}
	}

	@Override
	public void start() {
		if(Configuration.isVR())
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(!GameValues.isGameserverDisconnectHandled()){
			GameValues.setGameserverDisconnectHandled(true);
			SceneManager.addSceneManagerListener(new SceneManagerAdapter() {
				
				@Override
				public void exited(SceneManagerEvent e) {
					ConnectionManager.disconnectInternal("game closed");
				}

				@Override
				public void initialized(SceneManagerEvent arg0) {}
			});
		}
		ConnectionManager.connectASync(this);
		UserInputUtil.setMouseGrabbed(false);
	}
	
	@Override
	public void update() {
		if(errored){
			GUIManager.showPopup(GUIManager.POPUP_ERROR, status);
			SceneManager.changeScene(MenuPage.class);
		}
		if(!Configuration.isVR()) textstatus.setText(status);
		else Configuration.getPlayerCharacter().update();
	}
	
	@Override
	public void render() {

		if(Configuration.isVR()) {
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
			
	    	ShaderManager.start3DShader();
			
			ShaderManager.shader_loadProjectionMatrix(MatrixUtils.createProjectionMatrix(Configuration.getZNear(), Configuration.getZFar(), Configuration.getFOV()));
			ShaderManager.shader_loadViewMatrix(MatrixUtils.createCharacterViewMatrix());
			ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
			
			VRUtils.renderControllers3D();
	    	VRUtils.renderBaseStations3D();
	
			ShaderManager.stopShader();
			
			int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[ConnectPage] Render> GLERROR: "+err);
			
		}
	}
	
	@Override
	public void renderVR(int eye) {
		
		ShaderManager.startVRShader();
		
		ShaderManager.shader_loadProjectionMatrix(VRUtils.getProjectionMatrix(eye));
		ShaderManager.shader_loadViewMatrix(VRUtils.getViewMatrix(eye));
		ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
		
		VRUtils.renderControllers(eye);
    	VRUtils.renderBaseStations(eye);
		
		ShaderManager.stopShader();
		
		//VRMenuGUI.renderVR();
		
	}
	


	@Override
	public void stop() {}

	@Override
	public void handleConnectException(Exception e) {
		System.out.println(LogTimer.getTimeForLog()+"[ConnectPage] Error while connecting !");
		status = Localization.getTranslation("DISCONNECTED_ERROR");
		errored = true;
	}

	@Override
	public void connectionFinished() {
		System.out.println(LogTimer.getTimeForLog()+"[ConnectPage] Connection finished !");
		try {Thread.sleep(200);} catch (InterruptedException e1) {e1.printStackTrace();}
		SceneManager.changeScene(GamePage.class);
	}

	@Override
	public void connectionStateChanged(int state) {
		System.out.println(LogTimer.getTimeForLog()+"[ConnectPage] State changed to "+state+" !");
		if(state == ConnectionManager.DISCONNECTED){
			status = Localization.getTranslation("DISCONNECTED_CONNECTION_REFUSED")+": "+ConnectionManager.getDisconnectMessage();
			errored = true;
			return;
		}
		else status = Localization.getTranslation("CONNECTION_STATE_"+state);
	}

	@Override
	public void resize() {}
}
