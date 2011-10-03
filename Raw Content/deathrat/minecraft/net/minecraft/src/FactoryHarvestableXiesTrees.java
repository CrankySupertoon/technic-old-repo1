package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public class FactoryHarvestableXiesTrees extends FactoryHarvestableStandard implements FactoryHarvestable
{
		private int sourceId;
		private FactoryHarvestType harvestType;
		private int stages;
		private Item [] itemDropArray;
		private Item itemDrop;
		private boolean hasAdditionalDrop;
        public FactoryHarvestableXiesTrees(int i, FactoryHarvestType j, int k, Item [] items)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
                itemDropArray = items;
                hasAdditionalDrop = true;
        }
        
        public FactoryHarvestableXiesTrees(int i, FactoryHarvestType j, int k, Item item)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
                itemDrop = item;
                hasAdditionalDrop = true;
        }
        
        public FactoryHarvestableXiesTrees(int i, FactoryHarvestType j, int k)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
        }
        
        @Override
        public boolean hasAdditionalDrops()
        {
                return true;
        }
        
        @Override
        public boolean canBeHarvested(int blockMetadata)
        {
        		return true;
        }
        
        @Override
        public List<ItemStack> getAdditionalDrops(int blockId, int blockMetadata, World world)
        {
                List<ItemStack> drops = new LinkedList<ItemStack>();
        for(int i1 = 0; i1 < 3; i1++)
        {
        	if(blockMetadata < 3)
        	{
            	int r = XieMod.treeManager.get(blockMetadata).saplingDropChance;
            	if (r>0)
            		if (world.rand.nextInt(r)==0)
            		{
            			drops.add(new ItemStack(XieMod.treeManager.get(blockMetadata).sapling));
            		}
        	}
        }
        return drops;
        }
}