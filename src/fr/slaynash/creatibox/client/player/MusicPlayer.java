package fr.slaynash.creatibox.client.player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import slaynash.sgengine.Configuration;

public class MusicPlayer extends Thread {
	private static MusicPlayer instance = null;
	private static List<File> soundtracks = new ArrayList<File>(); 

	public static void init() {
		instance = new MusicPlayer();
		instance.setName("MUSICPLAYER");
		instance.setDaemon(true);
		
		loadDirectory(new File(Configuration.getAbsoluteInstallPath()+"/res/musicplayer/"));
	}
	
	@Override
	public void run() {}
	
	public static void startPlayer() {
		instance.start();
	}
	
	public static void render2D() {}
	
	public static void renderVR() {}
	
	public static void play() {}
	
	public static void next() {}
	
	public static void pause() {}
	
	public static void setVolume() {}
	
	public static void setMusicTime() {}
	
	public static int getCurrentTime() {
		return 0;
	}
	
	public static String getCurrentName() {
		return "";
	}
	
	private static void loadDirectory(File directory){
		File[] files = directory.listFiles();
		
		for(File file:files) {
			if(file.isDirectory()) {
				loadDirectory(file);
			}
			else if(file.isFile() && file.getName().endsWith(".wav")) {
				soundtracks.add(file);
			}
		}
	}
	
}
