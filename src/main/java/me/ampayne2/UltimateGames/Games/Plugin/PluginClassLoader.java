package me.ampayne2.UltimateGames.Games.Plugin;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * This class allows to load plugins .jars
 */
public class PluginClassLoader extends URLClassLoader{

	public PluginClassLoader() {
		super(new URL[] {});
	}

	public void addUrl(URL url) {
		this.addURL(url);
	}

}
