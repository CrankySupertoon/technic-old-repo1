package net.minecraft.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

public class mod_XieFoodStacking extends BaseMod {

	final static String modName = "Xie Food Stacking";
	final static String version = "1.7b";
	final static String settingsFile = "foodstacking.ini";
	
	Properties props;
	static final String settingsPath = "/mods/Xie/";
	
	// food stack size
	public int foodStackSize = 16; // default to 16
	
	public mod_XieFoodStacking() {
		props = loadProperties(settingsFile, defaultProperties());
		
		foodStackSize = Integer.parseInt(props.getProperty("foodStackSize",""+foodStackSize));
	}
	
    public void ModsLoaded()
    {
    	
    	for (int i=0; i<Item.itemsList.length; i++) {
    		if (isFoodButNotCookie(Item.itemsList[i])) Item.itemsList[i].setMaxStackSize(foodStackSize);
    	}
    }

	
	private String defaultProperties() {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		
		try {
			bw.write("# "+this.toString()+" settings file");
			bw.newLine();bw.newLine();
			bw.write("# Food Stack Size");
			bw.newLine();
			bw.write("foodStackSize="+foodStackSize);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.toString();
	}
    
	public String Version() {
		return version;
	}
	
	public String ModName() {
		return modName;
	}
	
	public String toString()
	{
		return (new StringBuilder(ModName())).append(" ").append(Version()).toString();
	}
	
	public static boolean isFood(Item i) {
		return i instanceof ItemFood;
	}
	
	public static boolean isCookieLike(Item i) {
		return i instanceof ItemCookie;
	}
	
	public static boolean isFoodButNotCookie(Item i) {
		return isFood(i) && !isCookieLike(i);
	}
	
	public static Properties loadProperties (String settingsFile, String defaults) {
		File dir = ModLoader.getMinecraftInstance().getMinecraftDir();
		File file = new File(dir.getPath()+settingsPath+settingsFile);
		Properties props = new Properties();
		try {
			props.load(new FileReader(file));
		} catch (FileNotFoundException e) {
			try {
				// check for path existence
				File path = new File(dir.getPath()+settingsPath);
				if (!path.exists()) path.mkdirs();
				
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				fw.write(defaults);
				fw.close();
			} catch (IOException e1) { e1.printStackTrace(); }	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return props;
	}
}
