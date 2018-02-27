package fr.slaynash.creatibox.launch;

import java.io.FileNotFoundException;
import java.io.IOException;

import fr.slaynash.creatibox.client.UserData;
import fr.slaynash.creatibox.client.pages.MenuPage;
import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.RessourceLoader;
import slaynash.sgengine.Configuration;
import slaynash.sgengine.utils.SceneManager;
import slaynash.sgengine.utils.sceneManagerEvent.SceneManagerAdapter;
import slaynash.sgengine.utils.sceneManagerEvent.SceneManagerEvent;
import slaynash.text.utils.Localization;

public class StartClient {
	
	public static void main(String installPath, String[] args) {
		Configuration.setInstallPath(installPath);
		if(args.length != 0) {
			for(String arg:args) {
				if(arg.equals("--vr")) {
					Configuration.enableVSync(false);
					Configuration.enableVR(true);
					Configuration.setVRSSAASamples(8);
					Configuration.setSSAASamples(2);
				}
				else if(arg.startsWith("--uuid=")) {
					UserData.uuid = Integer.parseInt(arg.substring(7));
				}
				else if(arg.startsWith("--username=")) {
					UserData.playername = arg.substring(11);
				}
				else if(arg.startsWith("--ip=")) {
					GameValues.serverAddress = arg.substring(5);
				}
				else if(arg.startsWith("--lang=")) {
					Localization.setLang(arg.substring(7));
				}
			}
		}
		if(!Configuration.isVR()) {
			Configuration.setSSAASamples(8);
			Configuration.enableVSync(true);
		}
		Configuration.enableVSync(true);
		
		Localization.loadLangFile("fr", Configuration.getAbsoluteInstallPath()+"/res/localization/fr.txt");
		Localization.loadLangFile("en", Configuration.getAbsoluteInstallPath()+"/res/localization/en.txt");
		

		SceneManager.addSceneManagerListener(new SceneManagerAdapter() {
			
			@Override
			public void initialized(SceneManagerEvent arg0) {
				try {
					RessourceLoader.loadRessources();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void exited(SceneManagerEvent arg0) {}
		});
		
		SceneManager.init(1366, 768, false, MenuPage.class);
	}
}
