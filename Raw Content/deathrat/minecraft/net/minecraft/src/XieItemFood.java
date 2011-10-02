package net.minecraft.src;

import net.minecraft.src.forge.ITextureProvider;

public class XieItemFood extends ItemFood implements ITextureProvider
{

	public XieItemFood(int i, int j, boolean flag)
	{
		super(i, j, flag);
	}
	
	
	public String getTextureFile()
	{
		return "/Xie/img/items/xie_items.png";
	}
}
