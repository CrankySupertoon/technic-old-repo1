package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemSoup extends ItemSoup implements ITextureProvider
{
	public XieItemSoup(int i, int j)
	{
		super(i,j);
	}
	
	
    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        super.onFoodEaten(itemstack, world, entityplayer);
        return new ItemStack(Item.bowlEmpty);
    }
	
    public ItemFood setPotionEffect(int i, int j, int k, float f)
    {
    	return super.setPotionEffect(i, j, k, f);
    }
    
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
