package net.minecraft.src;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Random;

import net.minecraft.src.forge.MinecraftForgeClient;

public class mod_XieFarming extends BaseMod {

	
	final static String modName = "Xie Farming";
	final static String version = "1.7b";
	final static String settingsFile = "farming.ini";
	
	Properties props;
	
	public mod_XieFarming() {
		props = XieMod.loadProperties(settingsFile, defaultProperties());	
		
		try {
			XieMod.readBooleanProperties(props, XieMod.Farming.Enable.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Farming.Settings.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Farming.FoodYield.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Farming.BlockID.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Farming.ItemID.class.getDeclaredFields());
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		loadBlocks();
		loadItems();
		loadTrees();
		
		MinecraftForgeClient.preloadTexture("/Xie/img/terrain/xie_terrain.png");
		MinecraftForgeClient.preloadTexture("/Xie/img/terrain/saltdeposit.png");
		MinecraftForgeClient.preloadTexture("/Xie/img/items/xie_items.png");

		
		XieMod.xieFarming=true;
		
		AddRecipes();
	}
	
	private String defaultProperties() {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		
		try {
			bw.write("# "+this.toString()+" settings file");bw.newLine();
						
			XieMod.writeProperties(bw, "# Enable/disable (true/false)", XieMod.Farming.Enable.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Spawn rates, drop chances (higher value = lower chance)", XieMod.Farming.Settings.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Food yield, half hearts", XieMod.Farming.FoodYield.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Block IDs", XieMod.Farming.BlockID.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Item IDs", XieMod.Farming.ItemID.class.getDeclaredFields());
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sw.toString();
	}
	
    public void ModsLoaded()
    {
    }
	
    private void loadTrees () {
    	XieMod.log = (XieBlockLog) new XieBlockLog(XieMod.Farming.BlockID.xieLog).setBlockName("xieLog");
    	ModLoader.RegisterBlock(XieMod.log);
    	ModLoader.AddName(XieMod.log, "Log");
    	XieItemAxe.effective.add(XieMod.log);
    	
    	XieMod.leaves = (XieBlockLeaves) new XieBlockLeaves(XieMod.Farming.BlockID.xieLeaves).setBlockName("xieLeaves");
    	ModLoader.RegisterBlock(XieMod.leaves);
    	ModLoader.AddName(XieMod.leaves, "Leaves");
    	
    	XieMod.treeManager = new XieTreeManager();
    	
    	XieMod.treeManager.addTree(XieMod.APPLE, "Apple", Item.appleRed, XieMod.Farming.Settings.appleDropChance, XieMod.Farming.BlockID.appleSapling, 
    			XieMod.Farming.Settings.appleSaplingDropChance, XieMod.Farming.Settings.appleGrowthChance, XieMod.Farming.Settings.appleGrowthTime);
    	XieMod.treeManager.addTree(XieMod.ORANGE, "Orange", XieMod.orange, XieMod.Farming.Settings.orangeDropChance, XieMod.Farming.BlockID.orangeSapling, 
    			XieMod.Farming.Settings.orangeSaplingDropChance, XieMod.Farming.Settings.orangeGrowthChance, XieMod.Farming.Settings.orangeGrowthTime);
    	XieMod.treeManager.addTree(XieMod.LEMON, "Lemon", XieMod.lemon, XieMod.Farming.Settings.lemonDropChance, XieMod.Farming.BlockID.lemonSapling, 
    			XieMod.Farming.Settings.lemonSaplingDropChance, XieMod.Farming.Settings.lemonGrowthChance, XieMod.Farming.Settings.lemonGrowthTime);
    	XieMod.treeManager.addTree(XieMod.AVOCADO, "Avocado", XieMod.avocado, XieMod.Farming.Settings.avocadoDropChance, XieMod.Farming.BlockID.avocadoSapling, 
    			XieMod.Farming.Settings.avocadoSaplingDropChance, XieMod.Farming.Settings.avocadoGrowthChance, XieMod.Farming.Settings.avocadoGrowthTime);
    	
    	//XieMod.leavesTex=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/leaves.png");
    	//XieMod.leaves.blockIndexInTexture=XieMod.leavesTex; // override for digging particle effect
//    	XieMod.leaves.blockIndexInTexture=4; // override for digging particle effect
    	
    	// make tree blocks flammable
    	//Block.fire.setBurnRate(XieMod.log.blockID, 5, 5);
    	//Block.fire.setBurnRate(XieMod.leaves.blockID, 30, 60);
    	// Since setBurnRate is private, we need to do this by accessing the arrays instead (thanks to FlowerChild for the pro tip!
    	setFlammability(XieMod.log.blockID,5,5);
    	setFlammability(XieMod.leaves.blockID,30,60);
    	
    }
    
    // thanks to FlowerChild of the BetterThanWolves mod for this pro tip ;)
    private void setFlammability(int blockID, int i, int j) {
    	try {
			int _i[]=(int[])(ModLoader.getPrivateValue(BlockFire.class, Block.fire, 0));
			int _j[]=(int[])(ModLoader.getPrivateValue(BlockFire.class, Block.fire, 1));
		
	    	_i[blockID]=i;
	    	_j[blockID]=j;
    	} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // TODO This is a lot messier than it needs to be
    public void loadBlocks () {
//		XieMod.watermelon = (XieBlockWatermelon) (new XieBlockWatermelon(XieMod.Farming.BlockID.watermelon, ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/watermelonside.png"), false)).setBlockName("watermelon");
		XieMod.watermelon = (XieBlockWatermelon) (new XieBlockWatermelon(XieMod.Farming.BlockID.watermelon, 9, false)).setBlockName("watermelon");
		ModLoader.RegisterBlock(XieMod.watermelon);
//		XieMod.watermelonTop = ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/watermelontop.png");
		XieMod.watermelonTop = 10;
		ModLoader.AddName(XieMod.watermelon, XieMod.getName("Watermelon"));
		
		int pumpkinVineIndex = 11;
		XieMod.pumpkinVineTex = new int[4];
//		for (int i=0; i<4; i++) XieMod.pumpkinVineTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/pumpkinvine"+i+".png");
		for (int i=0; i<4; i++) XieMod.pumpkinVineTex[i]=i + pumpkinVineIndex;
		XieMod.pumpkinVine = (XieBlockSeedling) (new XieBlockSeedling(XieMod.Farming.BlockID.pumpkinVine, 
				XieMod.pumpkinVineTex, Block.pumpkin.blockID, XieMod.Farming.Settings.pumpkinGrowthChance)).setBlockName("pumpkinVine");
		XieMod.pumpkinVine.setBlockBounds(0, 0, 0, 1, 0.5F, 1);
		ModLoader.RegisterBlock(XieMod.pumpkinVine);
		ModLoader.AddName(XieMod.pumpkinVine, "Pumpkin Vine");
		
		int melonVineIndex = 15;
		XieMod.watermelonVineTex = new int[4];
//		for (int i=0; i<4; i++) XieMod.watermelonVineTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/watermelonvine"+i+".png");		
		for (int i=0; i<4; i++) XieMod.watermelonVineTex[i]=i + melonVineIndex;
		XieMod.watermelonVine = (XieBlockSeedling) (new XieBlockSeedling(XieMod.Farming.BlockID.watermelonVine, 
				XieMod.watermelonVineTex, XieMod.watermelon.blockID, XieMod.Farming.Settings.watermelonGrowthChance)).setBlockName("melonVine");
		XieMod.watermelonVine.setBlockBounds(0, 0, 0, 1, 0.5F, 1);
		ModLoader.RegisterBlock(XieMod.watermelonVine);
		ModLoader.AddName(XieMod.watermelonVine, "Watermelon Vine");
		
		int yellowSeedIndex = 19;
		XieMod.yellowSeedlingTex = new int[3];
//		for (int i=0; i<3; i++) XieMod.yellowSeedlingTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/yellowseedling"+i+".png");
		for (int i=0; i<3; i++) XieMod.yellowSeedlingTex[i]=i + yellowSeedIndex;
		XieMod.yellowSeedling = (XieBlockSeedling) (new XieBlockSeedling(XieMod.Farming.BlockID.yellowSeedling, 
				XieMod.yellowSeedlingTex, Block.plantYellow.blockID, XieMod.Farming.Settings.flowerGrowthChance)).setBlockName("yellowSeedling");
		ModLoader.RegisterBlock(XieMod.yellowSeedling);
		ModLoader.AddName(XieMod.yellowSeedling, "Yellow Flower Seedling");
		
		int redSeedIndex = 22;
		XieMod.redSeedlingTex = new int[3];
//		for (int i=0; i<3; i++) XieMod.redSeedlingTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/redseedling"+i+".png");
		for (int i=0; i<3; i++) XieMod.redSeedlingTex[i]=i + redSeedIndex;
		XieMod.redSeedling = (XieBlockSeedling) (new XieBlockSeedling(XieMod.Farming.BlockID.redSeedling, 
				XieMod.redSeedlingTex, Block.plantRed.blockID, XieMod.Farming.Settings.flowerGrowthChance)).setBlockName("yellowSeedling");
		ModLoader.RegisterBlock(XieMod.redSeedling);
		ModLoader.AddName(XieMod.redSeedling, "Red Flower Seedling");
		
		int tomatoIndex = 24;
		XieMod.tomatoTex = new int[5];
//		for (int i=0; i<5; i++) XieMod.tomatoTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/tomato"+i+".png");
		for (int i=0; i<5; i++) XieMod.tomatoTex[i]=i + tomatoIndex;
		XieMod.tomatoPlant = (XieBlockBush) (new XieBlockBush(XieMod.Farming.BlockID.tomatoPlant, 
				XieMod.tomatoTex, XieMod.Farming.Settings.tomatoGrowthChance)).setBlockName("tomatoPlant");
		XieMod.tomatoPlant.setAsBillboard(true);
		ModLoader.RegisterBlock(XieMod.tomatoPlant);
		ModLoader.AddName(XieMod.tomatoPlant, "Tomato Plant");

		int cornIndex = 49;
		XieMod.cornTex = new int[5];
//		for (int i=0; i<5; i++) XieMod.cornTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/corn"+i+".png");
		for (int i=0; i<5; i++) XieMod.cornTex[i]=i + cornIndex;
		XieMod.cornPlant = (XieBlockCrop) (new XieBlockCrop(XieMod.Farming.BlockID.cornPlant, 
				XieMod.cornTex, XieMod.Farming.Settings.cornGrowthChance, 3)).setBlockName("cornPlant");
		ModLoader.RegisterBlock(XieMod.cornPlant);
		ModLoader.AddName(XieMod.cornPlant, "Corn Crop");

		int cottonIndex = 30;
		XieMod.cottonTex = new int[5];
//		for (int i=0; i<5; i++) XieMod.cottonTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/cotton"+i+".png");
		for (int i=0; i<5; i++) XieMod.cottonTex[i]=i + cottonIndex;
		XieMod.cottonPlant = (XieBlockBush) (new XieBlockBush(XieMod.Farming.BlockID.cottonPlant, 
				XieMod.cottonTex, XieMod.Farming.Settings.cottonGrowthChance)).setBlockName("cottonPlant");
		XieMod.cottonPlant.setAsBillboard(true);
		ModLoader.RegisterBlock(XieMod.cottonPlant);
		ModLoader.AddName(XieMod.cottonPlant, "Cotton Plant");

		int lettuceIndex = 35;
		XieMod.lettuceTex = new int[3];
//		for (int i=0; i<3; i++) XieMod.lettuceTex[i]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/lettuce"+i+".png");
		for (int i=0; i<3; i++) XieMod.lettuceTex[i]=i + lettuceIndex;
		XieMod.lettucePlant = (XieBlockCrop) (new XieBlockCrop(XieMod.Farming.BlockID.lettucePlant, 
				XieMod.lettuceTex, XieMod.Farming.Settings.lettuceGrowthChance)).setBlockName("lettucePlant");
		XieMod.lettucePlant.setAsBillboard(true);
		ModLoader.RegisterBlock(XieMod.lettucePlant);
		ModLoader.AddName(XieMod.lettucePlant, "Lettuce Plant");

		int wheatIndex = 38;
		int wheatTex[] = new int[8];
//		for (int i=0; i<8; i++) {wheatTex[i]=Block.crops.blockIndexInTexture+i;}
		for (int i=0; i<8; i++) {wheatTex[i]=i + wheatIndex;}
		XieMod.wheat = (XieBlockCrop) (new XieBlockCrop(XieMod.Farming.BlockID.wheatOverride, wheatTex, XieMod.Farming.Settings.wheatGrowthChance)).setBlockName("wheat");
		ModLoader.RegisterBlock(XieMod.wheat);
		ModLoader.AddName(XieMod.wheat, "Wheat Crop");
		XieMod.wheat.setDrop(Item.wheat.shiftedIndex);
		
		int hybridIndex = 46;
		int hybridTex[] = new int[8];
//		for (int i=0; i<7; i++) {hybridTex[i]=Block.crops.blockIndexInTexture+i;}
		for (int i=0; i<7; i++) {hybridTex[i]=i + wheatIndex;}
//		hybridTex[7]=ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/hybridwheat.png");
		hybridTex[7]=hybridIndex;
		XieMod.hybridWheat = (XieBlockCrop) (new XieBlockCrop(XieMod.Farming.BlockID.hybridWheat, hybridTex, XieMod.Farming.Settings.wheatGrowthChance, 3)).setBlockName("hWheat");
		ModLoader.RegisterBlock(XieMod.wheat);
		ModLoader.AddName(XieMod.wheat, XieMod.getName("Hybrid Wheat"));
		XieMod.hybridWheat.setDrop(Item.wheat.shiftedIndex);
    }
    
    // TODO This is a lot messier than it needs to be
    public void loadItems () {
//    	XieMod.hybridSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.hybridSeeds, XieMod.hybridWheat.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/hybridseeds.png")).setItemName("hybridSeed");
    	XieMod.hybridSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.hybridSeeds, XieMod.hybridWheat.blockID))
    	.setIconIndex(0).setItemName("hybridSeed");
    	ModLoader.AddName(XieMod.hybridSeeds, XieMod.getName("Hybrid Seeds"));
    	XieMod.wheat.hybridSeed = XieMod.hybridSeeds.shiftedIndex;
    	XieMod.hybridWheat.setSeeds(XieMod.hybridSeeds.shiftedIndex);
	
    	// override native seeds
    	Item.seeds = (new ItemSeeds(39, XieMod.wheat.blockID)).setIconCoord(9, 0).setItemName("seeds");
    	XieMod.wheat.setSeeds(Item.seeds.shiftedIndex);
    	
//		XieMod.watermelonPiece = (XieWatermelonPiece) (new XieWatermelonPiece(XieMod.Farming.ItemID.watermelonPiece, XieMod.Farming.FoodYield.watermelonPiece))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/watermelonpiece.png")).setItemName("xie_watermelonPiece");
		XieMod.watermelonPiece = (XieWatermelonPiece) (new XieWatermelonPiece(XieMod.Farming.ItemID.watermelonPiece, XieMod.Farming.FoodYield.watermelonPiece))
		.setIconIndex(1).setItemName("xie_watermelonPiece");
		ModLoader.AddName(XieMod.watermelonPiece, XieMod.getName("Watermelon Piece"));
	
//		XieMod.melonSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.melonSeeds, XieMod.watermelonVine.blockID))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/watermelonseeds.png")).setItemName("xie_watermelonSeed");
		XieMod.melonSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.melonSeeds, XieMod.watermelonVine.blockID))
		.setIconIndex(2).setItemName("xie_watermelonSeed");
		ModLoader.AddName(XieMod.melonSeeds, XieMod.getName("Watermelon Seeds"));
		XieMod.watermelonVine.setSeeds(XieMod.melonSeeds.shiftedIndex);
		
//		XieMod.pumpkinSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.pumpkinSeeds, XieMod.pumpkinVine.blockID))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/pumpkinseeds.png")).setItemName("xie_pumpkinSeeds");
		XieMod.pumpkinSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.pumpkinSeeds, XieMod.pumpkinVine.blockID))
		.setIconIndex(3).setItemName("xie_pumpkinSeeds");
		ModLoader.AddName(XieMod.pumpkinSeeds, XieMod.getName("Pumpkin Seeds"));
		XieMod.pumpkinVine.setSeeds(XieMod.pumpkinSeeds.shiftedIndex);
		
