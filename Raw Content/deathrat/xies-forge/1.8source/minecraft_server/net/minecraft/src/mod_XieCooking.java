package net.minecraft.src;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import net.minecraft.server.MinecraftServer;

public class mod_XieCooking extends BaseModMp {
	
	private static mod_XieCooking instance;

	final static String modName = "Xie Cooking";
	final static String version = "1.7c";
	final static String settingsFile = "cooking.ini";

	Properties props;

	public mod_XieCooking() {
		instance = this;
		props = XieMod.loadProperties(settingsFile, defaultProperties());
		
		try {
			XieMod.readIntegerProperties(props, XieMod.Cooking.Settings.class.getDeclaredFields());
			XieMod.readBooleanProperties(props, XieMod.Cooking.Enable.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Cooking.FoodYield.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Cooking.BlockID.class.getDeclaredFields());
			XieMod.readIntegerProperties(props, XieMod.Cooking.ItemID.class.getDeclaredFields());
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		loadBlocks();
		loadItems();
		AddRecipes();
		
		// set update for each render frame
		if (XieMod.Cooking.Enable.saltRespawn) ModLoader.SetInGameHook(this, true, true);
		if (XieMod.Cooking.Settings.saltDepositInGameSpawnChance<1) XieMod.Cooking.Settings.saltDepositInGameSpawnChance=1;
		
		XieMod.xieCooking = true;
	}

	private String defaultProperties() {
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		
		try {
			bw.write("# "+this.toString()+" settings file");bw.newLine();
					
			XieMod.writeProperties(bw, "# Spawn chances for salt deposits / chance to till salt from sand", XieMod.Cooking.Settings.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Enable/disable (true/false)", XieMod.Cooking.Enable.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Food Healing, in half hearts", XieMod.Cooking.FoodYield.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Block IDs", XieMod.Cooking.BlockID.class.getDeclaredFields());
			XieMod.writeProperties(bw, "# Item IDs", XieMod.Cooking.ItemID.class.getDeclaredFields());
			
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
	
	// called each tick
//	public boolean OnTickInGame(Minecraft game) {
//		if (game.theWorld.rand.nextInt(XieMod.Cooking.Settings.saltDepositInGameSpawnChance)==0) {
//			Random rand = game.theWorld.rand;
//			
//			int dx = rand.nextInt(256) - 128;
//			int dz = rand.nextInt(256) - 128;
//			int x = (int) game.thePlayer.posX + dx;
//			int z = (int) game.thePlayer.posZ + dz;
//			
//			generateSaltDeposit(game.theWorld, rand, x, z);
//		}
//		return true;
//	}

	// TODO Tidy this up, there are more elegant ways
	public void loadBlocks () {		
//		XieMod.campfire = (XieBlockCampfire) (new XieBlockCampfire(XieMod.Cooking.BlockID.campfire, ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/campfire.png"), 3)).setBlockName("campfire");
		XieMod.campfire = (XieBlockCampfire) (new XieBlockCampfire(XieMod.Cooking.BlockID.campfire, 48 , 3)).setBlockName("campfire");
		XieMod.setBlockLightValue(XieMod.campfire,0.75F);
		ModLoader.RegisterBlock(XieMod.campfire);
		ModLoader.RegisterTileEntity(XieTileEntityCampfire.class,"Campfire");
//		ModLoader.AddName(XieMod.campfire, XieMod.getName("Campfire"));

//		XieMod.bonfire = (XieBlockCampfire) (new XieBlockCampfire(XieMod.Cooking.BlockID.bonfire, ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/bonfire.png"), 6)).setBlockName("bonfire");;
		XieMod.bonfire = (XieBlockCampfire) (new XieBlockCampfire(XieMod.Cooking.BlockID.bonfire, 49, 6)).setBlockName("bonfire");;
		XieMod.setBlockLightValue(XieMod.bonfire,1);
		ModLoader.RegisterBlock(XieMod.bonfire);
//		ModLoader.AddName(XieMod.bonfire, XieMod.getName("Bonfire"));

		XieMod.stoveIdle = (XieBlockStove) (new XieBlockStove(XieMod.Cooking.BlockID.stoveIdle, 45, false)).setBlockName("stove");;
		XieMod.setBlockLightValue(XieMod.stoveIdle,0F);
		ModLoader.RegisterBlock(XieMod.stoveIdle);
//		ModLoader.AddName(XieMod.stoveIdle, XieMod.getName("Stove"));

		XieMod.stoveActive = (XieBlockStove) (new XieBlockStove(XieMod.Cooking.BlockID.stoveActive, 45, true)).setBlockName("stove");
		XieMod.setBlockLightValue(XieMod.stoveActive,0.65F);
		ModLoader.RegisterBlock(XieMod.stoveActive);
		ModLoader.RegisterTileEntity(XieTileEntityStove.class,"Stove");
//		ModLoader.AddName(XieMod.stoveActive, XieMod.getName("Stove"));
		
		XieMod.saltDeposit = (XieBlockDeposit) (new XieBlockDeposit(XieMod.Cooking.BlockID.saltDeposit, ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/saltdeposit.png"), Material.snow)).setBlockName("saltDeposit");
//		XieMod.saltDeposit = (XieBlockDeposit) (new XieBlockDeposit(XieMod.Cooking.BlockID.saltDeposit, 50, Material.snow)).setBlockName("saltDeposit");
		ModLoader.RegisterBlock(XieMod.saltDeposit);
		XieMod.saltDeposit.setBase(Block.sand.blockID);
	}

	// TODO Tidy this up, there are more elegant ways
	public void loadItems () {
//		XieMod.debugFood = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.debugFood, 20)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/debug.png")).setItemName("xie_debugFood");
		XieMod.debugFood = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.debugFood, 42)).setIconIndex(42).setItemName("xie_debugFood");
//		ModLoader.AddName(XieMod.debugFood, XieMod.getName("Xie's Brains"));
		
		//XieMod.oil = XieMod.newItem(XieMod.Cooking.ItemID.oil,ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/oil.png"),"xie_bowlOil");
//		XieMod.oil = new Item(XieMod.Cooking.ItemID.oil).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/oil.png")).setItemName("Oil");
		XieMod.oil = new XieItem(XieMod.Cooking.ItemID.oil).setIconIndex(25).setItemName("Oil");
//		ModLoader.AddName(XieMod.oil, XieMod.getName("Oil"));

//		XieMod.friedEgg = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.friedEgg,XieMod.Cooking.FoodYield.friedEgg, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/friedegg.png")).setItemName("xie_friedEgg");
		XieMod.friedEgg = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.friedEgg,XieMod.Cooking.FoodYield.friedEgg, false, 9.6F))
		.setIconIndex(26).setItemName("xie_friedEgg");
//		ModLoader.AddName(XieMod.friedEgg, XieMod.getName("Fried Egg"));

//		XieMod.cheese = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.cheese, XieMod.Cooking.FoodYield.cheese, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/cheese.png")).setItemName("xie_cheese");
		XieMod.cheese = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.cheese, XieMod.Cooking.FoodYield.cheese, false, 9.6F))
		.setIconIndex(27).setItemName("xie_cheese");
//		ModLoader.AddName(XieMod.cheese, XieMod.getName("Cheese"));

		//XieMod.mayo = XieMod.addNewContainedFood("Mayonnaise", XieMod.Cooking.ItemID.mayo, XieMod.Cooking.FoodYield.mayo, Item.bowlEmpty.shiftedIndex);
//		XieMod.mayo = new Item(XieMod.Cooking.ItemID.mayo).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/mayonnaise.png")).setItemName("mayo");
		XieMod.mayo = new XieItem(XieMod.Cooking.ItemID.mayo).setIconIndex(28).setItemName("mayo");
//		ModLoader.AddName(XieMod.mayo, XieMod.getName("Mayonnaise"));
		
//		XieMod.failSoup = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.failSoup, XieMod.Cooking.FoodYield.failSoup))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/failsoup.png")).setItemName("xie_failSoup");
		XieMod.failSoup = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.failSoup, XieMod.Cooking.FoodYield.failSoup))
		.func_35422_a(Potion.hungerPotion.potionId, 30, 0, 0.8F)
		.setIconIndex(29).setItemName("xie_failSoup");
