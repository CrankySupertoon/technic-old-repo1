package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemFood extends ItemFood implements ITextureProvider
{

	public XieItemFood(int i, int j, boolean flag)
	{
		super(i, j, flag);
	}
	
	public XieItemFood(int i, int j, boolean flag, float f)
	{
		super(i, j, f, flag);
	}
	
	
//    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
//    {
//    	return super.func_35405_b(itemstack, world, entityplayer);
//    }
    
	public ItemStack func_35405_b(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if(world.singleplayerWorld) {return super.func_35405_b(itemstack, world, entityplayer);}
    	if (itemstack.itemID == XieMod.jayCake.shiftedIndex)
    	{
    		if(world.rand.nextInt(100) <= XieMod.Cooking.Settings.surpriseChance)
    		{
    			EntitySilverfish lolsilverfish = new EntitySilverfish(world);
    			lolsilverfish.setPosition(entityplayer.posX, entityplayer.posY, entityplayer.posZ);
//    			double lolX,lolY,lolZ;
//    			lolX = entityplayer.posX;
//    			lolY = entityplayer.posY;
//    			lolZ = entityplayer.posZ;
//    			lolsilverfish.setLocationAndAngles(lolX, lolY, lolZ, 0.0F, 0.0F);
    			world.entityJoinedWorld(lolsilverfish);
    			world.entityJoinedWorld(lolsilverfish);
    			lolsilverfish.spawnExplosionParticle();
    		}
    	}
    	return super.func_35405_b(itemstack, world, entityplayer);
	}
	
	
    public ItemFood func_35422_a(int i, int j, int k, float f)
    {
    	return super.func_35422_a(i, j, k, f);
    }
	
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.eat;
    }

	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
