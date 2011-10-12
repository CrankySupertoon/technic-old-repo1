package net.minecraft.src;

import net.minecraft.src.Item;
//import net.minecraft.src.forge.ITextureProvider;

public class XieItemHoe extends ItemHoe {

	public XieItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, enumtoolmaterial);
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (super.onItemUse(itemstack, entityplayer, world, i, j, k, l)) {
        	if(XieMod.xieFarming)
            {
        		Item seedz = getSeedDrop(world, i, j, k);
        		
        		if (seedz!=null) {
                    float f = 0.7F;
                    float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float f2 = 1.2F;
                    float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(world, (float)i + f1, (float)j + f2, (float)k + f3, new ItemStack(seedz));
                    entityitem.delayBeforeCanPickup = 10;
                    world.entityJoinedWorld(entityitem);
                }
            }
        	return true;
        } else 
        // If XieCooking is enabled, dig salt from sand
        if (XieMod.xieCooking && XieMod.Cooking.Enable.salt && XieMod.Cooking.Settings.saltTillChance>0) {
        	int i1 = world.getBlockId(i, j, k);
            Material material = world.getBlockMaterial(i, j + 1, k);

            if (i1 == Block.sand.blockID) {
        		Block block = Block.sand;
	        	world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.stepSoundDir(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	            if(world.singleplayerWorld)
	            {
	                return true;
	            }
	            itemstack.damageItem(1, entityplayer);
	            if(world.rand.nextInt(XieMod.Cooking.Settings.saltTillChance) == 0)
	            {
	                float f = 0.7F;
	                float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
	                float f2 = 1.2F;
	                float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
	                EntityItem entityitem = new EntityItem(world, (float)i + f1, (float)j + f2, (float)k + f3, new ItemStack(XieMod.salt));
	                entityitem.delayBeforeCanPickup = 10;
	                world.entityJoinedWorld(entityitem);
	                world.setBlockWithNotify(i, j, k, 0);
	            }
	            return true;
        	} else return false;
        } else return false;
    }
	
    private Item getSeedDrop(World world, int i, int j, int k) {
    	int r = XieMod.Farming.Settings.seedTillChance;
    	if (getMaxDamage()>1000) r = 5;

    	if (world.rand.nextInt(r)==0) {
    		// drop a special seed
    		int n = XieMod.seedsDropList.size();
    		Item it = null;
    		if (n>0) return XieMod.seedsDropList.get(world.rand.nextInt(n));
    	}

    	// check for location based chances of other seed drops!
    	final int d = 3;
    	int n = 5;
    	if (getMaxDamage()>1000) n = 1;
    	for (int x=-d; x<=d; x++)
    		for (int y=-d; y<=d; y++)
    			for (int z=-d; z<=d; z++) {
    				int blockID = world.getBlockId(i+x, j+y, k+z);
    				if (XieMod.Farming.Enable.pumpkins && blockID==Block.pumpkin.blockID && world.rand.nextInt(n)==0) return Item.pumpkinSeeds;
    				if (XieMod.Farming.Enable.melons && blockID==Block.melon.blockID && world.rand.nextInt(n)==0) return Item.melonSeeds;
    				if (XieMod.Farming.Enable.flowerSeeds && blockID==Block.plantYellow.blockID && world.rand.nextInt(n)==0) return XieMod.yellowSeeds;
    				if (XieMod.Farming.Enable.flowerSeeds && blockID==Block.plantRed.blockID && world.rand.nextInt(n)==0) return XieMod.redSeeds;
    			}

    	return null;
    }
}