//		ModLoader.AddName(XieMod.failSoup, XieMod.getName("Weak Soup"));
		
//		XieMod.soup = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.soup, XieMod.Cooking.FoodYield.failSoup))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/soup.png")).setItemName("xie_soup");
		XieMod.soup = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.soup, XieMod.Cooking.FoodYield.failSoup))
		.func_35422_a(Potion.hungerPotion.potionId, 30, 0, 0.8F)
		.setIconIndex(30).setItemName("xie_soup");
//		ModLoader.AddName(XieMod.soup, XieMod.getName("Soup"));

//		XieMod.stew = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.stew, XieMod.Cooking.FoodYield.stew))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/stew.png")).setItemName("xie_stew");
		XieMod.stew = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.stew, XieMod.Cooking.FoodYield.stew))
		.setIconIndex(31).setItemName("xie_stew");
//		ModLoader.AddName(XieMod.stew, XieMod.getName("Stew"));

//		XieMod.failFry = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.failFry, XieMod.Cooking.FoodYield.failFry))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/failfry.png")).setItemName("xie_failFry");
		XieMod.failFry = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.failFry, XieMod.Cooking.FoodYield.failFry))
		.func_35422_a(Potion.hungerPotion.potionId, 30, 0, 0.8F)
		.setIconIndex(32).setItemName("xie_failFry");