//		XieMod.pumpkinPiece = XieMod.newItem(XieMod.Farming.ItemID.pumpkinPiece, ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/pumpkinpiece.png"), "xie_pumpkinPiece");
//		XieMod.pumpkinPiece = (XieWatermelonPiece) (new XieWatermelonPiece(XieMod.Farming.ItemID.pumpkinPiece, XieMod.Farming.FoodYield.pumpkinPiece))
//		.setIconIndex(5).setItemName("xie_pumpkinPiece");
		XieMod.pumpkinPiece = XieMod.newItem(XieMod.Farming.ItemID.pumpkinPiece, 4, "xie_pumpkinPiece");
		ModLoader.AddName(XieMod.pumpkinPiece, XieMod.getName("Pumpkin Piece"));
		
//		XieMod.roastPumpkin = (ItemFood) (new ItemFood(XieMod.Farming.ItemID.roastPumpkin,XieMod.Farming.FoodYield.roastPumpkin, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/roastpumpkin.png")).setItemName("xie_roastPumpkin");
		XieMod.roastPumpkin = (XieItemFood) (new XieItemFood(XieMod.Farming.ItemID.roastPumpkin,XieMod.Farming.FoodYield.roastPumpkin, false))
		.setIconIndex(5).setItemName("xie_roastPumpkin");
		ModLoader.AddName(XieMod.roastPumpkin, XieMod.getName("Roast Pumpkin"));
		
