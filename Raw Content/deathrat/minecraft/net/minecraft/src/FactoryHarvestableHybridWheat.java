package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public class FactoryHarvestableHybridWheat extends FactoryHarvestableStandard implements FactoryHarvestable
{
        public FactoryHarvestableHybridWheat()
        {
                super(XieMod.hybridWheat.blockID, FactoryHarvestType.Normal);
        }
        
        @Override
        public boolean hasAdditionalDrops()
        {
                return true;
        }
        
        @Override
        public boolean canBeHarvested(int blockMetadata)
        {
                return blockMetadata == 8;
        }
        
        @Override
        public List<ItemStack> getAdditionalDrops(int blockId, int blockMetadata, World world)
        {
                List<ItemStack> drops = new LinkedList<ItemStack>();
        for(int i1 = 0; i1 < 3; i1++)
        {
            if(world.rand.nextInt(15) <= 7)
            {
                drops.add(new ItemStack(Item.seeds));
            }
        }
        return drops;
        }
}