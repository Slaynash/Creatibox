package fr.slaynash.creatibox.launch;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Launch {
	private static final boolean LOAD_JARS = true;
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
			
			"vr/jopenvr-1.0.7_2",
			"vr/JRift-1.3.0.0.1",
			
			"jamepad_sge_edition_2607171819",
			"sge_5ab2e3c"
	};
	
	public static void main(String[] args) {
		String decodedPath = "";
		try {
			String path = Launch.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			decodedPath = new File(URLDecoder.decode(path, "UTF-8")).getParent();
			System.out.println("[Launch] Game directory: "+decodedPath);
		}catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
		if(LOAD_JARS){
			System.out.println("loading libraries...");
			for(String jar:jars) JarLoader.loadJar(decodedPath+"/libs/"+jar+".jar");
		}
		
		Start.main(decodedPath);
	}
}
