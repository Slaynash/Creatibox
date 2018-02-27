package fr.slaynash.creatibox.client.pages;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import fr.slaynash.creatibox.client.guipages.GUIOptionPage;
import fr.slaynash.creatibox.client.vrguipages.VRMenuGUI;
import fr.slaynash.creatibox.common.LogTimer;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.gui.GUIManager;
import slaynash.sgengine.gui.GUIText;
import slaynash.sgengine.gui.button.GUIButton;
import slaynash.sgengine.gui.button.GUIButtonEvent;
import slaynash.sgengine.gui.button.GUIButtonListener;
import slaynash.sgengine.gui.text2d.Text2d;
import slaynash.sgengine.maths.Vector2i;
import slaynash.sgengine.playercharacters.PlayerCharacterVR;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.utils.DisplayManager;
import slaynash.sgengine.utils.MatrixUtils;
import slaynash.sgengine.utils.Scene;
import slaynash.sgengine.utils.SceneManager;
import slaynash.sgengine.utils.UserInputUtil;
import slaynash.sgengine.utils.VRUtils;
import slaynash.text.utils.Localization;

public class MenuPage extends Scene {
	
	public static final String BACKGROUND_PATH = "res/textures/menu/background.png";
	private GUIButton playMulti;
	private GUIButton exit;
	private GUIButton options;
	private GUIButton credits;
	
	@Override
	public void init() {
		
		//Configuration.setUsingTimingDebug(true);
		
		if(!Configuration.isVR()) {
			Configuration.setPlayerCharacter(null);
			UserInputUtil.setMouseGrabbed(false);
			GUIManager.reset();
			GUIManager.setDrawMode(GUIManager.DRAWMODE_MENU);
			GUIManager.hideMenu(false);
			GUIManager.setBackground(BACKGROUND_PATH);
			
			playMulti = new GUIButton(new Vector2i(170, 30), new Vector2i(DisplayManager.getWidth()/2-85, 20), null, GUIManager.ELEMENT_MENU);
			options = new GUIButton(new Vector2i(170, 30), new Vector2i(DisplayManager.getWidth()/2-85, 60), null, GUIManager.ELEMENT_MENU);
			credits = new GUIButton(new Vector2i(170, 30), new Vector2i(DisplayManager.getWidth()/2-85, 100), null, GUIManager.ELEMENT_MENU);
			exit = new GUIButton(new Vector2i(170, 30), new Vector2i(DisplayManager.getWidth()/2-85, 140), null, GUIManager.ELEMENT_MENU);
			
			
			
			playMulti.setText(Localization.getTranslation("MULTIPLAYER"), true);
			playMulti.addGUIButtonListener(new GUIButtonListener() {
				
				@Override
				public void mouseExited(GUIButtonEvent e) { }
				
				@Override
				public void mouseEntered(GUIButtonEvent e) { }
				
				@Override
				public void mousePressed(GUIButtonEvent e) {
					SceneManager.changeScene(ConnectPage.class);
				}
				
				@Override
				public void mouseReleased(GUIButtonEvent e) {
					
				}
			});
			
			options.setText(Localization.getTranslation("OPTIONS"), true);
			options.addGUIButtonListener(new GUIButtonListener() {
				
				@Override
				public void mouseExited(GUIButtonEvent e) { }
				
				@Override
				public void mouseEntered(GUIButtonEvent e) { }
				
				@Override
				public void mousePressed(GUIButtonEvent e) {
					GUIOptionPage.show();
				}
				
				@Override
				public void mouseReleased(GUIButtonEvent e) {}
			});
			
			credits.setText(Localization.getTranslation("CREDITS"), true);
			credits.addGUIButtonListener(new GUIButtonListener() {
				
				@Override
				public void mouseExited(GUIButtonEvent e) { }
				
				@Override
				public void mouseEntered(GUIButtonEvent e) { }
				
				@Override
				public void mousePressed(GUIButtonEvent e) {
					//GUIMiniPopup mp = new GUIMiniPopup();
					//new GUIText("hey", "tahoma", 250, 10, 10, 200, false, mp, 0);
					GUIManager.showPopup(GUIManager.POPUP_INFO, Localization.getTranslation("CREDITS_TEXT"));
				}
				
				@Override
				public void mouseReleased(GUIButtonEvent e) {}
			});
			
			exit.setText(Localization.getTranslation("EXIT_BUTTON"), true);
			exit.addGUIButtonListener(new GUIButtonListener() {
				
				@Override
				public void mouseExited(GUIButtonEvent e) { }
				
				@Override
				public void mouseEntered(GUIButtonEvent e) { }
				
				@Override
				public void mousePressed(GUIButtonEvent e) {
					SceneManager.close();
				}
				
				@Override
				public void mouseReleased(GUIButtonEvent e) {
					
				}
			});
		}
		else {
			Configuration.setPlayerCharacter(new PlayerCharacterVR());
			GUIManager.setDrawMode(GUIManager.DRAWMODE_GAME);
			GUIManager.hideMenu(true);
			VRMenuGUI.init();
			new GUIText("test", 20, 20, 100, null, GUIManager.ELEMENT_GAME);
		}
	}
	
	@Override
	public void start() {
		UserInputUtil.setMouseGrabbed(false);
	}
	
	@Override
	public void update() {
		if(Configuration.getPlayerCharacter() != null) Configuration.getPlayerCharacter().update();
		if(Configuration.isVR()) VRMenuGUI.update();
	}
	
	@Override
	public void render() {
		
		if(Configuration.isVR()) {
			
			VRUtils.setCurrentRenderEye(VRUtils.EYE_CENTER);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
			
	    	ShaderManager.start3DShader();
			
			ShaderManager.shader_loadProjectionMatrix(MatrixUtils.createProjectionMatrix(Configuration.getZNear(), Configuration.getZFar(), Configuration.getFOV()));
			ShaderManager.shader_loadViewMatrix(MatrixUtils.createCharacterViewMatrix());
			ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
			
			VRUtils.renderControllers3D();
	    	VRUtils.renderBaseStations3D();
			
			
	
			ShaderManager.stopShader();
			
			int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[MenuPage] Render> GLERROR: "+err);
			
	
			VRMenuGUI.render3D();
			err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[MenuPage] VRMenuGUI> GLERROR: "+err);
			
		}
	}

	@Override
	public void stop() {
		VRMenuGUI.destroy();
	}

	@Override
	public void resize() {
		GL11.glViewport(0, 0, DisplayManager.getWidth(), DisplayManager.getHeight());
		Text2d.reload();
		playMulti.setPosition(new Vector2i(DisplayManager.getWidth()/2-85, 20));
		options.setPosition(new Vector2i(DisplayManager.getWidth()/2-85, 60));
		credits.setPosition(new Vector2i(DisplayManager.getWidth()/2-85, 100));
		exit.setPosition(new Vector2i(DisplayManager.getWidth()/2-85, 140));
		GUIManager.setBackground(BACKGROUND_PATH);
	}

	@Override
	public void renderVR(int eye) {
		ShaderManager.startVRShader();
    	
    	GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		
		
		ShaderManager.shader_loadProjectionMatrix(VRUtils.getProjectionMatrix(eye));
		ShaderManager.shader_loadViewMatrix(VRUtils.getViewMatrix(eye));
		ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
    	
		VRUtils.renderControllers(eye);
    	VRUtils.renderBaseStations(eye);
		
		
		ShaderManager.stopShader();
		int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[MenuPage] Render [VR]> GLERROR: "+err);
		
		VRMenuGUI.renderVR(eye);
		err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[MenuPage] VRMenuGUI [VR]> GLERROR: "+err);
	}
}
