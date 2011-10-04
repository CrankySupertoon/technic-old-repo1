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
	
	
    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
    	return super.onFoodEaten(itemstack, world, entityplayer);
    }
    
    public ItemFood setPotionEffect(int i, int j, int k, float f)
    {
    	return super.setPotionEffect(i, j, k, f);
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
