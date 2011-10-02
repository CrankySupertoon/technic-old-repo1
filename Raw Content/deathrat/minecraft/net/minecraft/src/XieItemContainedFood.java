package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 
import net.minecraft.src.forge.ITextureProvider;


public class XieItemContainedFood extends ItemFood implements ITextureProvider
{
	public int container;

    public XieItemContainedFood(int i, int j)
    {
        super(i, j, false);
        container = Item.bowlEmpty.shiftedIndex; // default
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/items/xie_items.png";
    }
    
    public XieItemContainedFood(int i, int j, int it) {
    	super(i, j, false);
    	container = it;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (itemstack.stackSize>1) return itemstack;
        super.onItemRightClick(itemstack, world, entityplayer);
        return new ItemStack(container,1,0);
    }
}
