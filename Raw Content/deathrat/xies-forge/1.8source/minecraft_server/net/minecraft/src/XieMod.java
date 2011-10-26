package net.minecraft.src;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.minecraft.server.MinecraftServer;

public abstract class XieMod {
	// Global Settings
	public static boolean DEBUG = false;
	public static boolean CHEATS = false;
	
	static final String settingsPath = "config/Xie/";
	
	public static boolean xieModInit = false;
	public static Properties lang;
	
	public static boolean xieCooking = false;
	public static boolean xieFarming = false;
	public static boolean xieFoodStacking = false;
	
	// Stax settings
	public static int foodStackSize = 16;

	// Farming Settings
	public static class Farming {
		
		public static class Settings {
			public static int appleTreeChance = 20;
			public static int orangeTreeChance = 30;
			public static int lemonTreeChance = 30;
			public static int avocadoTreeChance = 40;
			public static int watermelonChance = 20;
			public static int watermelonGrowthChance = 15;
			public static int pumpkinGrowthChance = 15;
			public static int flowerGrowthChance = 15;
			public static int tomatoGrowthChance = 15;
			public static int cocoaGrowthChance = 15;
			public static int cornGrowthChance = 80;
			public static int lettuceGrowthChance = 15;
			public static int cottonGrowthChance = 15;
			public static int appleGrowthChance = 7;
			public static int appleGrowthTime = 15;
			public static int appleSaplingDropChance = 14;
			public static int appleDropChance = 14;
			public static int stickDropChance = 0;
			public static int seedTillChance = 40;
			public static int pumpkinSeedYield = 0;
			public static int orangeSaplingDropChance = 14;
			public static int orangeDropChance = 14;
			public static int orangeGrowthChance = 7;
			public static int orangeGrowthTime = 15;
			public static int lemonSaplingDropChance = 14;
			public static int lemonDropChance = 14;
			public static int lemonGrowthChance = 7;
			public static int lemonGrowthTime = 15;
			public static int avocadoSaplingDropChance = 14;
			public static int avocadoDropChance = 14;
			public static int avocadoGrowthChance = 7;
			public static int avocadoGrowthTime = 15;
			public static int wheatGrowthChance = 100;
			public static int hybridSeedChance = 10;
		}
		
		public static class FoodYield {
			public static int roastPumpkin = 7;
			public static int watermelonPiece = 5;
			public static int pumpkinPiece = 5;
			public static int popcorn = 4;
			public static int cornCob = 4;
			public static int cornCobCooked = 6;
			public static int tomato = 4;
			public static int lettuce = 4;
			public static int orange = 4;
			public static int lemon = 1;
			public static int avocado = 5;
			public static int orangeJuice = 7;
			public static int lemonade = 7;
			public static int guacamole = 8;
		}
		
		public static class Enable {
			public static boolean pumpkins = true;
			public static boolean melons = true;
			public static boolean appleTrees = true;
			public static boolean flowerSeeds = true;
			public static boolean tomatoes = true;
			public static boolean corn = true;
			public static boolean cotton = true;
			public static boolean lettuce = true;
			public static boolean orange = true;
			public static boolean lemon = true;
			public static boolean avocado = true;
			public static boolean orangeJuice = true;
			public static boolean lemonade = true;
			public static boolean orangeTrees = true;
			public static boolean lemonTrees = true;
			public static boolean avocadoTrees = true;
		}

		public static class BlockID {
			public static int xieLeaves = 200;
			public static int xieLog = 201;
			public static int appleSapling = 202;
			public static int pumpkinVine = 203;
			public static int watermelonVine = 204;
			public static int watermelon = 205;
			public static int yellowSeedling = 206;
			public static int redSeedling = 207;
			
			public static int tomatoPlant = 208;
			public static int cornPlant = 209;
			public static int cottonPlant = 210;
			public static int lettucePlant = 211;
			public static int cocoaPlant = 212;
			
			public static int wheatOverride = 213;
			public static int hybridWheat = 214;

			public static int orangeSapling = 215;
			public static int lemonSapling = 216;
			public static int avocadoSapling = 217;
		}
		
