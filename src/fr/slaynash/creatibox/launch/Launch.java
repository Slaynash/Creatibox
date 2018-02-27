package fr.slaynash.creatibox.launch;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import fr.slaynash.creatibox.common.GameValues;
import fr.slaynash.creatibox.common.LogTimer;
import fr.slaynash.creatibox.server.Server;

public class Launch {
	private static final boolean LOAD_JARS = true;
	private static boolean serverMode = false;
	static final String[] jars = new String[]{
			"audio/CodecWav",
			"audio/LibraryLWJGLOpenAL",
			"audio/SoundSystem",
			
			"jna/jna-4.2.2",
			
			"lwjgl/lwjgl",
			"lwjgl/lwjgl_util",
			"lwjgl/slick-util",
			
			"physics/vecmath",
			"physics/jbullet",
			"physics/jbox2d_sge_edition_0808171240",
			
			"vr/jopenvr-1.0.7_2",
			"vr/JRift-1.3.0.0.1",
			
			"multiplayer/rudpLib-b9a25a4",
			
			"jamepad_sge_edition_2607171819",
			"sge_d3806c3",
			"localizationUtils_1792f78"
	};
	
	public static void main(String[] args) {
		if(args.length != 0) {
			for(String arg:args) if(arg.equals("--server")) {
				serverMode = true;
				GameValues.setServer(true);
			}
		}
		String decodedPath = "";
		try {
			String path = Launch.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			decodedPath = new File(URLDecoder.decode(path, "UTF-8")).getParent();
			System.out.println(LogTimer.getTimeForLog()+"[Launch] Game directory: "+decodedPath);
		}catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
		if(LOAD_JARS){
			System.out.println(LogTimer.getTimeForLog()+"[Launch] loading libraries...");
			for(String jar:jars) JarLoader.loadJar(decodedPath+"/libs/"+jar+".jar");
		}
		if(serverMode) Server.start(decodedPath, args, System.in);
		else StartClient.main(decodedPath, args);
	}
}
