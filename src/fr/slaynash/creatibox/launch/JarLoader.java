package fr.slaynash.creatibox.launch;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader {
	public static void loadJar(String path){
		File f = new File(path);
		try {
			URI u = f.toURI();
			if(!f.exists()) throw new FileNotFoundException();
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
			Class<URLClassLoader> urlClass = URLClassLoader.class;
			Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
			method.setAccessible(true);
			method.invoke(urlClassLoader, new Object[]{u.toURL()});
			System.out.println("[JarLoader] jar file "+f.getName()+" loaded.");
		} catch (IllegalAccessException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("[JarLoader] Unable to load jar file at "+f.getAbsolutePath()+" ! D:");
			e.printStackTrace();
		}
	}
}