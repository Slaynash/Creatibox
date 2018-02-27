package fr.slaynash.creatibox.client.pages;

import javax.vecmath.Quat4f;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import fr.slaynash.creatibox.client.Test;
import fr.slaynash.creatibox.client.UserData;
import fr.slaynash.creatibox.client.connection.ConnectionManager;
import fr.slaynash.creatibox.client.connection.ConnectionStateHandler;
import fr.slaynash.creatibox.client.guipages.GUIObjectsPage;
import fr.slaynash.creatibox.client.guipages.GUIOptionPage;
import fr.slaynash.creatibox.client.multiplayer.PlayerCharacterPCMulti;
import fr.slaynash.creatibox.client.multiplayer.PlayerCharacterVRMulti;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.common.entities.EntityManager;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.gui.GUIManager;
import slaynash.sgengine.gui.GUIText;
import slaynash.sgengine.gui.button.GUIButton;
import slaynash.sgengine.gui.button.GUIButtonEvent;
import slaynash.sgengine.gui.button.GUIButtonListener;
import slaynash.sgengine.inputs.KeyboardControlManager;
import slaynash.sgengine.maths.Vector2i;
import slaynash.sgengine.models.Renderable3dModel;
import slaynash.sgengine.models.utils.VaoManager;
import slaynash.sgengine.shaders.ShaderManager;
import slaynash.sgengine.textureUtils.TextureManager;
import slaynash.sgengine.utils.MatrixUtils;
import slaynash.sgengine.utils.Scene;
import slaynash.sgengine.utils.SceneManager;
import slaynash.sgengine.utils.ShapeHelper;
import slaynash.sgengine.utils.UserInputUtil;
import slaynash.sgengine.utils.VRUtils;
import slaynash.sgengine.world3d.CollisionManager3d;
import slaynash.text.utils.Localization;

public class GamePage extends Scene implements ConnectionStateHandler {
	
	private boolean hide = true;
	private boolean mouseGrabbed = true;
	private boolean esc = false;
	private boolean f3 = false;
	private int fpsCount = 0;
	private long lastFPS = 0;
	private GUIText framerateText = null;

	private String connectionStatus;
	private boolean connectionErrored;
	private Renderable3dModel pylonModel;
	private Renderable3dModel groundModel;
	
	private GUIText fpsText;
	
	public static final String BACKGROUND_PATH = "res/textures/menu/game_background.png";
	private GUIButton resume;
	private GUIButton options;
	private GUIButton backToMenu;
	private GUIButton exit;
	private boolean create1, create2, create3, create4;
	
