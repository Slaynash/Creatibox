package fr.slaynash.creatibox.launch;

import slaynash.sgengine.Configuration;
import slaynash.sgengine.utils.PageManager;

public class Start {
	
	public static void main(String installPath) {
		Configuration.setInstallPath(installPath);
		Configuration.setSSAASamples(1);
		Configuration.setVRSSAASamples(4);
		PageManager.init(800, 600, false, null);
	}
	
}
