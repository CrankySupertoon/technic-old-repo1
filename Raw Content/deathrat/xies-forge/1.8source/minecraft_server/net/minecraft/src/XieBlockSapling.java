package net.minecraft.src;
// Copied from BlockSapling.java, which was
// 	 Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// 	 Jad home page: http://www.kpdus.com/jad.html
// 	 Decompiler options: packimports(3) braces deadcode 
// Edited by Xie 12-1-11

import java.util.Random;
import net.minecraft.src.forge.ITextureProvider;


public class XieBlockSapling extends BlockFlower implements ITextureProvider
{
	public int treeID;
	
    public XieBlockSapling(int i, int j, String treeName, int indexNumber)
    {
        //super(i, ModLoader.addOverride("/terrain.png", "/Xie/img/terrain/"+treeName.toLowerCase()+"sapling.png"));
        super(i, j);
        blockIndexInTexture = indexNumber;
    	float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setHardness(0.0F);
        setStepSound(Block.soundGrassFootstep);
        treeID=j;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
    	super.updateTick(world, i, j, k, random);
        if(world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(XieMod.treeManager.get(treeID).growthChance) == 0)
        {
            int l = world.getBlockMetadata(i, j, k);
            if(l < XieMod.treeManager.get(treeID).growthTime) // this changes the average growing time, higher is longer
            {
            	world.setBlockMetadataWithNotify(i, j, k, l + 1);
            } else
            {
            	growTree(world, i, j, k, random);
            }
        }
    }
    
    public void growTree(World world, int i, int j, int k, Random random)
    {
    	world.setBlock(i, j, k, 0);
    	if(!XieMod.treeManager.generate(treeID, world, random, i, j, k))
        {
            world.setBlockAndMetadata(i, j, k, blockID, treeID);
        }
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }
}

