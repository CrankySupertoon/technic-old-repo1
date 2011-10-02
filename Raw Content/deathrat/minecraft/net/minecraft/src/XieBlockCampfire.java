package net.minecraft.src;
// Copy-pasta from BlockFurnace.java which was
// 		Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// 		Jad home page: http://www.kpdus.com/jad.html
// 		Decompiler options: packimports(3) braces deadcode 
// Edited by Xie 14-1-11

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.ITextureProvider;

public class XieBlockCampfire extends BlockContainer implements ITextureProvider
{

    public XieBlockCampfire(int blockID, int tex, int v)
    {
        super(blockID, Material.rock);
        blockIndexInTexture = tex;
        this.setStepSound(Block.soundWoodFootstep);
        this.setHardness(0.3F);
        //Item.itemsList[blockID] = new ItemBlock(blockID - 256);
        if (v<4) setBlockBounds(0, 0, 0, 1.0F, 0.6F, 1.0F);
        voracity = v;
    }

    public int idDropped(int i, Random random)
    {
        return -1;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
    }

 
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if(!isActive)
        {
            return;
        }
        
        for (int i=0; i<voracity; i++) {
        	float baseX = (float)x + 0.5F;
        	float baseY = (float)y + 0.20F;
        	float baseZ = (float)z + 0.5F;
        	float varX = random.nextFloat() * 0.8F - 0.4F;
        	float varY = random.nextFloat() * 1.0F - 0.5F;
        	float varZ = random.nextFloat() * 0.8F - 0.4F;
        
        	world.spawnParticle("smoke", baseX+varX, baseY+varY, baseZ+varZ, 0.0D, 0.0D, 0.0D);
        	world.spawnParticle("flame", baseX+varX, baseY, baseZ+varZ, 0.0D, 0.0D, 0.0D);
        }
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
            XieTileEntityCampfire tileentity = (XieTileEntityCampfire)world.getBlockTileEntity(i, j, k);
            Minecraft mc = ModLoader.getMinecraftInstance();
            mc.displayGuiScreen(new XieGUICampfire(mc.thePlayer.inventory, tileentity));
            return true;
        }
    }

    protected TileEntity getBlockEntity()
    {
    	return new XieTileEntityCampfire(voracity);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    // billboard type, same as flowers and saplings
    public int getRenderType()
    {
        return 1;
    }
    
   /* public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
    	XieTileEntityCampfire campfire = (XieTileEntityCampfire)world.getBlockTileEntity(i,j,k);
    	campfire.ejectInventory();
    }
    
    public void onBlockDestroyedByExplosion(World world, int i, int j, int k, int l)
    {
    	XieTileEntityCampfire campfire = (XieTileEntityCampfire)world.getBlockTileEntity(i,j,k);
    	campfire.ejectInventory();
    }*/
    
    /*public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
    	// set entity on fire?
    }*/
    
    private boolean isActive = true;
    public int voracity;

}