//		XieMod.yellowSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.yellowSeeds, XieMod.yellowSeedling.blockID))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/yellowseeds.png")).setItemName("xie_yellowSeeds");
		XieMod.yellowSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.yellowSeeds, XieMod.yellowSeedling.blockID))
		.setIconIndex(7).setItemName("xie_yellowSeeds");
		ModLoader.AddName(XieMod.yellowSeeds, XieMod.getName("Yellow Flower Seeds"));
		XieMod.yellowSeedling.setSeeds(XieMod.yellowSeeds.shiftedIndex);
		
//		XieMod.redSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.redSeeds, XieMod.redSeedling.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/redseeds.png")).setItemName("xie_redSeeds");
		XieMod.redSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.redSeeds, XieMod.redSeedling.blockID))
		.setIconIndex(6).setItemName("xie_redSeeds");
		ModLoader.AddName(XieMod.redSeeds, XieMod.getName("Red Flower Seeds"));
		XieMod.redSeedling.setSeeds(XieMod.redSeeds.shiftedIndex);
		
//		XieMod.tomatoSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.tomatoSeeds, XieMod.tomatoPlant.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/tomatoseeds.png")).setItemName("tomatoSeeds");
		XieMod.tomatoSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.tomatoSeeds, XieMod.tomatoPlant.blockID))
		.setIconIndex(8).setItemName("tomatoSeeds");
		ModLoader.AddName(XieMod.tomatoSeeds, XieMod.getName("Tomato Seeds"));
		XieMod.tomatoPlant.setSeeds(XieMod.tomatoSeeds.shiftedIndex);
		
