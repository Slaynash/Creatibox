package fr.slaynash.creatibox.client.guipages;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import slaynash.sgengine.gui.GUIFrame;
import slaynash.sgengine.gui.GUIManager;
import slaynash.sgengine.gui.GUIText;
import slaynash.sgengine.gui.button.GUIButton;
import slaynash.sgengine.gui.button.GUIButtonEvent;
import slaynash.sgengine.gui.button.GUIButtonListener;
import slaynash.sgengine.gui.checkBox.GUICheckBox;
import slaynash.sgengine.gui.comboBox.GUIComboBox;
import slaynash.sgengine.maths.Vector2i;
import slaynash.sgengine.utils.DisplayManager;
import slaynash.text.utils.Localization;

public class GUIOptionPage {
	
	private static GUIFrame frame;
	
	public static void show(){
		if(frame != null && !frame.isDestroyed()) return;
		frame = new GUIFrame(20, 20, 600, 400, GUIManager.ELEMENT_MENU);
		frame.setTitle(Localization.getTranslation("option_menu"));
		
		GUIButton apply = new GUIButton(new Vector2i(120, 30), new Vector2i(238, 350), frame, 0);
		apply.setText(Localization.getTranslation("apply"), true);
		List<DisplayMode> dt = new ArrayList<DisplayMode>();
		for(DisplayMode d:DisplayManager.getAvailableResolutions()) if(d.getFrequency() == 60) dt.add(d);
		
		new GUIText(Localization.getTranslation("resolution"), "tahoma", 250, 5, 8, 200, true, frame, 0);
		
		final GUIComboBox<DisplayMode> cb = new GUIComboBox<DisplayMode>(5, 25, 200, dt, frame, 0);
		
		final GUICheckBox ckb = new GUICheckBox(5, 55, 16, Display.isFullscreen(), frame, 0);
		
		new GUIText(Localization.getTranslation("fullscreen"), "tahoma", 250, 35, 55, 200, false, frame, 0);
		
		
		apply.addGUIButtonListener(new GUIButtonListener() {
			
			@Override
			public void mouseReleased(GUIButtonEvent e) {
				DisplayManager.setDisplayMode(cb.getSelectedItem(), ckb.isChecked());
			}
			
			@Override
			public void mousePressed(GUIButtonEvent e) {}
			
			@Override
			public void mouseExited(GUIButtonEvent e) {}
			
			@Override
			public void mouseEntered(GUIButtonEvent e) {}
		});
		
		
	}
	
}