//		ModLoader.AddName(XieMod.failFry, XieMod.getName("Fried Mess"));
		
//		XieMod.fry = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.fry, XieMod.Cooking.FoodYield.fry))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/fry.png")).setItemName("xie_fry");
		XieMod.fry = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.fry, XieMod.Cooking.FoodYield.fry))
		.setIconIndex(33).setItemName("xie_fry");
//		ModLoader.AddName(XieMod.fry, XieMod.getName("Fried Meal"));
		
//		XieMod.stirfry = (ItemSoup) (new ItemSoup(XieMod.Cooking.ItemID.stirfry, XieMod.Cooking.FoodYield.stirfry))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/stirfry.png")).setItemName("xie_stirFry");
		XieMod.stirfry = (XieItemSoup) (new XieItemSoup(XieMod.Cooking.ItemID.stirfry, XieMod.Cooking.FoodYield.stirfry))
		.setIconIndex(34).setItemName("xie_stirFry");
//		ModLoader.AddName(XieMod.stirfry, XieMod.getName("Stirfry"));

//		XieMod.sammich = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.sammich, XieMod.Cooking.FoodYield.sammich, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/sammich.png")).setItemName("xie_sammich");
		XieMod.sammich = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.sammich, XieMod.Cooking.FoodYield.sammich, false, 9.5F))
		.setIconIndex(35).setItemName("xie_sammich");
//		ModLoader.AddName(XieMod.sammich, XieMod.getName("Sammich"));
//		XieMod.tastySammich = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.tastySammich, XieMod.Cooking.FoodYield.tastySammich, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/tastysammich.png")).setItemName("xie_tastySammich");
		XieMod.tastySammich = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.tastySammich, XieMod.Cooking.FoodYield.tastySammich, false, 12.8F))
		.setIconIndex(36).setItemName("xie_tastySammich");
//		ModLoader.AddName(XieMod.tastySammich, XieMod.getName("Tasty Sammich"));
		
//		XieMod.pancakeBatter = (new ItemBucket(XieMod.Cooking.ItemID.pancakeBatter, -1)).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/pancakebatter.png")).setItemName("xie_bucketPancake");
		XieMod.pancakeBatter = (new XieItemBucket(XieMod.Cooking.ItemID.pancakeBatter, -1)).setIconIndex(37).setItemName("xie_bucketPancake");