//		XieMod.cornSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.cornSeeds, XieMod.cornPlant.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/cornseeds.png")).setItemName("cornSeeds");
		XieMod.cornSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.cornSeeds, XieMod.cornPlant.blockID))
		.setIconIndex(9).setItemName("cornSeeds");
		ModLoader.AddName(XieMod.cornSeeds, XieMod.getName("Corn Seeds"));
		
//		XieMod.cottonSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.cottonSeeds, XieMod.cottonPlant.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/cottonseeds.png")).setItemName("cottonSeeds");
		XieMod.cottonSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.cottonSeeds, XieMod.cottonPlant.blockID))
		.setIconIndex(10).setItemName("cottonSeeds");
		ModLoader.AddName(XieMod.cottonSeeds, XieMod.getName("Cotton Seeds"));
		XieMod.cottonPlant.setSeeds(XieMod.cottonSeeds.shiftedIndex);
		
//		XieMod.lettuceSeeds = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.lettuceSeeds, XieMod.lettucePlant.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/lettuceseeds.png")).setItemName("lettuceSeeds");
		XieMod.lettuceSeeds = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.lettuceSeeds, XieMod.lettucePlant.blockID))
		.setIconIndex(11).setItemName("lettuceSeeds");
		ModLoader.AddName(XieMod.lettuceSeeds, XieMod.getName("Lettuce Seeds"));
		XieMod.lettucePlant.setSeeds(XieMod.lettuceSeeds.shiftedIndex);
		