		public static class ItemID {
			public static int watermelonPiece=300;
			public static int melonSeeds=301;
			public static int pumpkinSeeds=302;
			public static int pumpkinPiece=303;
			public static int roastPumpkin=304;
			public static int yellowSeeds=305;
			public static int redSeeds=306;
			public static int cocoaSeeds=307;
			
			public static int tomatoSeeds = 400;
			public static int cornSeeds = 401;
			public static int cottonSeeds = 402;
			public static int lettuceSeeds = 403;
			
			public static int cornKernels = 404;
			public static int popcorn = 405;
			public static int cornCob = 406;
			public static int cornCobCooked = 407;
			public static int tomato = 408;
			public static int cotton = 409;
			public static int lettuce = 410;
			public static int orange = 411;
			public static int lemon = 412;
			public static int avocado = 413;
			public static int orangeJuice = 414;
			public static int lemonade = 415;
			public static int hybridSeeds = 416;
			
			public static int guacamole = 417;
			public static int cocoaFruit = 418;
		}
	}
	
	// Cooking Settings
	public static class Cooking {
		
		public static class Settings {
			public static int saltDepositSpawnChance = 12;
			public static int saltDepositInGameSpawnChance = 16;
			public static int saltTillChance = 0;
			public static int surpriseChance = 8;
			public static int drinkHealAmount = 4;
		}
		
		public static class Enable {
			public static boolean campfire = true;
			public static boolean bonfire = true;
			public static boolean stove = true;		
			public static boolean cheese = true;
			public static boolean jerky = true;
			public static boolean sammich = true;
			public static boolean tastySammich = true;
			public static boolean fruitSalad = true;
			public static boolean salad = true;
			public static boolean friedEgg = true;
			public static boolean salt = true;
			public static boolean pancake = true;
			public static boolean oil = true;
			public static boolean mayo = true;
			public static boolean saltRespawn = false;
			public static boolean jaffaCake = true;
		}
		
		public static class FoodYield {
			public static int jerky = 3;
			public static int friedEgg = 5;
			public static int cheese = 8;
			public static int failFry = 6;
			public static int fry = 8;
			public static int stirfry = 12;
			public static int failSoup = 6;
			public static int soup = 8;
			public static int stew = 12;
			public static int pancake = 6;
			public static int sammich = 8;
			public static int tastySammich = 10;
			public static int fruitSalad = 8;
			public static int salad = 6;
			public static int mayo = 6;
			public static int jaffaCake = 8;
		}
		
		public static class BlockID {
			public static int campfire = 218;
			public static int bonfire = 219;
			public static int stoveIdle = 220;
			public static int stoveActive = 221;
			public static int saltDeposit = 222;
		}
		
		public static class ItemID {
			public static int debugFood = 310;
			public static int oil = 311;
			public static int mayo = 312;
			public static int pancakeBatter = 313; 
			public static int salt = 314;
			public static int friedEgg = 315;
			public static int cheese = 316;
			public static int pancake = 317;
			public static int jerky = 318;
			public static int fruitSalad = 319;
			public static int failSoup = 320;
			public static int soup = 321;
			public static int stew = 322;
			public static int failFry = 323;
			public static int fry = 324;
			public static int stirfry = 325;
			public static int sammich = 326;
			public static int tastySammich = 327;
			public static int salad = 328;
			public static int jaffaCake = 329;
		}
	}
	
	public XieMod () {
	}

	static File langFile;
	public static void init() {
		if (xieModInit) return;
		
		// load language file, if it exists
		lang = loadProperties("lang.ini");
		
//		File dir = ModLoader.getMinecraftInstance().getMinecraftDir();
		String dir = "";
		langFile = new File(dir+settingsPath+"lang.ini");
		
		xieModInit=true;
	}
	
		public static String getName(String n) {
		if (!xieModInit) init();
		return lang.getProperty(n,n);
	}
	
	// convenience wrapper for getting item sprite index from mod loader
	/**
	 * @deprecated
	 */
	public static int getIcon() {
		return ModLoader.getUniqueSpriteIndex("/gui/items.png");
	}
	
	// convenience wrapper for getting terrain sprite index from mod loader
	/**
	 * @deprecated
	 */
	
	public static int getTex() {
		return ModLoader.getUniqueSpriteIndex("/terrain.png");
	}
	
