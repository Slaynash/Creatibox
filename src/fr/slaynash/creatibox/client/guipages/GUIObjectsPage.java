package fr.slaynash.creatibox.client.guipages;

import org.lwjgl.opengl.Display;

import slaynash.sgengine.gui.GUIFrame;
import slaynash.sgengine.gui.GUIManager;
import slaynash.text.utils.Localization;

public class GUIObjectsPage {
	
	private static GUIFrame frame;
	
	public static void show(){
		if(isShown()) return;
		frame = new GUIFrame((int)(Display.getWidth()*0.05), (int)(Display.getHeight()*0.05), (int)(Display.getWidth()*0.9), (int)(Display.getHeight()*0.9), GUIManager.ELEMENT_GAME);
		frame.setTitle(Localization.getTranslation("object_menu"));
		
		
		
	}

	public static void destroy() {
		frame.destroy();
	}
	
	public static boolean isShown() {
		return frame != null && !frame.isDestroyed();
	}
	
}
