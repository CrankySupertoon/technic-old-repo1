package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemSoup extends ItemSoup implements ITextureProvider
{
	public XieItemSoup(int i, int j)
	{
		super(i,j);
	}
	
	
    public ItemStack func_35405_b(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        super.func_35405_b(itemstack, world, entityplayer);
        return new ItemStack(Item.bowlEmpty);
    }
	
    public ItemFood func_35422_a(int i, int j, int k, float f)
    {
    	return super.func_35422_a(i, j, k, f);
    }
    
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