	public static Properties loadProperties (String settingsFile) {
		return loadProperties(settingsFile, "");
	}
	
	public static Properties loadProperties (String settingsFile, String defaults) {
//		File dir = ModLoader.getMinecraftInstance().getMinecraftDir();
		String dir = "";
		File file = new File(dir+settingsPath+settingsFile);
		Properties props = new Properties();
		try {
			props.load(new FileReader(file));
		} catch (FileNotFoundException e) {
			try {
				// check for path existence
				if (defaults!=null && defaults!="") { 
					File path = new File(dir+settingsPath);
					if (!path.exists()) path.mkdirs();
				
					file.createNewFile();
					FileWriter fw = new FileWriter(file);
					fw.write(defaults);
					fw.close();
				}
			} catch (IOException e1) { e1.printStackTrace(); }	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (props.containsKey("DEBUG")) if (props.getProperty("DEBUG").compareTo("true")==0) DEBUG=true;
		if (props.containsKey("CHEATS")) if (props.getProperty("CHEATS").compareTo("true")==0) CHEATS=true;
		
		return props;
	}

	public static Item newItem(int id, int tex, String name) {
//		Item it = new Item(id).setIconIndex(tex).setItemName(name);
		Item it = new XieItem(id).setIconIndex(tex).setItemName(name);
//		ModLoader.AddName(it,getName(name));
		return it;
	}
	
	public static void setBlockHardnessAndSound(Block block, float hardness, StepSound sound, String name) {
		block.setHardness(hardness);
		block.setStepSound(sound);
		block.setBlockName(getName(name));
	}
	
	public static void setBlockLightValue(Block block, float f) {
		block.setLightValue(f);
	}
	
	public static void addRecipe (ItemStack is, Object o[]) {
		ModLoader.AddRecipe(is,o);
	}
	
	// Deprecated
	public static void addRecipe (CraftingManager cm, ItemStack is, Object o[]) {
		addRecipe(is, o);
	}
	
	public static void addShapelessRecipe (ItemStack is, Object o[]) {
		ModLoader.AddShapelessRecipe(is, o);
	}
	
	// Deprecated
	public static void addShapelessRecipe (CraftingManager cm, ItemStack is, Object o[]) {
		addShapelessRecipe(is, o);
	}
	
	public static void readBooleanProperties(Properties props, Field f[]) throws IllegalArgumentException, IllegalAccessException {
		for (int i=0; i<f.length; i++) {
			String propKey = f[i].getDeclaringClass().getSimpleName()+"."+f[i].getName();
			if (props.containsKey(propKey)) {
				String propVal = props.getProperty(propKey);
				if (propVal.compareTo("false")==0) f[i].set(null, false);
				else if (propVal.compareTo("true")==0) f[i].set(null, true);
			}
		}
	}
	
	public static void readIntegerProperties(Properties props, Field f[]) throws IllegalArgumentException, IllegalAccessException {
		for (int i=0; i<f.length; i++) {
			String propKey = f[i].getDeclaringClass().getSimpleName()+"."+f[i].getName();
			if (props.containsKey(propKey)) {
				String propVal = props.getProperty(propKey);
				int n = Integer.MIN_VALUE;
				try {
					n = Integer.parseInt(propVal);
				} catch (NumberFormatException e) {
					continue;
				}
				if (n!=Integer.MIN_VALUE) f[i].set(null, n);
			}
		}
	}
	
	public static void writeProperties(BufferedWriter bw, String header, Field f[]) throws IOException, IllegalArgumentException, IllegalAccessException {
		bw.newLine();
		bw.write(header);
		bw.newLine();
		for (int i=0; i<f.length; i++) {
			bw.write(f[i].getDeclaringClass().getSimpleName()+"."+f[i].getName()+"="+f[i].get(null));
			bw.newLine();
		}
	}
	
	public static Item getFromMod(String mod, String classname) {
		try {
			Class c = Class.forName(mod);
			if (c!=null) {
				Field f = c.getField(classname);
				if (f!=null) {
					Item i = (Item) f.get(null); 
					return i;
				}
			} 
		} catch (Exception e) {
			System.err.println("XieMod: couldn't get item "+classname+" from mod "+mod);
			e.printStackTrace();
		}
		
		return null;
	}

	public static void addModFoodsToLists(String mod, String foodList, List<Item>[] lists) {
		String [] foods = foodList.split(",");
		for (int i=0; i<foods.length; i++) {
			Item food = getFromMod(mod, foods[i]);
			if (food!=null) {
				for (int j=0; j<lists.length; j++) {
					lists[j].add(food);
				}
			}
		}
	}
	
	public static boolean isFood(Item i) {
		return (i instanceof ItemFood || customFoodList.contains(i));
	}
	
	// returns true if the item in question has cookie stacking properties (can be stacked and eaten from the hotbar)
	public static boolean isCookieLike(Item i) {
//		return i instanceof ItemCookie;
//		return i instanceof ItemFood;
		return true;
	}
	
	public static boolean isFoodButNotCookie(Item i) {
		return isFood(i) && !isCookieLike(i);
	}
	
	public static XieItemFood addNewFood (String name, int id, int yield, int index) {
		XieItemFood it = (new XieItemFood(id, yield, false));
//		it.setIconIndex(ModLoader.addOverride("/gui/items.png","/Xie/img/items/food/"+name.toLowerCase()+".png"));
		it.setIconIndex(index);
		it.setItemName(name);
//		ModLoader.AddName(it, getName(name));
		
		return it;
	}
	
	public static XieItemFood addNewFood (String name, int id, int yield, int index, float f) {
		XieItemFood it = (new XieItemFood(id, yield, false, f));
//		it.setIconIndex(ModLoader.addOverride("/gui/items.png","/Xie/img/items/food/"+name.toLowerCase()+".png"));
		it.setIconIndex(index);
		it.setItemName(name);
//		ModLoader.AddName(it, getName(name));
		
		return it;
	}
	
	public static XieItemContainedFood addNewContainedFood (String name, int id, int yield, int container, int index) {
		XieItemContainedFood it = (new XieItemContainedFood(id, yield, container));
//		it.setIconIndex(ModLoader.addOverride("/gui/items.png","/Xie/img/items/food/"+name.toLowerCase()+".png"));
		it.setIconIndex(index);
		
		it.setItemName(name);
//		ModLoader.AddName(it, getName(name));
		
		return it;
	}
	
	public static XieItemContainedFood addNewContainedFood (String name, int id, int yield, int container, int index, float f) {
		XieItemContainedFood it = (new XieItemContainedFood(id, yield, container, f));
//		it.setIconIndex(ModLoader.addOverride("/gui/items.png","/Xie/img/items/food/"+name.toLowerCase()+".png"));
		it.setIconIndex(index);
		
		it.setItemName(name);
//		ModLoader.AddName(it, getName(name));
		
		return it;
	}
	
//	public static XieItemContainedFood addNewContainedFood (String name, int id, int yield, int container) {
//		XieItemContainedFood it = (new XieItemContainedFood(id, yield, container));
//		it.setIconIndex(ModLoader.addOverride("/gui/items.png","/Xie/img/items/food/"+name.toLowerCase()+".png"));
//		
//		it.setItemName(name);
//		ModLoader.AddName(it, getName(name));
//		
//		return it;
//	}

	
	// Blocks (Cooking)
	public static XieBlockCampfire campfire;
	public static XieBlockCampfire bonfire;
	public static XieBlockStove stoveIdle;
	public static XieBlockStove stoveActive;
	
	// Items (Cooking)
	// Ingredients
	public static Item oil;
	public static Item mayo;
	public static Item pancakeBatter;
	public static Item salt;
	// Specific foods
	public static ItemFood friedEgg;
	public static ItemFood cheese;
	public static ItemFood pancake;
	public static ItemFood jerky;
	// Composite Foods
//	public static ItemFood jaffaCake;
	public static ItemSoup debugFood;
	public static ItemFood fruitSalad;
	public static ItemFood salad;
	public static ItemSoup failSoup;
	public static ItemSoup soup;
	public static ItemSoup stew;
	public static ItemSoup failFry;
	public static ItemSoup fry;
	public static ItemSoup stirfry;
	public static ItemFood sammich;
	public static ItemFood tastySammich;
	public static ItemFood jayCake;
	// Food Lists
	public static List<Item> soupList = new LinkedList<Item>();
	public static List<Item> fryList = new LinkedList<Item>();
	public static List<Item> sammichList = new LinkedList<Item>();
	public static List<Item> sauceList = new LinkedList<Item>();
	public static List<Item> fruitList = new LinkedList<Item>();
	public static List<Item> flavorList = new LinkedList<Item>();
	public static List<Item> saladList = new LinkedList<Item>();
	// juicy foods contribute to both thirst and hunger, juice foods contribute only to thirst
	public static List<Item> juicyList = new LinkedList<Item>();
	public static List<Item> juiceList = new LinkedList<Item>();
	// Custom food list is for food items from other mods that are food but don't inherit the food item class
	public static List<Item> customFoodList = new LinkedList<Item>();
	// Seeds list (for drops)
	public static List<Item> seedsDropList = new LinkedList<Item>();
	
	// Trees (farming)
	public static XieTreeManager treeManager;
	public final static int APPLE = 0;
	public final static int ORANGE = 1;
	public final static int LEMON = 2;
	public final static int AVOCADO = 3;
	
	// Blocks (Farming)
	public static XieBlockLeaves appleLeaves;  // @deprecated 
	public static int appleLeavesFancy; // @deprecated 
	public static int appleLeavesOpaque; // @deprecated
	public static XieBlockLog appleLog;  // @deprecated 
	public static XieBlockSapling appleSapling;	 // @deprecated 
	public static XieBlockLog log;
	public static XieBlockLeaves leaves;
	public static int leavesTex;
	public static XieBlockSeedling pumpkinVine;
	public static int pumpkinVineTex[];
	public static XieBlockSeedling watermelonVine;
	public static int watermelonVineTex[];
	public static XieBlockWatermelon watermelon;
	public static int watermelonTop;
	public static XieBlockSeedling yellowSeedling;
	public static XieBlockSeedling redSeedling;
	public static int yellowSeedlingTex[];
	public static int redSeedlingTex[];
	public static XieBlockDeposit saltDeposit;
	// new
	public static XieBlockBush tomatoPlant;
	public static int tomatoTex[];
	public static XieBlockCrop cornPlant;
	public static int cornTex[];
	public static XieBlockBush cottonPlant;
	public static int cottonTex[];
	public static XieBlockCrop lettucePlant;
	public static int lettuceTex[];
	public static XieBlockBush cocoaPlant;
	public static int cocoaTex[];
	public static XieBlockCrop wheat;
	public static XieBlockCrop hybridWheat;
	
	// Items  (Farming)
	public static ItemSeeds pumpkinSeeds;
	public static Item pumpkinPiece;
	public static ItemFood roastPumpkin;
	public static ItemSeeds melonSeeds;
	public static ItemFood watermelonPiece;
	public static ItemSeeds redSeeds;
	public static ItemSeeds yellowSeeds;
	public static ItemSeeds cocoaSeeds;
	// new
	public static ItemSeeds tomatoSeeds;
	public static ItemSeeds cornSeeds;
	public static ItemSeeds cottonSeeds;
	public static ItemSeeds lettuceSeeds;
	public static ItemSeeds cornKernels;
	public static ItemFood popcorn;
	public static ItemFood cornCob;
	public static ItemFood cornCobCooked;
	public static ItemFood tomato;
	public static Item cocoaFruit;
	public static Item cotton;
	public static ItemFood lettuce;
	public static ItemFood orange;
	public static Item lemon;
	public static ItemFood avocado;
	public static ItemFood guacamole;
	public static XieItemContainedFood orangeJuice;
	public static XieItemContainedFood lemonade;
	public static ItemSeeds hybridSeeds;
	
	// cheat start inventory
	public static ItemStack itemz [] = new ItemStack[] {
			new ItemStack(Item.pickaxeDiamond),
			new ItemStack(Item.axeDiamond),
			new ItemStack(Item.hoeDiamond),
			new ItemStack(Item.shovelDiamond),
			new ItemStack(Item.dyePowder, 64, 15),
			new ItemStack(Item.seeds, 64),
			new ItemStack(Item.diamond, 64),
			new ItemStack(Block.sapling, 64),
			new ItemStack(Item.redstone, 64)
	};
}
