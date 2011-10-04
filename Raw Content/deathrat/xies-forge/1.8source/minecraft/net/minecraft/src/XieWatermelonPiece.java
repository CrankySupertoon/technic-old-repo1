package net.minecraft.src;

// Xie 13-1-11

import net.minecraft.src.forge.ITextureProvider;

public class XieWatermelonPiece extends ItemFood implements ITextureProvider {

	public XieWatermelonPiece(int i, int j, float f) {
		super(i, j, f, false);
	}
	
	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		// first we check to see if the food is in a stack, if it is we don't let it get eaten
    	// Removed, to make watermelon pieces act like cookies
		// if (itemstack.stackSize>1) return itemstack;
    	itemstack.stackSize--;
		
        if (world.rand.nextInt(2)==0) {
        	float f = 0.7F;
            float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float f2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            EntityItem entityitem = new EntityItem(world, (float)entityplayer.posX + f1, (float)entityplayer.posY + f2, (float)entityplayer.posZ + f3, new ItemStack(Item.melonSeeds));
            entityitem.delayBeforeCanPickup = 10;
            world.entityJoinedWorld(entityitem);
        }
        	
        return itemstack;
    }
	
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return super.getItemUseAction(itemstack);
    }
	
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
