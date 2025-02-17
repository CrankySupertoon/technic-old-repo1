// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;
import net.minecraft.src.forge.ITextureProvider;


// Referenced classes of package net.minecraft.src:
//            Item, World, Block, ItemStack, 
//            EntityPlayer

public class XieItemSeeds extends ItemSeeds implements ITextureProvider
{

    public XieItemSeeds(int i, int j)
    {
        super(i, j);
        blockType = j;
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/items/xie_items.png";
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if(l != 1)
        {
            return false;
        }
        int i1 = world.getBlockId(i, j, k);
        if(i1 == Block.tilledField.blockID && world.isAirBlock(i, j + 1, k))
        {
            world.setBlockWithNotify(i, j + 1, k, blockType);
            itemstack.stackSize--;
            return true;
        } else
        {
            return false;
        }
    }

    private int blockType;
}
