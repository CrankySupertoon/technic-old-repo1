package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public class FactoryHarvestableXies extends FactoryHarvestableStandard implements FactoryHarvestable
{
		private int sourceId;
		private FactoryHarvestType harvestType;
		private int stages;
		private Item [] itemDropArray;
		private Item itemDrop;
		private boolean hasAdditionalDrop;
        public FactoryHarvestableXies(int i, FactoryHarvestType j, int k, Item [] items)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
                itemDropArray = items;
                hasAdditionalDrop = true;
        }
        
        public FactoryHarvestableXies(int i, FactoryHarvestType j, int k, Item item)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
                itemDrop = item;
                hasAdditionalDrop = true;
        }
        
        public FactoryHarvestableXies(int i, FactoryHarvestType j, int k)
        {
                super(i, j);
                sourceId = i;
                harvestType = j;
                stages = k;
                hasAdditionalDrop = false;
        }
        
        @Override
        public boolean hasAdditionalDrops()
        {
                return hasAdditionalDrop;
        }
        
        @Override
        public boolean canBeHarvested(int blockMetadata)
        {
        	if (stages > 0)
        	{
                return blockMetadata == stages;
        	}
        	else
        	{
        		return true;
        	}
        }
        
        @Override
        public List<ItemStack> getAdditionalDrops(int blockId, int blockMetadata, World world)
        {
                List<ItemStack> drops = new LinkedList<ItemStack>();
        for(int i1 = 0; i1 < 3; i1++)
        {
            if(world.rand.nextInt(15) <= 7)
            {
            	if(itemDropArray.length > 0)
            	{
            		for(int i2 = 0; i2 < itemDropArray.length; i2++)
            		{
            			drops.add(new ItemStack(itemDropArray[i2]));
            		}
            	}
            	else
            	{
            		drops.add(new ItemStack(itemDrop));
            	}
            }
        }
        return drops;
        }
}