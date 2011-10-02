package net.minecraft.src;
// Copy-pasta from BlockFurnace.java which was
// 		Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// 		Jad home page: http://www.kpdus.com/jad.html
// 		Decompiler options: packimports(3) braces deadcode 
// Edited by Xie 14-1-11


import java.util.Random;

import net.minecraft.client.Minecraft;

public class XieBlockStove extends BlockContainer
{
	public XieBlockStove(int blockID, int ignoredBaseTex, boolean flag)
    {
        super(blockID, Material.rock);
        isActive = flag;
        blockIndexInTexture = 45; 
        setStepSound(Block.soundStoneFootstep);
        setHardness(3.5F);
    }

    public int idDropped(int i, Random random)
    {
        return XieMod.stoveIdle.blockID;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        setDefaultDirection(world, i, j, k);
    }

    private void setDefaultDirection(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        byte byte0 = 3;
        if(Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
        {
            byte0 = 3;
        }
        if(Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 2;
        }
        if(Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
        {
            byte0 = 5;
        }
        if(Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
        {
            byte0 = 4;
        }
        world.setBlockMetadataWithNotify(i, j, k, byte0);
    }

    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side)
    {
        if(side == 1) // top
        {
            return Block.blockSteel.blockIndexInTexture;
        }
        if(side == 0) // base
        {
            return Block.stone.blockIndexInTexture;
        }
        
        int facing = iblockaccess.getBlockMetadata(i, j, k);
        if(side != facing)
        {
            return blockIndexInTexture;
        }
        
        // else assumed facing side
        
        if(isActive)
        {
            return blockIndexInTexture + 16;
        } else
        {
            return blockIndexInTexture - 1;
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if(!isActive)
        {
            return;
        } // else show flame and smoke particles
        
        int facing = world.getBlockMetadata(i, j, k);
        float f = (float)i + 0.5F;
        float f1 = (float)j + 0.0F + (random.nextFloat() * 6F) / 16F;
        float f2 = (float)k + 0.5F;
        float f3 = 0.52F;
        float f4 = random.nextFloat() * 0.6F - 0.3F;
        if(facing == 4)
        {
            world.spawnParticle("smoke", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        } else
        if(facing == 5)
        {
            world.spawnParticle("smoke", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
        } else
        if(facing == 2)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
        } else
        if(facing == 3)
        {
            world.spawnParticle("smoke", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
        }
    }

    public int getBlockTextureFromSide(int side)
    {
        if(side == 1) // top
        {
            return Block.blockSteel.blockIndexInTexture;
        }
        if(side == 0) // base
        {
            return Block.stone.blockID;
        }
        if(side == 3) // front
        {
            return blockIndexInTexture - 1;
        } else
        {
            return blockIndexInTexture;
        }
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
            XieTileEntityStove tileentity = (XieTileEntityStove)world.getBlockTileEntity(i, j, k);
            Minecraft mc = ModLoader.getMinecraftInstance();
            mc.displayGuiScreen(new XieGUIStove(mc.thePlayer.inventory, tileentity));
            return true;
        }
    }

    public static void updateBlockState(boolean flag, World world, int i, int j, int k)
    {
        int l = world.getBlockMetadata(i, j, k);
        TileEntity tileentity = world.getBlockTileEntity(i, j, k);
        if(flag)
        {
            world.setBlockWithNotify(i, j, k, XieMod.stoveActive.blockID);
        } else
        {
            world.setBlockWithNotify(i, j, k, XieMod.stoveIdle.blockID);
        }
        world.setBlockMetadataWithNotify(i, j, k, l);
        tileentity.validate();
        world.setBlockTileEntity(i, j, k, tileentity);
    }

    protected TileEntity SetBlockEntity()
    {
        return new XieTileEntityStove();
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if(l == 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 2);
        }
        if(l == 1)
        {
            world.setBlockMetadataWithNotify(i, j, k, 5);
        }
        if(l == 2)
        {
            world.setBlockMetadataWithNotify(i, j, k, 3);
        }
        if(l == 3)
        {
            world.setBlockMetadataWithNotify(i, j, k, 4);
        }
    }

    private final boolean isActive;
  
	protected TileEntity getBlockEntity() {
		return new XieTileEntityStove();
	}
}