//		ModLoader.AddName(XieMod.pancakeBatter, XieMod.getName("Pancake Batter"));
		XieMod.pancakeBatter.setMaxDamage(64);
//		XieMod.pancake = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.pancake, XieMod.Cooking.FoodYield.pancake, false))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/pancake.png")).setItemName("xie_pancake");
		XieMod.pancake = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.pancake, XieMod.Cooking.FoodYield.pancake, false, 12.8F))
		.setIconIndex(38).setItemName("xie_pancake");
//		ModLoader.AddName(XieMod.pancake, XieMod.getName("Pancakes"));

		//XieMod.salt = XieMod.newItem(XieMod.Cooking.ItemID.salt,ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/salt.png"),"salt");
//		XieMod.salt = new Item(XieMod.Cooking.ItemID.salt).setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/salt.png")).setItemName("salt");
		XieMod.salt = new XieItem(XieMod.Cooking.ItemID.salt).setIconIndex(39).setItemName("salt");
//		ModLoader.AddName(XieMod.salt, XieMod.getName("Salt"));
		XieMod.saltDeposit.setDrop(XieMod.salt.shiftedIndex);
		
//		XieMod.jerky = (ItemFood) (new ItemFood(XieMod.Cooking.ItemID.jerky, XieMod.Cooking.FoodYield.jerky, true))
//			.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/jerky.png")).setItemName("xie_jerky");
		XieMod.jerky = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.jerky, XieMod.Cooking.FoodYield.jerky, true, 8.5F))
		.setIconIndex(40).setItemName("xie_jerky");
//		ModLoader.AddName(XieMod.jerky, XieMod.getName("Beef Jerky"));

//		XieMod.fruitSalad = (ItemFood) (new ItemSoup(XieMod.Cooking.ItemID.fruitSalad, XieMod.Cooking.FoodYield.fruitSalad))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/fruitsalad.png")).setItemName("xie_fruitSalad");
		XieMod.fruitSalad = (ItemFood) (new XieItemSoup(XieMod.Cooking.ItemID.fruitSalad, XieMod.Cooking.FoodYield.fruitSalad))
		.setIconIndex(41).setItemName("xie_fruitSalad");
//		ModLoader.AddName(XieMod.fruitSalad, XieMod.getName("Fruit Salad"));
		
		//XieMod.salad = XieMod.addNewContainedFood("Salad", XieMod.Cooking.ItemID.salad, XieMod.Cooking.FoodYield.salad, Item.bowlEmpty.shiftedIndex);
//		XieMod.salad = (ItemFood) (new ItemSoup(XieMod.Cooking.ItemID.salad, XieMod.Cooking.FoodYield.salad))
//		.setIconIndex(ModLoader.addOverride("/gui/items.png", "/Xie/img/items/food/salad.png")).setItemName("salad");
		XieMod.salad = (ItemFood) (new XieItemSoup(XieMod.Cooking.ItemID.salad, XieMod.Cooking.FoodYield.salad))
		.setIconIndex(42).setItemName("salad");
//		ModLoader.AddName(XieMod.salad, XieMod.getName("Salad"));
		
		XieMod.jayCake = (XieItemFood) (new XieItemFood(XieMod.Cooking.ItemID.jaffaCake, XieMod.Cooking.FoodYield.jaffaCake, true, 9.6F))
		.setIconIndex(44).setItemName("xie_jaffaCake");
