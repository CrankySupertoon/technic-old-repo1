package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

// copypasta of BlockSnow.java, Xie 10/2/11

import java.util.Random;

public class XieBlockDeposit extends BlockSand
{
	private int drop;
	private int base;
	public int baseTex;
	
	public int getDrop() { return drop; }
	public void setDrop(int d) { drop = d; }

	public int getBase() { return base; }
	public void setBase(int b) { 
		base = b; 
		baseTex = Block.blocksList[base].blockIndexInTexture;
	}

    public XieBlockDeposit(int i, int j, Material m)
    {
        //super(i, j, m);
        super(i,j);
    	drop = -1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    //public boolean canPlaceBlockAt(World world, int i, int j, int k)
    //{
     //   int l = world.getBlockId(i, j + 1, k);
      //  if(l == 0) return true;
     //   else return false;
   // }
    
    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
    	int i1 = drop;
        float f = 0.7F;
        double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(i1, 1, 0));
        entityitem.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(entityitem);
        //world.setBlockWithNotify(i, j, k, base);
        world.setBlock(i, j, k, base);
    }

    public int idDropped(int i, Random random)
    {
        return base;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }
    
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f)
    {
    	// no soup for you!
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side)
    {
        if(side == 1) // top
        {
            return blockIndexInTexture;
        } else return baseTex;
    }

}
