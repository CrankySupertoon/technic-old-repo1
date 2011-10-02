package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemSoup extends ItemSoup implements ITextureProvider
{
	public XieItemSoup(int i, int j)
	{
		super(i,j);
	}
	
	
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
