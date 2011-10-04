package net.minecraft.src;

public class XieItemFertiliser extends ItemDye {

	public XieItemFertiliser(int i) {
		super(i);
	}
	
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (itemstack.getItemDamage() == 15) {
        	int bid = world.getBlockId(i, j, k);
        	Block block = Block.blocksList[bid];
        	
        	// check for custom crops
            if(block instanceof XieBlockSapling)
            {
                ((XieBlockSapling)block).growTree(world, i, j, k, world.rand);
                itemstack.stackSize--;
                return true;
            }
            if(block instanceof XieBlockSeedling)
            {
                ((XieBlockSeedling)block).fertilize(world, i, j, k);
                itemstack.stackSize--;
                return true;
            }
            if(block instanceof XieBlockCrop)
            {
                ((XieBlockCrop)block).fertilize(world, i, j, k);
                itemstack.stackSize--;
                return true;
            }
        }
    	
        return super.onItemUse(itemstack, entityplayer, world, i, j, k, l);
    }
}
