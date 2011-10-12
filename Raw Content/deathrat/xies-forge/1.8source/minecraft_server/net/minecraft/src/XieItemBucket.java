package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemBucket extends ItemBucket implements ITextureProvider
{
	public XieItemBucket(int i, int j)
	{
		super(i,j);
	}
	
	
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