//		ModLoader.AddName(XieMod.jayCake, "Jaffa Cake");
		
		
		
		
		// Add foods to smeltable list
		if (XieMod.Cooking.Enable.friedEgg)
			ModLoader.AddSmelting(Item.egg.shiftedIndex, new ItemStack(XieMod.friedEgg));
		
		// Add foods to recipes list
		XieMod.fruitList.add(Item.appleRed);
		
		XieMod.sammichList.add(Item.porkCooked);
		XieMod.sammichList.add(Item.fishCooked);
		if (XieMod.Cooking.Enable.friedEgg) XieMod.sammichList.add(XieMod.friedEgg);
		if (XieMod.Cooking.Enable.cheese) XieMod.sammichList.add(XieMod.cheese);
		
		XieMod.sauceList.add(XieMod.mayo);
		
		XieMod.soupList.add(Item.porkRaw);
		XieMod.soupList.add(Item.fishRaw);
		XieMod.soupList.add(Item.egg);
		XieMod.soupList.add(Item.itemsList[Block.mushroomBrown.blockID]);
		XieMod.soupList.add(Item.itemsList[Block.mushroomRed.blockID]);
		XieMod.soupList.add(Item.wheat);
		
		XieMod.fryList.add(Item.porkRaw);
		XieMod.fryList.add(Item.fishRaw);
		XieMod.fryList.add(Item.egg);
		XieMod.fryList.add(Item.itemsList[Block.mushroomBrown.blockID]);
		XieMod.fryList.add(Item.itemsList[Block.mushroomRed.blockID]);
		
		if (XieMod.Cooking.Enable.salt) XieMod.flavorList.add(XieMod.salt);
		
		// override native hoe
		if (!XieMod.xieFarming) {
			Item.hoeWood = (new XieItemHoe(34, EnumToolMaterial.WOOD)).setIconCoord(0, 8).setItemName("hoeWood");
			Item.hoeStone = (new XieItemHoe(35, EnumToolMaterial.STONE)).setIconCoord(1, 8).setItemName("hoeStone");
			Item.hoeSteel = (new XieItemHoe(36, EnumToolMaterial.IRON)).setIconCoord(2, 8).setItemName("hoeIron");
			Item.hoeDiamond = (new XieItemHoe(37, EnumToolMaterial.EMERALD)).setIconCoord(3, 8).setItemName("hoeDiamond");
			Item.hoeGold = (new XieItemHoe(38, EnumToolMaterial.GOLD)).setIconCoord(4, 8).setItemName("hoeGold");
		}
	}
	
   public void GenerateSurface(World world, Random random, int i, int j)
    {
    	if (XieMod.Cooking.Enable.salt && XieMod.Cooking.Settings.saltDepositSpawnChance>0) {
    		if (random.nextInt(XieMod.Cooking.Settings.saltDepositSpawnChance)==0) {
    			generateSaltDeposit(world, random, i, j);
    		}
    	}
    }
   
   private boolean generateSaltDeposit(World world, Random random, int X, int Z)
   {
	   int i = X + random.nextInt(16) + 8;
	   int k = Z + random.nextInt(16) + 8;
	   int j = world.getHeightValue(i, k);
		
	   for(int l = 0; l < 64; l++)
       {
           int i1 = (i + random.nextInt(8)) - random.nextInt(8);
           int j1 = (j + random.nextInt(4)) - random.nextInt(4);
           int k1 = (k + random.nextInt(8)) - random.nextInt(8);
           if(world.isAirBlock(i1, j1+1, k1) &&
           		world.getBlockId(i1, j1, k1) == Block.sand.blockID && isNearWater(world, i1,j1,k1))
           {
        	   int dx = random.nextInt(3);
        	   int dz = random.nextInt(3);
               for (int x=-dx; x<=dx; x++)
            		   for (int z=-dz; z<=dz; z++) {
            			   if(world.isAirBlock(i1+x, j1+1, k1+z) && world.getBlockId(i1+x, j1, k1+z) == Block.sand.blockID)
            				   	world.setBlock(i1+x, j1, k1+z, XieMod.saltDeposit.blockID);
            		   }
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
	
	public void AddRecipes() {		
		// oil
		if (XieMod.Cooking.Enable.oil) 
		XieMod.addRecipe(new ItemStack(XieMod.oil), new Object[] {
           "X", "X", "#", Character.valueOf('X'), Item.seeds, Character.valueOf('#'), Item.bowlEmpty
        });

		if (XieMod.Cooking.Enable.mayo) 
		XieMod.addRecipe(new ItemStack(XieMod.mayo), new Object[] {
	       "Y", "X", "Z", Character.valueOf('X'), Block.plantYellow, Character.valueOf('Y'), Item.egg, 
	       		Character.valueOf('Z'), XieMod.oil
	    });
		
//		if (XieMod.Cooking.Enable.jaffaCake)
//		ModLoader.AddRecipe(new ItemStack(jaffaCake), new Object[] {
//			"#C#", "#O#", "#B#", Character.valueOf('C'), new ItemStack(Item.dyePowder.shiftedIndex, 1, 3), Character.valueOf('O'), XieMod.orange,
//				Character.valueOf('B'), Item.bread
//		});
		
//		if (XieMod.Cooking.Enable.jaffaCake)
//			ModLoader.AddRecipe(new ItemStack(XieMod.jayCake, 4), new Object[] {
//				" C ", " O ", " B ", Character.valueOf('C'), new ItemStack(Item.dyePowder.shiftedIndex, 1, 3), Character.valueOf('O'), XieMod.orange,
//					Character.valueOf('B'), Item.bread
//			});
	
		// campfire
		if (XieMod.Cooking.Enable.campfire) 
		XieMod.addRecipe(new ItemStack(XieMod.campfire), new Object[] {
			"XX","XX", Character.valueOf('X'), Item.stick
		});	
		
		// bonfire
		if (XieMod.Cooking.Enable.bonfire) 
		XieMod.addRecipe(new ItemStack(XieMod.bonfire), new Object[] {
			"XX","XX", Character.valueOf('X'), Block.wood
		});		
		
		// stove
		if (XieMod.Cooking.Enable.stove) 
		XieMod.addRecipe(new ItemStack(XieMod.stoveIdle), new Object[] {
			"X X"," X "," # ", Character.valueOf('X'), Item.ingotIron, Character.valueOf('#'), Block.stoneOvenIdle
		});	
	
		// pancake batter
		if (XieMod.Cooking.Enable.pancake) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.pancakeBatter, 1, 32), new Object [] {
				Item.bucketEmpty, Item.bucketMilk, Item.wheat, Item.egg
			});
			XieMod.addShapelessRecipe(new ItemStack(XieMod.pancakeBatter, 1, 16), new Object [] {
				Item.bucketEmpty, Item.bucketMilk, Item.wheat, Item.egg, Item.wheat, Item.egg
			});
			XieMod.addShapelessRecipe(new ItemStack(XieMod.pancakeBatter, 1, 0), new Object [] {
				Item.bucketEmpty, Item.bucketMilk, Item.wheat, Item.egg, Item.wheat, Item.egg, Item.wheat, Item.egg
			});
		}
	
		// jerky
		if (XieMod.Cooking.Enable.jerky) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.jerky,2), new Object[] {XieMod.salt, Item.leather});
		}
		
		if (XieMod.CHEATS) addCheatRecipes();
		
		addDependantRecipes();
	}
	
	public static void addDependantRecipes() {
		// Load recipes dependant on other mods
		
		// Mayo
		if (XieMod.xieFarming) {
			XieMod.addShapelessRecipe(new ItemStack(XieMod.mayo), new Object[] {XieMod.lemon, XieMod.oil, Item.egg});
			XieMod.sauceList.add(XieMod.mayo);
		}
		
		// check for nandoalt's cheese mod
		if (ModLoader.isModLoaded("mod_cheesemaker")) {
			Item i = XieMod.getFromMod("mod_cheesemaker","Cheese");
			if (i!=null) XieMod.sammichList.add(i);
		}
		
		// check for Dr Zhark's Farmcraft mod
		if (ModLoader.isModLoaded("mod_farmcraft")) {
			XieMod.addModFoodsToLists("mod_farmcraft","rawbeef,rawmutton,rawchicken",new List[] {XieMod.customFoodList, XieMod.soupList, XieMod.fryList});
			XieMod.addModFoodsToLists("mod_farmcraft","cookedbeef,cookedmutton,cookedchicken",new List[] {XieMod.customFoodList, XieMod.sammichList});
		}

		// check for ChefCraft
		if (ModLoader.isModLoaded("mod_ChefCraft")) {
			XieMod.addModFoodsToLists("mod_ChefCraft", "friedEgg,cheese,", new List[] {XieMod.customFoodList, XieMod.sammichList});
		}
		
		addGenericRecipes();
    }
	
	private static void addGenericRecipes () {				
		// salad
		if (XieMod.Cooking.Enable.salad && XieMod.saladList.size()>0) {
			for (int i=0; i<XieMod.saladList.size(); i++)
				for (int j=0; j<XieMod.saladList.size(); j++)
					if (i!=j)
						XieMod.addShapelessRecipe(new ItemStack(XieMod.salad), new Object[] {
							XieMod.saladList.get(i),
							XieMod.saladList.get(j),
							Item.bowlEmpty
						});
		}
		
		// fruit salad
		if (XieMod.Cooking.Enable.fruitSalad && XieMod.fruitList.size()>0)
			for (int i=0; i<XieMod.fruitList.size(); i++)
				for (int j=0; j<XieMod.fruitList.size(); j++)
					if (i!=j)
						XieMod.addShapelessRecipe(new ItemStack(XieMod.fruitSalad), new Object[] {
							XieMod.fruitList.get(i),
							XieMod.fruitList.get(j),
							Item.bowlEmpty
						});
		
		// sammiches
		if (XieMod.Cooking.Enable.sammich && XieMod.sammichList.size()>0) {
			
			for (int i=0; i<XieMod.sammichList.size(); i++)
				XieMod.addRecipe(new ItemStack(XieMod.sammich,3), new Object[] {
					"X", "Y", "X", Character.valueOf('X'), Item.bread, Character.valueOf('Y'), XieMod.sammichList.get(i)
		        });
		}
		
		if (XieMod.Cooking.Enable.tastySammich && XieMod.sammichList.size()>0 && XieMod.sauceList.size()>0) {
			for (int i=0; i<XieMod.sammichList.size(); i++) {
				for (int j=0; j<XieMod.sammichList.size(); j++) {
					if (i!=j) {
						for (int k=0; k<XieMod.sauceList.size(); k++) {
							XieMod.addRecipe(new ItemStack(XieMod.tastySammich,3), new Object[] {
								" # ", "IKJ", " # ", Character.valueOf('#'), Item.bread, 
								Character.valueOf('I'), XieMod.sammichList.get(i),
								Character.valueOf('J'), XieMod.sammichList.get(j),
								Character.valueOf('K'), XieMod.sauceList.get(k)
							});
						}
					}
				}
			}
		}
	}
    
    private void addCheatRecipes() {
    	XieMod.addShapelessRecipe(new ItemStack(Item.cookie), new Object[] {
	           Item.seeds
	        });
    	
    	XieMod.addRecipe(new ItemStack(Item.bucketMilk), new Object[] {
	           "#","X", Character.valueOf('#'), Item.seeds, Character.valueOf('X'), Item.bucketEmpty
	        });
    	
    	XieMod.addRecipe(new ItemStack(Block.cobblestone, 64), new Object[] {
	           "#","#","#", Character.valueOf('#'), Block.dirt
	        });
    	
		XieMod.addRecipe(new ItemStack(XieMod.stoveIdle), new Object[] {
			"XX","XX", Character.valueOf('X'), Block.dirt
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
    }
    
//	@Override
//	public Packet getTileEntityPacket(TileEntity te, int[] dataInt, float[] dataFloat, String[] dataString)
//	{
//		return ModLoaderMp.GetTileEntityPacket(instance, te.xCoord, te.yCoord, te.zCoord, 0, dataInt, dataFloat, dataString);
//	}
//	
//	@Override
//	public void sendPacketToAll(Packet230ModLoader p)
//	{
//		ModLoaderMp.SendPacketToAll(instance, p);
//	}
    
    public static void sendPacketToAll(Packet230ModLoader packet)
    {
    	ModLoaderMp.SendPacketToAll(instance, packet);
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
	
	public static XieItemFood jaffaCake;
}
