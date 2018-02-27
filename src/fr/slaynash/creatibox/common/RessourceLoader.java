package fr.slaynash.creatibox.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.slaynash.creatibox.common.entities.EntityDef;
import fr.slaynash.creatibox.common.entities.EntityManager;
import slaynash.sgengine.Configuration;

public class RessourceLoader {
	
	//Entities
	//Scripts
	
	private static boolean loaded = false;

	public static void loadRessources() throws FileNotFoundException, IOException {
		if(loaded) return;
		loaded = true;
		readDirectory(new File(Configuration.getAbsoluteInstallPath()+"/res/descfiles/"));
	}
	
	private static void readDirectory(File directory) throws FileNotFoundException, IOException {
		File[] files = directory.listFiles();
		
		for(File file:files) {
			if(file.isDirectory()) {
				readDirectory(file);
			}
			else if(file.isFile() && file.getName().endsWith(".sge")) {
				readFile(file);
			}
		}
	}

	private static void readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		int tabs = 0;
		boolean rcheck = false;
		
		while(rcheck || (line = reader.readLine()) != null) {rcheck = false; tabs = line.split("\\t").length-1;
			if(tabs > 0) continue;
			
			if(line.equals("entities")) {
				while(rcheck || (line = reader.readLine()) != null) {rcheck = false; tabs = line.split("\\t").length-1;
					if(tabs > 1) continue; if(tabs < 1) {rcheck=true;break;};
					
					EntityDef entity = EntityManager.createEntityDef(line.trim());
					while(rcheck || (line = reader.readLine()) != null) {rcheck = false; tabs = line.split("\\t").length-1;
						if(tabs > 2) continue; if(tabs < 2) {rcheck=true;break;};
						line = line.trim();
						entity.addParameter(line);
					}
					EntityManager.registerEntityDef(entity);
					
				}
			}
			
		}
		
		reader.close();
	}
}
