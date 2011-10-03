package net.minecraft.src;



public class mod_MineFactoryCompat_XiesFarming extends BaseMod
{
	public mod_MineFactoryCompat_XiesFarming()
	{
		//Hybrid Wheat
//		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.Farming.ItemID.hybridSeeds, XieMod.Farming.BlockID.hybridWheat));
//		mod_MineFactory.registerHarvestable(new FactoryHarvestableStandard(XieMod.Farming.BlockID.hybridWheat, FactoryHarvestType.Normal));
	}
	
	@Override
	public void ModsLoaded()
	{
		//Hybrid Wheat
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.hybridWheat.blockID, FactoryHarvestType.Normal, XieMod.hybridWheat.texArray.length));
		mod_MineFactory.registerPlantable(new FactoryPlantableHybridWheat());
		//Trees
//		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.leaves.blockID, FactoryHarvestType.Tree, 0 ));
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXiesTrees(XieMod.leaves.blockID, FactoryHarvestType.Tree, 0 ));
		
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.log.blockID, FactoryHarvestType.Tree, 0));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.treeManager.get(0).sapling.blockID, XieMod.treeManager.get(0).sapling.blockID));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.treeManager.get(1).sapling.blockID, XieMod.treeManager.get(1).sapling.blockID));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.treeManager.get(2).sapling.blockID, XieMod.treeManager.get(2).sapling.blockID));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.treeManager.get(3).sapling.blockID, XieMod.treeManager.get(3).sapling.blockID));

		//Melon
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.watermelon.blockID, FactoryHarvestType.Normal, 0));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.melonSeeds.shiftedIndex, XieMod.watermelonVine.blockID));
		//Pumpkin
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(Block.pumpkin.blockID, FactoryHarvestType.Normal, 0));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.pumpkinSeeds.shiftedIndex, XieMod.pumpkinVine.blockID));
		//Corn
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.cornPlant.blockID, FactoryHarvestType.Normal, XieMod.cornPlant.texArray.length));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.cornSeeds.shiftedIndex, XieMod.cornPlant.blockID));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.cornKernels.shiftedIndex, XieMod.cornPlant.blockID));
		//Tomato
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.tomatoPlant.blockID, FactoryHarvestType.Normal, XieMod.tomatoPlant.texArray.length, XieMod.tomatoSeeds));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.tomatoSeeds.shiftedIndex, XieMod.tomatoPlant.blockID));
		//Cotton
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.cottonPlant.blockID, FactoryHarvestType.Normal, XieMod.cottonPlant.texArray.length, XieMod.cottonSeeds));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.cottonSeeds.shiftedIndex, XieMod.cottonPlant.blockID));
		//Lettuce
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.lettucePlant.blockID, FactoryHarvestType.Normal, XieMod.lettucePlant.texArray.length, XieMod.lettuceSeeds));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.lettuceSeeds.shiftedIndex, XieMod.lettucePlant.blockID));
		//Wheat
		mod_MineFactory.registerHarvestable(new FactoryHarvestableXies(XieMod.wheat.blockID, FactoryHarvestType.Normal, XieMod.wheat.texArray.length));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(Item.wheat.shiftedIndex, XieMod.wheat.blockID));
		
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.redSeeds.shiftedIndex, XieMod.redSeedling.blockID));
		mod_MineFactory.registerPlantable(new FactoryPlantableXies(XieMod.yellowSeeds.shiftedIndex, XieMod.yellowSeedling.blockID));

	}
	
	public String Version()
	{
		return "1.3";
	}
}