	@Override
	public void init() {
		
		//KeyboardControlManager.registerControl("object_menu", Keyboard.KEY_A);
		KeyboardControlManager.registerControl("create_object_test_1", Keyboard.KEY_1);
		KeyboardControlManager.registerControl("create_object_test_2", Keyboard.KEY_2);
		KeyboardControlManager.registerControl("create_object_test_3", Keyboard.KEY_3);
		KeyboardControlManager.registerControl("create_object_test_4", Keyboard.KEY_4);
		
		ConnectionManager.setConnectionStateHandler(this);
		Configuration.setCollisionManager3dEnabled(true);
		GUIManager.reset();
		GUIManager.setDrawMode(GUIManager.DRAWMODE_GAME);
		GUIManager.setBackground(BACKGROUND_PATH);
		
		int cx = Display.getWidth()/2;
		int cy = Display.getHeight()/2;
		
		resume = new GUIButton(new Vector2i(170, 30), new Vector2i(cx-85, cy-70), null, GUIManager.ELEMENT_MENU);
		options = new GUIButton(new Vector2i(170, 30), new Vector2i(cx-85, cy-30), null, GUIManager.ELEMENT_MENU);
		backToMenu = new GUIButton(new Vector2i(170, 30), new Vector2i(cx-85, cy+10), null, GUIManager.ELEMENT_MENU);
		exit = new GUIButton(new Vector2i(170, 30), new Vector2i(cx-85, cy+50), null, GUIManager.ELEMENT_MENU);
		
		
		resume.setText(Localization.getTranslation("RESUME"), true);
		resume.addGUIButtonListener(new GUIButtonListener() {
			
			@Override
			public void mouseExited(GUIButtonEvent e) { }
			
			@Override
			public void mouseEntered(GUIButtonEvent e) { }
			
			@Override
			public void mousePressed(GUIButtonEvent e) {
				hide = true;
				GUIManager.hideMenu(hide);
				UserInputUtil.setMouseGrabbed(hide && mouseGrabbed);
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
		
		backToMenu.setText(Localization.getTranslation("BACKTOMENU"), true);
		backToMenu.addGUIButtonListener(new GUIButtonListener() {
			
			@Override
			public void mouseExited(GUIButtonEvent e) { }
			
			@Override
			public void mouseEntered(GUIButtonEvent e) { }
			
			@Override
			public void mousePressed(GUIButtonEvent e) {
				ConnectionManager.disconnectInternal("Disconnected by user");
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
		
		
		
		
		
		
		
		
		
		
		CollisionManager3d.reload();
		if(!Configuration.isVR()) {
			Configuration.setPlayerCharacter(new PlayerCharacterPCMulti());
			KeyboardControlManager.loadDefaultControls();
		}
		else {
			Configuration.setPlayerCharacter(new PlayerCharacterVRMulti());
		}
		
		CollisionShape groundShape = new StaticPlaneShape(new javax.vecmath.Vector3f(0, 1, 0), 1);
		Transform transform = new Transform();
		transform.origin.set(new javax.vecmath.Vector3f(new float[] {0,-1,0}));
		transform.setRotation(new Quat4f(0, 0, 0, 1));
		DefaultMotionState groundMotionState = new DefaultMotionState(transform);
		
		RigidBodyConstructionInfo constructionInfo = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new javax.vecmath.Vector3f(0,0,0));
		
		RigidBody ground = new RigidBody(constructionInfo);
		CollisionManager3d.getDynamicWorld().addRigidBody(ground);
	}
	
	@Override
	public void start() {
		GUIManager.hideMenu(true);
		if(!Configuration.isVR()) {
			UserInputUtil.setMouseGrabbed(mouseGrabbed);
		}
		else {
			
		}
		
		float pylonS = 0.1f;
		float pylonH = 100f;
		
		float[] pylonVertices = new float[] {
				 pylonS, -pylonH, -pylonS,
				 pylonS,  pylonH, -pylonS,
				 pylonS,  pylonH,  pylonS,
				 pylonS,  pylonH,  pylonS,
				 pylonS, -pylonH,  pylonS,
				 pylonS, -pylonH, -pylonS,
				
				 pylonS, -pylonH,  pylonS,
				 pylonS,  pylonH,  pylonS,
				-pylonS,  pylonH,  pylonS,
				-pylonS,  pylonH,  pylonS,
				-pylonS, -pylonH,  pylonS,
				 pylonS, -pylonH,  pylonS,
				
				-pylonS, -pylonH,  pylonS,
				-pylonS,  pylonH,  pylonS,
				-pylonS,  pylonH, -pylonS,
				-pylonS,  pylonH, -pylonS,
				-pylonS, -pylonH, -pylonS,
				-pylonS, -pylonH,  pylonS,
				
				 pylonS,  pylonH, -pylonS,
				 pylonS, -pylonH, -pylonS,
				-pylonS, -pylonH, -pylonS,
				-pylonS, -pylonH, -pylonS,
				-pylonS,  pylonH, -pylonS,
				 pylonS,  pylonH, -pylonS
		};
		
		float[] pylonUvs = new float[] {
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0,
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0,
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0,
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0
		};
		
		int[] pylonIndices = new int[] {
				0, 1, 2, 3, 4, 5,
				6, 7, 8, 9, 10, 11,
				12, 13, 14, 15, 16, 17,
				18, 19, 20, 21, 22, 23
		};
		
		pylonModel = new Renderable3dModel(VaoManager.loadToVao3d(pylonVertices, pylonUvs, ShapeHelper.calculateNormals(pylonVertices), ShapeHelper.calculateTangents(pylonVertices, pylonUvs), pylonIndices), TextureManager.getTextureDef("res/textures/pylonG.png", TextureManager.TEXTURE_DIFFUSE), null, TextureManager.getTextureDef("res/textures/pylon_spec.png", TextureManager.TEXTURE_SPECULAR));
		
		
		float[] groundVertices = new float[] {
				-100, 0, -100,
				-100, 0,  100,
				 100, 0,  100,
				 100, 0,  100,
				 100, 0, -100,
				-100, 0, -100
		};
		
		float[] groundUvs = new float[] {
				0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0
		};
		
		int[] groundIndices = new int[] {0, 1, 2, 3, 4, 5};
		
		groundModel = new Renderable3dModel(VaoManager.loadToVao3d(groundVertices, groundUvs, ShapeHelper.calculateNormals(groundVertices), ShapeHelper.calculateTangents(groundVertices, groundUvs), groundIndices), TextureManager.getTextureDef("res/textures/ground.png", TextureManager.TEXTURE_DIFFUSE), null, null);
		
		fpsText = new GUIText("FPS: ?", 5, 5, 400, null, GUIManager.ELEMENT_GAME);
		new GUIText("UUID: "+UserData.uuid, 5, 25, 400, null, GUIManager.ELEMENT_GAME);
		
		lastFPS = (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	@Override
	public void update() {
		
		if(connectionErrored){
			GUIManager.showPopup(GUIManager.POPUP_ERROR, connectionStatus);
			SceneManager.changeScene(MenuPage.class);
			return;
		}
		
		updateFPS();
		Configuration.getPlayerCharacter().update();
		
		
		if(!Configuration.isVR()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				if (!esc){
					esc = true;
					hide = !hide;
					GUIManager.hideMenu(hide);
					UserInputUtil.setMouseGrabbed(hide && mouseGrabbed && !GUIObjectsPage.isShown());
				}
			}
			else esc = false;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_F3)){
				if (!f3){
					f3 = true;
					mouseGrabbed = !mouseGrabbed;
					UserInputUtil.setMouseGrabbed(mouseGrabbed && hide && !GUIObjectsPage.isShown());
				}
			}
			else f3 = false;
			if(hide) ((PlayerCharacterPCMulti) Configuration.getPlayerCharacter()).updateControls();
			
			if(KeyboardControlManager.isPressed("object_menu")) {
				if(!GUIObjectsPage.isShown()) {
					GUIObjectsPage.show();
					UserInputUtil.setMouseGrabbed(false);
				}
			}
			else {
				if(GUIObjectsPage.isShown()) {
					GUIObjectsPage.destroy();
					UserInputUtil.setMouseGrabbed(mouseGrabbed && hide && !GUIObjectsPage.isShown());
				}
			}
			
			if(KeyboardControlManager.isPressed("create_object_test_1")) {
				if(!create1)Test.createObjectTest("prop_cylinder05x1");
				create1 = true;
			}else create1 = false;
			
			if(KeyboardControlManager.isPressed("create_object_test_2")) {
				if(!create2) Test.createObjectTest("prop_plate05x005x1");
				create2 = true;
			}else create2 = false;
			
			if(KeyboardControlManager.isPressed("create_object_test_3")) {
				if(!create3) Test.createObjectTest("prop_plate1x005x1");
				create3 = true;
			}else create3 = false;
			
			if(KeyboardControlManager.isPressed("create_object_test_4")) {
				if(!create4) Test.createObjectTest("pl_cube");
				create4 = true;
			}else create4 = false;
		}
	}
	
	@Override
	public void render() {
		if(!Configuration.isVR()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
			
			ShaderManager.start3DShader();
			
			ShaderManager.shader_loadProjectionMatrix(MatrixUtils.createProjectionMatrix(Configuration.getZNear(), Configuration.getZFar(), Configuration.getFOV()));
			ShaderManager.shader_loadViewMatrix(MatrixUtils.createCharacterViewMatrix());
			
			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 0, 1));
			groundModel.render();
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonG.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();

			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 90, 0, 0, 1));
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonB.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();

			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 90, 1));
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonR.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();
			