//		XieMod.cornKernels = (ItemSeeds) (new ItemSeeds(XieMod.Farming.ItemID.cornKernels, XieMod.cornPlant.blockID))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/seeds/cornkernels.png")).setItemName("cornKernels");
		XieMod.cornKernels = (XieItemSeeds) (new XieItemSeeds(XieMod.Farming.ItemID.cornKernels, XieMod.cornPlant.blockID))
		.setIconIndex(12).setItemName("cornKernels");
		ModLoader.AddName(XieMod.cornKernels, XieMod.getName("Corn Kernels"));
		XieMod.cornPlant.setSeeds(XieMod.cornKernels.shiftedIndex);
		
//		XieMod.popcorn = (ItemFood) (new ItemFood(XieMod.Farming.ItemID.popcorn,XieMod.Farming.FoodYield.popcorn, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/popcorn.png")).setItemName("popcorn");
		XieMod.popcorn = (XieItemFood) (new XieItemFood(XieMod.Farming.ItemID.popcorn,XieMod.Farming.FoodYield.popcorn, false))
		.setIconIndex(13).setItemName("popcorn");
		ModLoader.AddName(XieMod.popcorn, XieMod.getName("Popcorn"));
		
//		XieMod.cornCob = (ItemFood) (new ItemFood(XieMod.Farming.ItemID.cornCob,XieMod.Farming.FoodYield.cornCob, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/corncob.png")).setItemName("cornCob");
		XieMod.cornCob = (XieItemFood) (new XieItemFood(XieMod.Farming.ItemID.cornCob,XieMod.Farming.FoodYield.cornCob, false))
		.setIconIndex(14).setItemName("cornCob");
		ModLoader.AddName(XieMod.cornCob, XieMod.getName("Corn cob"));
		XieMod.cornPlant.setDrop(XieMod.cornCob.shiftedIndex);
		
//		XieMod.cornCobCooked = (ItemFood) (new ItemFood(XieMod.Farming.ItemID.cornCobCooked,XieMod.Farming.FoodYield.cornCobCooked, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/corncobcooked.png")).setItemName("cornCobCooked");
		XieMod.cornCobCooked = (XieItemFood) (new XieItemFood(XieMod.Farming.ItemID.cornCobCooked,XieMod.Farming.FoodYield.cornCobCooked, false))
		.setIconIndex(15).setItemName("cornCobCooked");
		ModLoader.AddName(XieMod.cornCobCooked, XieMod.getName("Corn on the cob"));
		
		//XieMod.tomato = (ItemFood) (new ItemFood(XieMod.Farming.ItemID.tomato,XieMod.Farming.FoodYield.tomato, false))
		//	.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/tomato.png")).setItemName("tomato");
		//ModLoader.AddName(XieMod.tomato, "Tomato");
		XieMod.tomato = XieMod.addNewFood("Tomato",XieMod.Farming.ItemID.tomato,XieMod.Farming.FoodYield.tomato, 16);
		XieMod.tomatoPlant.setDrop(XieMod.tomato.shiftedIndex);
		
		XieMod.lettuce = XieMod.addNewFood("Lettuce", XieMod.Farming.ItemID.lettuce, XieMod.Farming.FoodYield.lettuce, 17);
		XieMod.lettucePlant.setDrop(XieMod.lettuce.shiftedIndex);
		
//		XieMod.cotton = XieMod.newItem(XieMod.Farming.ItemID.cotton, ModLoader.addOverride("/gui/items.png", "/Xie/img/items/cotton.png"), "cotton");
		XieMod.cotton = XieMod.newItem(XieMod.Farming.ItemID.cotton, 18, "cotton");
		ModLoader.AddName(XieMod.cotton, XieMod.getName("Cotton"));
		XieMod.cottonPlant.setDrop(XieMod.cotton.shiftedIndex);
		
		XieMod.orange = XieMod.addNewFood("Orange", XieMod.Farming.ItemID.orange, XieMod.Farming.FoodYield.orange, 19);
