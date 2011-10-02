package net.minecraft.src;

public class XieTree {

	public int id;
	public String name;
	public Item drop;
	public int dropChance;
	public int saplingDropChance;
	public int texID;
	
	public int growthChance;
	public int growthTime;
	
	public XieBlockSapling sapling;
	
	// default constructor
	public XieTree(int treeID, String treeName) {
		id = treeID;
		name = treeName;
		//texID = ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/"+treeName.toLowerCase()+"leaves.png");
		if(treeName == "Apple")
		{
			texID = 1;
		}
		if(treeName == "Orange")
		{
			texID = 2;
		}
		if(treeName == "Lemon")
		{
			texID = 3;
		}
		if(treeName == "Avocado")
		{
			texID = 4;	
		}
		
		// defaults
		drop = null;
		dropChance = 0;
		saplingDropChance = 12; 
		growthChance = 5;
		growthTime = 15;
		sapling = null;
	}
	
	// complete constructor
	public XieTree(int treeID, String treeName, Item treeDrop, int chance, int saplingBlockID, int saplingChance, int growthC, int growthT) {
		this(treeID,treeName);
		drop = treeDrop;
		dropChance = chance;
		saplingDropChance = saplingChance;
		growthChance = growthC;
		growthTime = growthT;
		int indexNumber = 0;
		if(treeName == "Apple")
		{
			indexNumber = 6;
		}
		if(treeName == "Orange")
		{
			indexNumber = 7;
		}
		if(treeName == "Lemon")
		{
			indexNumber = 8;
		}
		if(treeName == "Avocado")
		{
			indexNumber = 9;
		}
		sapling = (XieBlockSapling) (new XieBlockSapling(saplingBlockID, treeID, treeName, indexNumber).setBlockName(treeName+"Sapling"));
		ModLoader.RegisterBlock(sapling);
		ModLoader.AddName(sapling, XieMod.getName(treeName+" Sapling"));
	}
}