			EntityManager.renderEntities3D();
			
			for(int i=0;i<ConnectionManager.getPlayers().size();i++) {
				ConnectionManager.getPlayers().get(i).render();
			}
			
			int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[GamePage] Render> GLERROR:"+err);
		}
		else {
			
			ShaderManager.start3DShader();
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);

			ShaderManager.shader_loadProjectionMatrix(MatrixUtils.createProjectionMatrix(Configuration.getZNear(), Configuration.getZFar(), Configuration.getFOV()));
			ShaderManager.shader_loadViewMatrix(MatrixUtils.createCharacterViewMatrix());
			ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
			
			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 0, 1));
			groundModel.render();
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonG.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();

			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 90, 0, 0, 1));
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonB.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();
			
			ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 90, 1));
			pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonR.png", TextureManager.TEXTURE_DIFFUSE));
			pylonModel.render();
			
			EntityManager.renderEntities3D();
			
	    	for(int i=0;i<ConnectionManager.getPlayers().size();i++) {
				ConnectionManager.getPlayers().get(i).render();
			}
			
			VRUtils.renderControllers3D();
	    	VRUtils.renderBaseStations3D();
			int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[GamePage] Render> GLERROR:"+err);
		}
		
		int err = 0; if((err = GL11.glGetError()) != 0) System.out.println("[GamePage] Render2d> GLERROR:"+err);
		
	}

	private void updateFPS() {
		long ct = (Sys.getTime() * 1000) / Sys.getTimerResolution();
		
		if (ct - lastFPS > 1000) {
			if(framerateText != null) framerateText.setText("FPS: "+fpsCount);
			fpsText.setText("FPS: "+fpsCount);
			fpsCount = 0;
			lastFPS += 1000;
		}
		fpsCount++;
		
	}

	@Override
	public void renderVR(int eye) {
		ShaderManager.startVRShader();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.443f, 0.784f, 0.882f, 1);
		
		ShaderManager.shader_loadProjectionMatrix(VRUtils.getProjectionMatrix(eye));
		ShaderManager.shader_loadViewMatrix(VRUtils.getViewMatrix(eye));
		ShaderManager.shader_loadTransformationMatrix(new Matrix4f());
		
		ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 0, 1));
		groundModel.render();
		pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonG.png", TextureManager.TEXTURE_DIFFUSE));
		pylonModel.renderVR(eye);

		ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 90, 0, 0, 1));
		pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonB.png", TextureManager.TEXTURE_DIFFUSE));
		pylonModel.renderVR(eye);

		ShaderManager.shader_loadTransformationMatrix(MatrixUtils.createTransformationMatrix(new Vector3f(), 0, 0, 90, 1));
		pylonModel.setTextureColor(TextureManager.getTextureDef("res/textures/pylonR.png", TextureManager.TEXTURE_DIFFUSE));
		pylonModel.renderVR(eye);

		EntityManager.renderEntitiesVR(eye);
		
		for(int i=0;i<ConnectionManager.getPlayers().size();i++) {
			ConnectionManager.getPlayers().get(i).renderVR(eye);
		}
		
		VRUtils.renderControllers(eye);
    	VRUtils.renderBaseStations(eye);
		int err = 0; if((err = GL11.glGetError()) != 0) System.out.println(LogTimer.getTimeForLog()+"[GamePage] RenderVR> GLERROR:"+err);
		
		ShaderManager.stopShader();
	}

	@Override
	public void resize() {
		int cx = Display.getWidth()/2;
		int cy = Display.getHeight()/2;
		resume.setPosition(new Vector2i(cx-85, cy-70));
		options.setPosition(new Vector2i(cx-85, cy-30));
		backToMenu.setPosition(new Vector2i(cx-85, cy+10));
		exit.setPosition(new Vector2i(cx-85, cy+50));
		GUIManager.setBackground(BACKGROUND_PATH);
	}

	@Override
	public void stop() {
		if(ConnectionManager.getState() != ConnectionManager.DISCONNECTED)
			ConnectionManager.disconnectInternal("game closed");
		ConnectionManager.setConnectionStateHandler(null);
		CollisionManager3d.destroyDynamicWorld();
		EntityManager.clear();
		Configuration.setCollisionManager3dEnabled(false);
	}

	@Override
	public void handleConnectException(Exception e) {}

	@Override
	public void connectionFinished() {}

	@Override
	public void connectionStateChanged(int state) {
		//System.out.println(LogTimer.getTimeForLog()+"[GamePage] state changed to "+state+" !");
		if(state == ConnectionManager.DISCONNECTED){
			connectionStatus = Localization.getTranslation("DISCONNECTED_FROM_SERVER")+": "+ConnectionManager.getDisconnectMessage();
			connectionErrored = true;
			return;
		}
		else connectionStatus = Localization.getTranslation("CONNECTION_STATE_"+state);
	}
	
}