//		XieMod.lemon = XieMod.newItem(XieMod.Farming.ItemID.lemon, ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/lemon.png"), "Lemon");
		XieMod.lemon = XieMod.newItem(XieMod.Farming.ItemID.lemon, 20, "Lemon");
		XieMod.avocado = XieMod.addNewFood("Avocado", XieMod.Farming.ItemID.avocado, XieMod.Farming.FoodYield.avocado, 21);
		XieMod.orangeJuice = XieMod.addNewContainedFood("Orange Juice", XieMod.Farming.ItemID.orangeJuice, XieMod.Farming.FoodYield.orangeJuice, Block.glass.blockID, 22);
		XieMod.lemonade = XieMod.addNewContainedFood("Lemonade", XieMod.Farming.ItemID.lemonade, XieMod.Farming.FoodYield.lemonade, Block.glass.blockID, 23);
		
		XieMod.guacamole = XieMod.addNewContainedFood("Guacamole", XieMod.Farming.ItemID.guacamole, XieMod.Farming.FoodYield.guacamole, Item.bowlEmpty.shiftedIndex, 24);
		
		ModLoader.AddSmelting(XieMod.pumpkinPiece.shiftedIndex, new ItemStack(XieMod.roastPumpkin));
		ModLoader.AddSmelting(XieMod.cornCob.shiftedIndex, new ItemStack(XieMod.cornCobCooked));
		ModLoader.AddSmelting(XieMod.cornKernels.shiftedIndex, new ItemStack(XieMod.popcorn));
		
		// override native hoe
		if (!XieMod.xieCooking) {
			Item.hoeWood = (new XieItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
			Item.hoeStone = (new XieItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
			Item.hoeSteel = (new XieItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
			Item.hoeDiamond = (new XieItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
			Item.hoeGold = (new XieItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
		}
		
		// override native axes
		Item.axeSteel = (new XieItemAxe(2, EnumToolMaterial.IRON)).setIconCoord(2, 7).setItemName("hatchetIron");
		Item.axeWood = (new XieItemAxe(15, EnumToolMaterial.WOOD)).setIconCoord(0, 7).setItemName("hatchetWood");
		Item.axeStone = (new XieItemAxe(19, EnumToolMaterial.STONE)).setIconCoord(1, 7).setItemName("hatchetStone");
		Item.axeDiamond = (new XieItemAxe(23, EnumToolMaterial.EMERALD)).setIconCoord(3, 7).setItemName("hatchetDiamond");
		Item.axeGold = (new XieItemAxe(30, EnumToolMaterial.GOLD)).setIconCoord(4, 7).setItemName("hatchetGold");
		
		// override bonemeal
		Item.dyePowder = (new XieItemFertiliser(95)).setIconCoord(14, 4).setItemName("dyePowder");
		
		// add food items to lists		
		XieMod.fruitList.add(XieMod.watermelonPiece);
		XieMod.soupList.add(XieMod.pumpkinPiece);
		XieMod.soupList.add(XieMod.cornCob);
		XieMod.soupList.add(XieMod.tomato);
		XieMod.fryList.add(XieMod.tomato);
		XieMod.fryList.add(XieMod.cornCob);
		XieMod.saladList.add(XieMod.cornCob);
		XieMod.saladList.add(XieMod.tomato);
		XieMod.saladList.add(XieMod.lettuce);
		XieMod.saladList.add(XieMod.avocado);
		XieMod.flavorList.add(XieMod.yellowSeeds);
		XieMod.flavorList.add(XieMod.lemon);
		XieMod.flavorList.add(Item.sugar);
		XieMod.sammichList.add(XieMod.tomato);
		XieMod.sammichList.add(XieMod.lettuce);
		XieMod.sammichList.add(XieMod.avocado);
		XieMod.fruitList.add(XieMod.orange);
		XieMod.fruitList.add(XieMod.lemon);
		//XieMod.fruitList.add(XieMod.avocado);
		XieMod.sauceList.add(XieMod.guacamole);
		XieMod.sauceList.add(XieMod.tomato);
		XieMod.sauceList.add(XieMod.avocado);
		
		// add droppable seeds to list
		if (XieMod.Farming.Enable.flowerSeeds) XieMod.seedsDropList.add(XieMod.redSeeds);
		if (XieMod.Farming.Enable.flowerSeeds) XieMod.seedsDropList.add(XieMod.yellowSeeds);
		if (XieMod.Farming.Enable.pumpkins) XieMod.seedsDropList.add(XieMod.pumpkinSeeds);
		if (XieMod.Farming.Enable.melons) XieMod.seedsDropList.add(XieMod.melonSeeds);
	
	}
    
    public void AddRecipes() {
		// recipe to turn pumpkins and watermelons into pieces
		if (XieMod.Farming.Enable.pumpkins) {
			XieMod.addRecipe(new ItemStack(XieMod.pumpkinPiece, 4), new Object[] {
				"#", Character.valueOf('#'), Block.pumpkin
			});
			
			if (XieMod.Farming.Settings.pumpkinSeedYield>0)
				XieMod.addRecipe(new ItemStack(XieMod.pumpkinSeeds, XieMod.Farming.Settings.pumpkinSeedYield), new Object[] {
					"#", Character.valueOf('#'), XieMod.pumpkinPiece
				});
		}
		


		
		XieMod.addRecipe(new ItemStack(XieMod.watermelonPiece, 4), new Object[] {
			"#", Character.valueOf('#'), XieMod.watermelon
		});
		
		// hybrid seeds
		XieMod.addShapelessRecipe(new ItemStack(XieMod.hybridSeeds), new Object[] {Item.redstone, Item.seeds});
		
		// spliced plant seeds
		if (XieMod.Farming.Enable.tomatoes) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.tomatoSeeds), new Object[] {XieMod.redSeeds, XieMod.hybridSeeds});
		}
		
		if (XieMod.Farming.Enable.corn) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.cornSeeds), new Object[] {XieMod.yellowSeeds, XieMod.hybridSeeds});
			XieMod.addRecipe(new ItemStack(Item.bread), new Object[] {"XXX",Character.valueOf('X'),XieMod.cornKernels});
		}

		if (XieMod.Farming.Enable.cotton) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.cottonSeeds), new Object[] {XieMod.pumpkinSeeds, XieMod.hybridSeeds});
			XieMod.addRecipe(new ItemStack(Item.silk,1), new Object[] {"X","X",Character.valueOf('X'),XieMod.cotton});
		}
		
		if (XieMod.Farming.Enable.lettuce) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.lettuceSeeds), new Object[] {XieMod.melonSeeds, XieMod.hybridSeeds});
		}
		
		// spliced tree seeds
		if (XieMod.Farming.Enable.appleTrees) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.treeManager.get(XieMod.APPLE).sapling), new Object[] {XieMod.redSeeds, Block.sapling, XieMod.hybridSeeds});
		}
		
		if (XieMod.Farming.Enable.orangeTrees) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.treeManager.get(XieMod.ORANGE).sapling), new Object[] {XieMod.pumpkinSeeds, Block.sapling, XieMod.hybridSeeds});
		}
		
		if (XieMod.Farming.Enable.lemonTrees) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.treeManager.get(XieMod.LEMON).sapling), new Object[] {XieMod.yellowSeeds, Block.sapling, XieMod.hybridSeeds});
		}

		if (XieMod.Farming.Enable.avocadoTrees) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.treeManager.get(XieMod.AVOCADO).sapling), new Object[] {XieMod.melonSeeds, Block.sapling, XieMod.hybridSeeds});
		}

		// Add food recipes
		XieMod.addRecipe(new ItemStack(XieMod.orangeJuice), new Object[] {
			"X","X","Z", Character.valueOf('X'), XieMod.orange, Character.valueOf('Z'), Block.glass
		});
		
		XieMod.addRecipe(new ItemStack(XieMod.lemonade), new Object[] {
			"X","Y","Z", Character.valueOf('X'), Item.sugar, Character.valueOf('Y'), XieMod.lemon, Character.valueOf('Z'), Block.glass
		});
		
		XieMod.addShapelessRecipe(new ItemStack(XieMod.guacamole), new Object[] { XieMod.lemon, XieMod.avocado, XieMod.tomato, Item.bowlEmpty });
		
		if (XieMod.CHEATS) addCheatRecipes();
		
		if (XieMod.xieCooking) {
			mod_XieCooking.addDependantRecipes();
		}
    }
    
    private void addCheatRecipes() {
    	XieMod.addRecipe(new ItemStack(XieMod.guacamole), new Object [] {"X", Character.valueOf('X'), Block.glass});
    	
		if (XieMod.Farming.Enable.flowerSeeds) {
			XieMod.addRecipe(new ItemStack(XieMod.redSeeds, 64), new Object[] {
		           "##", Character.valueOf('#'), Block.plantRed
		        });
		
			XieMod.addRecipe(new ItemStack(XieMod.yellowSeeds, 64), new Object[] {
	           "##", Character.valueOf('#'), Block.plantYellow
	        });
		}
    	
		if (XieMod.Farming.Enable.pumpkins)
    	XieMod.addRecipe(new ItemStack(Block.pumpkin, 64), new Object[] {
		           "#X", Character.valueOf('#'), Item.diamond, Character.valueOf('X'), Item.coal
		        });
			
		if (XieMod.Farming.Enable.melons)
		XieMod.addRecipe(new ItemStack(XieMod.watermelon, 64), new Object[] {
	           "X#", Character.valueOf('#'), Item.diamond, Character.valueOf('X'), Item.stick
	        });
		
		XieMod.addRecipe(new ItemStack(Item.diamond,64), new Object[] {
	           "#", Character.valueOf('#'), Block.dirt
	        });
		
		XieMod.addRecipe(new ItemStack(Item.coal,64), new Object[] {
	           "#", Character.valueOf('#'), Item.diamond
	        });
	
		XieMod.addRecipe(new ItemStack(Block.wood,64), new Object[] {
			"#","#", Character.valueOf('#'), Block.dirt
		});
		
		XieMod.addRecipe(new ItemStack(Item.ingotIron,64), new Object[] {
			"###", Character.valueOf('#'), Block.dirt
		});
		
		XieMod.addRecipe(new ItemStack(Block.sand,64), new Object[] {
			"##", Character.valueOf('#'), Block.sand
		});
		
		XieMod.addRecipe(new ItemStack(Item.egg), new Object[] {
	           "#", Character.valueOf('#'), Item.stick
	        });
		
		XieMod.addRecipe(new ItemStack(Item.porkRaw), new Object[] {
	           "#", Character.valueOf('#'), Item.egg
	        });
		
		XieMod.addRecipe(new ItemStack(Item.bread), new Object[] {
	           "##", Character.valueOf('#'), Item.egg
	        });

		XieMod.addRecipe(new ItemStack(Block.tnt,64), new Object[] {
	           "#","X", Character.valueOf('#'), Item.egg, Character.valueOf('X'), Item.stick
	        });
		
		XieMod.addRecipe(new ItemStack(Item.bone), new Object[] {
	           "##","##", Character.valueOf('#'), Item.coal
	        });
		
		
		XieMod.addShapelessRecipe(new ItemStack(XieMod.cornKernels, 64), new Object[] {XieMod.cornSeeds, Item.seeds});
    }
    
    public void GenerateSurface(World world, Random random, int i, int j)
    {
    	if (XieMod.Farming.Enable.appleTrees && XieMod.Farming.Settings.appleTreeChance>0) {
    		if (random.nextInt(XieMod.Farming.Settings.appleTreeChance)==0) {
    			int x = i + random.nextInt(16) + 8;
    			int z = j + random.nextInt(16) + 8;
    			int y = world.getHeightValue(x, z);
    			XieMod.treeManager.generate(XieMod.APPLE, world, random, x, y, z);
    		}
    	}
    	
    	if (XieMod.Farming.Enable.orangeTrees && XieMod.Farming.Settings.orangeTreeChance>0) {
    		if (random.nextInt(XieMod.Farming.Settings.orangeTreeChance)==0) {
    			int x = i + random.nextInt(16) + 8;
    			int z = j + random.nextInt(16) + 8;
    			int y = world.getHeightValue(x, z);
    			XieMod.treeManager.generate(XieMod.ORANGE, world, random, x, y, z);
    		}
    	}
    	
    	if (XieMod.Farming.Enable.lemonTrees && XieMod.Farming.Settings.lemonTreeChance>0) {
    		if (random.nextInt(XieMod.Farming.Settings.lemonTreeChance)==0) {
    			int x = i + random.nextInt(16) + 8;
    			int z = j + random.nextInt(16) + 8;
    			int y = world.getHeightValue(x, z);
    			XieMod.treeManager.generate(XieMod.LEMON, world, random, x, y, z);
    		}
    	}
    	
    	if (XieMod.Farming.Enable.avocadoTrees && XieMod.Farming.Settings.avocadoTreeChance>0) {
    		if (random.nextInt(XieMod.Farming.Settings.avocadoTreeChance)==0) {
    			int x = i + random.nextInt(16) + 8;
    			int z = j + random.nextInt(16) + 8;
    			int y = world.getHeightValue(x, z);
    			XieMod.treeManager.generate(XieMod.AVOCADO, world, random, x, y, z);
    		}
    	}

    	if (XieMod.Farming.Enable.melons && XieMod.Farming.Settings.watermelonChance>0) {
    		if(random.nextInt(XieMod.Farming.Settings.watermelonChance) == 0)
    		{
    			int x = i + random.nextInt(16) + 8;
    			int	z = j + random.nextInt(16) + 8;
    			int y = world.getHeightValue(x, z);
    			generateWatermelon(world, random, x, y, z);
    		}
    	}

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
	
    public boolean generateWatermelon(World world, Random random, int i, int j, int k)
    {
        for(int l = 0; l < 64; l++)
        {
            int i1 = (i + random.nextInt(8)) - random.nextInt(8);
            int j1 = (j + random.nextInt(4)) - random.nextInt(4);
            int k1 = (k + random.nextInt(8)) - random.nextInt(8);
            if(world.isAirBlock(i1, j1, k1) 
            		&& world.getBlockId(i1, j1 - 1, k1) == Block.grass.blockID 
            		&& XieMod.watermelon.canPlaceBlockAt(world, i1, j1, k1)
            		&& isNearWater(world, i1,j1,k1))
            {
                world.setBlock(i1, j1, k1, XieMod.watermelon.blockID);
            }
        }

        return true;
    }
    
    // checks to make sure there's some water nearby
    private boolean isNearWater(World world, int x, int y, int z) {
    	int d = 3;
    	boolean foundWater = false;
    	
    	for (int i=-3; i<d; i++) {
    		for (int j=-3; j<d; j++) {
    			for (int k=-3; k<d; k++) {
    				
    				foundWater = (world.getBlockId(x+i, y+j, z+k) == Block.waterStill.blockID) || 
    					(world.getBlockId(x+i, y+j, z+k) == Block.waterMoving.blockID);
  
    				if (foundWater) return foundWater;
    			}
    		}
    	}
    	
    	return foundWater;
    }
}
