package net.minecraft.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Properties;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class mod_XieHunger extends BaseMod {

	final static String modName = "Xie Hunger";
	final static String version = "1.7b";
	final static String settingsFile = "hunger.ini";
	
	static final String settingsPath = "/mods/Xie/";
	static final String hungerSaveFilePath = "/mods/Xie/Hunger";
	
	final static String iconFile = "/Xie/img/xiehunger.png";
	
	// Hunger Settings
	public static int hungerRate = 60; // now # hungerClockTickRate ticks
	public static boolean passiveRegen = false;
	public static int hungerMax = 20;
	private static int hungerStateFactor = 5; // how much hunger per hunger state (designed as max/4)
	
	// Thirst Settings
	public static int thirstRate = 20; // now # hungerClockTickRate ticks
	public static boolean waterDrinkable = true;
	public static int thirstMax = 20;
	private static int thirstStateFactor = 5; // how much thirst per thirst state (designed as max/4)
	
	// Fatigue Settings
	public static int fatigue = 0;
	public static int fatigueMax = 1000;
	public static int fatigueScaledMax = 80; // scaled maximum (set to 80 to display bar within 80 pixels)
	public static int fatigueScaled = 0; // scaled by ScaledMax (for displaying within N pixels)
	public static int fatigueStateFactor = fatigueMax/4; // max/4
	public static int fatigueState = 0;
	private static int fatigueHeartScaleFactor = fatigueMax/20; // max/20 -> scale factor to get fatigue scaled same as half-hearts
	
	// fatigue rates [passive, walking, jumping, swimming, swinging (tool/weapon/fists), sneaking sleeping]
	// per fatigue clock rate
	public static int [] fatigueRate = {0,2,5,5,5,5,-1,-3};
	
	// clock rate
	public static int hungerClockTickRate = 20; // time between hunger mod ticks (20 clock ticks per second)
	private static long timeOfLastTick = 0; // for ensuring correct time scaling of the effects
	
	// Enables
	public static boolean hungerEnabled = true;
	public static boolean thirstEnabled = true;
	public static boolean fatigueEnabled = true;
	
	// Icon vs Bar toggle
	public static boolean useBars = true;
	
	// State variables
	public static int hunger = 0; // same scale as hearts. When hunger=20 you start starving to death (further hunger deals damage)
	public static int thirst = 0; // same scale as hearts. When thirst=20, you start dying of dehyrdation (further thirst deals damage)
	public static int hungerState = 0; // 0 - none, 1 - slight, 2 - moderate, 3 - severe, 4 - critical
	public static int thirstState = 0; // hungerState = floor(hunger/5) etc
	public static boolean dying = false;
	
	// Bear Grylls' Contribution
	private static boolean gryllsEnabled = false;

	public mod_XieHunger() {
		XieFoodLists.init();
		
		loadProperties(settingsFile, defaultProperties());
		
		// set update for each clock tick
		ModLoader.SetInGameHook(this, true, true);
		ModLoader.SetInGUIHook(this, true, true);	
	}
	
	public void ModsLoaded() {
		// check for presence of farming mod and if so add support for orange juice and lemonade
		if (ModLoader.isModLoaded("mod_XieFarming")) {
			XieFoodLists.juiceList.add(XieMod.lemonade);
			XieFoodLists.juiceList.add(XieMod.orangeJuice);
			XieFoodLists.addItemsToList(new Item [] {XieMod.orange, XieMod.lemon, XieMod.lettuce,
											XieMod.tomato,XieMod.watermelonPiece, XieMod.fruitSalad}, XieFoodLists.juicyList);
			
			gryllsEnabled = true;
		}
		
		// check for presence of cooking mod and add support for soups and stews
		if (ModLoader.isModLoaded("mod_XieCooking")) {
			XieFoodLists.juicyList.add(XieMod.soup);
			XieFoodLists.juicyList.add(XieMod.stew);
		}
	}
	
	private static int tickCounter = 0;
	public boolean OnTickInGame(Minecraft game) {
		gameTick(game);
		return true;
	}
	public void OnTickInGui(Minecraft game) {
		gameTick(game);
	}
	
	private String currentWorld = "";
	private int saveStateTick=0;
	private final int ticksBetweenSaves = 40; // saves every 2 seconds
	public void gameTick(Minecraft game) {
		// first up check that we are in the right world
		if (!currentWorld.equals(game.theWorld.worldInfo.getWorldName())) {
			// attempt to load info from file, if present
			loadStateFromFile(game);
			currentWorld=game.theWorld.worldInfo.getWorldName();
		}
		
		if (++tickCounter>=hungerClockTickRate) {
			tickCounter=0;
			hungerClockTick(game);
		}
		
		if (++saveStateTick>=ticksBetweenSaves) {
			saveStateTick=0;
			saveStateToFile(game);
		}
	}
	
	private static int hungerTicks=0;
	private static int thirstTicks=0;
	public void hungerClockTick(Minecraft game) {
		// determine scaling factor for the amount of time that has passed
		if (timeOfLastTick==0) timeOfLastTick=game.theWorld.getWorldTime(); // check to see if this is the first ever tick
		long scalingFactor = 1; // default to 1
		long dt = game.theWorld.getWorldTime() - timeOfLastTick;
		if (dt>hungerClockTickRate) scalingFactor = dt/hungerClockTickRate;
		timeOfLastTick = game.theWorld.getWorldTime();
		
		if (passiveRegen && inTheGreen()) healPlayer(game, scalingFactor);

		if (hungerEnabled) doHunger(game, scalingFactor);

		if (thirstEnabled) doThirst(game, scalingFactor);

		if (fatigueEnabled) doFatigue(game, scalingFactor, dt);
		
		if (gryllsEnabled) doGrylls(game);
		
		stateUpdate();
    }
	
	private static void stateUpdate () {
		// bounds check
		if (hunger<0) hunger=0;
		if (hunger>hungerMax) hunger=hungerMax;
		if (thirst<0) thirst=0;
		if (thirst>thirstMax) thirst=thirstMax;
		if (fatigue<0) fatigue=0;
		if (fatigue>fatigueMax) fatigue=fatigueMax;
		
		// dying state check
		if (hunger>=hungerMax || thirst>=thirstMax || fatigue>=fatigueMax) dying=true;
		else dying=false;
		
		// hunger and thirst states
		hungerState=(int) Math.floor(hunger/hungerStateFactor);
		thirstState=(int) Math.floor(thirst/thirstStateFactor);
		
		// update fatigue state
		if (fatigue<=fatigueMax) {
			fatigueScaled=fatigue*fatigueScaledMax/fatigueMax;
			fatigueState=(int) Math.floor(fatigue/fatigueStateFactor);
		} else  {
			fatigueScaled=fatigueScaledMax;
			fatigueState=(int) Math.floor(fatigueMax/fatigueStateFactor);
		}
	}
	
	private void doHunger(Minecraft game, long scale) {
		hungerTicks+=scale;
		// check for hunger increase
		if (hungerTicks>=hungerRate) {
			int amt = hungerTicks/hungerRate;
			hungerTicks=0;
			hunger+=amt;
			if (hunger>=hungerMax) {
				dying=true;
				hurtPlayer(game, hunger-hungerMax);
				hunger=hungerMax;
			}
		}
	}
	
	private void doThirst(Minecraft game, long scale) {
		thirstTicks+=scale;
		if (thirstTicks>=thirstRate) {
			int amt = thirstTicks/thirstRate;
			thirstTicks=0;
			thirst+=amt;
			if (thirst>=thirstMax) {
				dying=true;
				hurtPlayer(game, thirst-thirstMax);
				thirst=thirstMax;
			}
		}

		// check to see if you're crouching in water, in which case drink your fill!
		if (waterDrinkable && game.thePlayer.isInWater() && game.thePlayer.isSneaking() && game.thePlayer.moveForward==0 && game.thePlayer.moveStrafing==0) {
			// we want to speed this up so you're thirst is fully quenched in about 5 seconds,
			// i.e. remove thirstMax in 100 clock ticks
			thirst-=hungerClockTickRate*thirstMax/100;
		}
	}
	
	private int fatigueOverflow = 0;
	private void doFatigue(Minecraft game, long scale, long dt)  {		
		// passive increase
		fatigue+=fatigueRate[CONSTANT]*scale;

		boolean resting = true;
		// check for sleep (by detecting significant jump of time (a whole minute seems significant enough))
		if (dt>=1200) {
			fatigue+=fatigueRate[SLEEPING]*scale;
			if (fatigue<0) fatigue=0;
			resting=false;
		} else { // otherwise apply other conditions
			// walking / swimming
			if (game.thePlayer.moveForward!=0 || game.thePlayer.moveStrafing!=0) {
				if (game.thePlayer.isInWater()) fatigue+=fatigueRate[SWIMMING]*scale;
				else fatigue+=fatigueRate[WALKING]*scale;
				resting=false;
			}
			// jumping
			if (game.thePlayer.isJumping) {
				resting=false;
				fatigue+=fatigueRate[JUMPING]*scale;
			}
			// using a tool/weapon/fist
			if (game.thePlayer.isSwinging) {
				resting=false;
				fatigue+=fatigueRate[SWINGING]*scale;
			}
			// sneaking
			if (game.thePlayer.isSneaking()) {
				resting=false;
				fatigue+=fatigueRate[SNEAKING]*scale;
			}
			// resting
			if (resting) {
				fatigue+=fatigueRate[RESTING]*scale;
			}
			// walking on various block types increases fatigue? That may be going too far :P
			//int blockID = game.theWorld.getBlockId((int)game.thePlayer.posX, (int)game.thePlayer.posY-2, (int)game.thePlayer.posZ);
		}

		// check for fatigue damage
		if (fatigue>=fatigueMax) {
			dying=true;
			fatigueOverflow+=(fatigue-fatigueMax);
			int amt = fatigueOverflow/fatigueHeartScaleFactor;
			if (amt>=1) {
				if (hurtPlayer(game, amt)) fatigue=fatigueMax;
				fatigueOverflow=0;
			}
			// apply other fatigued penalties
			// TODO e.g. slowing player, making str vs blocks lower etc
		}
	}
	
	private static int gryllsTick = 0;
	private static void doGrylls(Minecraft game) {
		if (thirst>=thirstMax && game.thePlayer.isSneaking() && !game.thePlayer.isInWater() && game.thePlayer.moveForward==0 && game.thePlayer.moveStrafing==0) {
			if (game.thePlayer.inventory.getCurrentItem()!=null) {
				if (game.thePlayer.inventory.getCurrentItem().itemID==Block.glass.blockID 
						&& game.thePlayer.inventory.getCurrentItem().stackSize==1 && thirst>=thirstMax) {
					if (gryllsTick++>=3) {
						game.thePlayer.inventory.mainInventory[game.thePlayer.inventory.currentItem]=new ItemStack(XieMod.lemonade);
					}
				} else gryllsTick=0;
			} else gryllsTick=0;
		} else gryllsTick=0;
	}
	
	public static void feed(int amount) {
		hunger-=amount;
		stateUpdate();
	}
	
	public static void feed(int amount, ItemFood food) {
        // establish amount to relieve hunger and amount to relieve thirst, default is relieves hunger, not thirst
		int hungerRelief = amount;
		int thirstRelief = 0;
		
		if (XieFoodLists.juicyList.contains(food)) {
			// food is JUICY, so it relieves both hunger and thirst
			hungerRelief = amount;
			thirstRelief = amount/2;
		} else if (XieFoodLists.juiceList.contains(food)) {
			// food is a juice, i.e. entirely liquid
			hungerRelief = amount/2;
			thirstRelief = amount;
		} else if (XieFoodLists.waterList.contains(food)) {
			// food is just water, relieves thirst but does nothing for hunger
			hungerRelief=0;
			thirstRelief=amount;
		}
				
		// apply changes
		hunger-=hungerRelief;
		thirst-=thirstRelief;
		
		stateUpdate();
	}
	
	// hurt the player by the given amount. Returns true if the player survived
	private boolean hurtPlayer(Minecraft game, long scale) {
		if (game.thePlayer.health<=scale) {
			resetState();
			game.thePlayer.kill();
			return false;
		} else {	
			game.thePlayer.health-=scale;
			return true;
		}
	}
	
	private static void resetState() {
		hunger=0;
		hungerState=0;
		thirst=0;
		thirstState=0;
		fatigue=0;
		fatigueState=0;
		hungerTicks=0;
		thirstTicks=0;
		tickCounter=0;
		timeOfLastTick=0;
	}
	
	private void healPlayer(Minecraft game, long scale) {
		if (game.thePlayer.health<20) game.thePlayer.heal((int)scale);
	}
	
	public boolean inTheGreen() {
		return hungerState<=1 && thirstState<=1 && fatigueState<=1;
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

	private String defaultProperties() {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		
		try {
			bw.newLine();
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// use newline from calling "bw.newLine()" instead of escape character,
		// because escape characters can vary from system to system
		String newline = sw.toString();
		
		sw = new StringWriter();
		bw = new BufferedWriter(sw);
		
		try {
			bw.write(
					"# "+this.toString()+" settings file"+newline
					+newline
					+"# Enable or disable hunger, thirst and fatigue (true or false)"+newline
					+"hungerEnabled="+hungerEnabled+newline
					+"thirstEnabled="+thirstEnabled+newline
					+"fatigueEnabled="+fatigueEnabled+newline
					+newline
					+"# Number of game clock ticks (~20 ticks/second) between triggers of the hunger mod's clock ticks"+newline
					+"# (Lower provides more precision, but higher reduces computational cost)"+newline
					+"clockRate="+hungerClockTickRate+newline+newline
					+"# Time (in hunger mod clock ticks) between increasing hunger/thirst"+newline
					+"# It will take 20 times this amount to start to starve/dehydrate, and that time again to die"+newline
					+"hungerTime="+hungerRate+newline
					+"thirstTime="+thirstRate+newline
					+newline
					+"# 'In the green' hunger benefits, applied when you are not hungry"+newline
					+"# Passive Regeneration - passive regen like in Peaceful mode"+newline
					+"passiveRegen="+passiveRegen+newline
					+newline
					+"# Use bars or icons to display hunger state (useBars=false means icons will be used)"
					+newline
					+"useBars="+useBars+newline
					+newline
					+"# Thirst: natural water is drinkable (by crouching in it) (true/false)"+newline
					+"waterDrinkable="+waterDrinkable+newline+newline
					+"# Fatigue rates. Amount by which to increase fatigue each hunger clock tick"+newline
					+"# Constant rate. Applied each tick regardless of other factors."+newline
					+"Constant="+fatigueRate[CONSTANT]+newline
					+"# Walking rate. Includes activley walking, including strafing."+newline
					+"Walking="+fatigueRate[WALKING]+newline
					+"# Jumping rate. Applied while in the midst of a jump. Not applied to natural falling."+newline
					+"Jumping="+fatigueRate[JUMPING]+newline
					+"# Swimming rate. Replaces 'walking' rate while moving in water."+newline
					+"Swimming="+fatigueRate[SWIMMING]+newline
					+"# Swinging rate. Applied when swinging a weapon/tool/fist etc, regardless if you're hitting anything."+newline
					+"Swinging="+fatigueRate[SWINGING]+newline
					+"# Sneaking rate. Applied while sneaking, regardless of if you're moving or not. Stacks with 'walking' rate."+newline
					+"Sneaking="+fatigueRate[SNEAKING]+newline
					+"# Resting rate. Applied when none of these conditional modifiers are."+newline
					+"Resting="+fatigueRate[RESTING]+newline
					+"# Sleeping rate. Applied when resting in a bed."+newline
					+"Sleeping="+fatigueRate[SLEEPING]+newline
			);
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.toString();
	}
	
	public static void loadProperties (String settingsFile, String defaults) {
		File dir = Minecraft.getMinecraftDir();
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
		
		hungerEnabled=setBool(props, "hungerEnabled", hungerEnabled);
		thirstEnabled=setBool(props, "thirstEnabled", thirstEnabled);
		fatigueEnabled=setBool(props, "fatigueEnabled", fatigueEnabled);
		
		hungerClockTickRate=setInt(props, "ClockRate", hungerClockTickRate);
		
		hungerRate=setInt(props, "hungerTime",hungerRate);
		passiveRegen=setBool(props, "passiveRegen", passiveRegen);
		thirstRate=setInt(props, "thirstTime",thirstRate);
		
		useBars=setBool(props, "useBars", useBars);
		
		waterDrinkable=setBool(props, "waterDrinkable", waterDrinkable);
		
		fatigueRate[CONSTANT]=setInt(props, "Constant", fatigueRate[CONSTANT]);
		fatigueRate[WALKING]=setInt(props, "Walking", fatigueRate[WALKING]);
		fatigueRate[JUMPING]=setInt(props, "Jumping", fatigueRate[JUMPING]);
		fatigueRate[SWIMMING]=setInt(props, "Swimming", fatigueRate[SWIMMING]);
		fatigueRate[SWINGING]=setInt(props, "Swinging", fatigueRate[SWINGING]);
		fatigueRate[SNEAKING]=setInt(props, "Sneaking", fatigueRate[SNEAKING]);
		fatigueRate[RESTING]=setInt(props, "Resting", fatigueRate[RESTING]);
		fatigueRate[SLEEPING]=setInt(props, "Sleeping", fatigueRate[SLEEPING]);
	}
	
	private static int setInt(Properties props, String prop, int def) {
		return Integer.parseInt(props.getProperty(prop,""+def));
	}
	
	private static boolean setBool(Properties props, String prop, boolean def) {
		return Boolean.parseBoolean(props.getProperty(prop,""+def));
	}
	
	// Saves hunger state for player in a text file
	public static void saveStateToFile(Minecraft game) {
		File dir = new File(Minecraft.getMinecraftDir()+hungerSaveFilePath);
		if (!dir.exists()) dir.mkdirs();
		String filename = game.session.username+".hunger";
		File f = new File(dir+"/"+filename);
		
		Properties props = new Properties();
		
		String world;
		if (game.isMultiplayerWorld()) world = game.gameSettings.lastServer;
		else world = game.theWorld.worldInfo.getWorldName();
		
		String keyHunger = world+"@hunger";
		String keyThirst = world+"@thirst";
		String keyFatigue = world+"@fatigue";
		String keyClockTick = world+"@clockTick";
		String lastTickTime = world+"@lastTickTime";
		
		try {
			if (!f.exists()) {
				f.createNewFile();
			} else {
				FileReader fr = new FileReader(f);
				props.load(fr);
			}
			
			props.put(keyHunger, ""+hunger);
			props.put(keyThirst, ""+thirst);
			props.put(keyFatigue, ""+fatigue);
			props.put(keyClockTick, ""+tickCounter);
			props.put(lastTickTime, ""+timeOfLastTick);
			
			FileWriter fw = new FileWriter(f);
			props.store(fw, null);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Loads hunger state info for server
	public static void loadStateFromFile(Minecraft mc) {
		// first clear state
		resetState();
		
		File dir = new File(mc.getMinecraftDir()+hungerSaveFilePath);
		String filename = mc.session.username+".hunger";
		File f = new File(dir+"/"+filename);
		
		Properties props = new Properties();
		
		String world;
		if (mc.isMultiplayerWorld()) world = mc.gameSettings.lastServer;
		else world = mc.theWorld.worldInfo.getWorldName();
		
		String keyHunger = world+"@hunger";
		String keyThirst = world+"@thirst";
		String keyFatigue = world+"@fatigue";
		String keyClockTick = world+"@clockTick";
		String lastTickTime = world+"@lastTickTime";
		
		try {
			if (!f.exists()) {
				System.out.println("Couldn't find hunger file "+f.getCanonicalPath());
			} else {
				FileReader fr = new FileReader(f);
				props.load(fr);
				
				if (props.containsKey(keyHunger)) {
					hunger = Integer.parseInt(props.getProperty(keyHunger));
				} else {
					System.out.println("Hunger information for world/server "+world+" not found.");
				}
				
				if (props.containsKey(keyThirst)) {
					thirst = Integer.parseInt(props.getProperty(keyThirst));
				} else {
					System.out.println("Thirst information for world/server "+world+" not found.");
				}
				
				if (props.containsKey(keyFatigue)) {
					fatigue = Integer.parseInt(props.getProperty(keyFatigue));
				} else {
					System.out.println("Fatigue information for world/server "+world+" not found.");
				}
				
				if (props.containsKey(keyClockTick)) tickCounter = Integer.parseInt(props.getProperty(keyClockTick));
				if (props.containsKey(lastTickTime)) timeOfLastTick = Long.parseLong(props.getProperty(lastTickTime));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// fatigue rates
	public static final int CONSTANT=0;
	public static final int WALKING=1;
	public static final int JUMPING=2;
	public static final int SWIMMING=3;
	public static final int SWINGING=4;
	public static final int SNEAKING=5;
	public static final int RESTING=6;
	public static final int SLEEPING=7;
}

