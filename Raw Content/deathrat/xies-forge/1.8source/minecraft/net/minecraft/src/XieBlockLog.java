package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public class XieBlockLog extends BlockLog {

	public XieBlockLog(int i) {
		super(i);
		blockIndexInTexture = Block.wood.blockIndexInTexture;
		setHardness(4.0F);
		setStepSound(soundWoodFootstep);
		setBlockName("xieLog");
        destroyedPlace = new int[3];
	}

	public void onBlockRemoval(World world, int i, int j, int k)
	{
        byte byte0 = 4;
        int l = byte0 + 1;
        if(world.checkChunksExist(i - l, j - l, k - l, i + l, j + l, k + l))
        {
            for(int i1 = -byte0; i1 <= byte0; i1++)
            {
                for(int j1 = -byte0; j1 <= byte0; j1++)
                {
                    for(int k1 = -byte0; k1 <= byte0; k1++)
                    {
                    	int blockID = world.getBlockId(i + i1, j + j1, k + k1);
                        if(blockID != XieMod.leaves.blockID)
                        {
                            continue;
                        }
                        
                        int i2 = world.getBlockMetadata(i + i1, j + j1, k + k1);
                        if(!XieBlockLeaves.decayFlag(i2))
                        {
                        	XieBlockLeaves.setDecayFlag(world, i + i1, j + j1, k + k1, true);
                        }
                    }

                }

            }

        }
    }
	
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		if(canCut(world) && isTree(world, i, j, k))
		{
			destroyedPlace[0] = i;
			destroyedPlace[1] = j;
			destroyedPlace[2] = k;
			checkLogs(world, i, j, k);
		}
	}
	
	private boolean canCut(World world)
	{
		if(world.multiplayerWorld)
		{
			return false;
		}
		if(mod_XieFarming.isIdInList(-1, mod_XieFarming.treeCutterIDs))
		{
			return true;
		}
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		ItemStack itemstack = minecraft.thePlayer.getCurrentEquippedItem();
		if(itemstack == null)
		{
			return mod_XieFarming.isIdInList(0, mod_XieFarming.treeCutterIDs);
		} else
		{
			return itemstack != null && mod_XieFarming.isIdInList(itemstack.itemID, mod_XieFarming.treeCutterIDs);
		}
	}
	
	private boolean isTree(World world, int i, int j, int k)
	{
		for(; Block.blocksList[world.getBlockId(i, j + 1, k)] instanceof BlockLog; j++) { }
		int l = 0;
		for(int i1 = -l; i1 <= 1; i1++)
		{
			for(int j1 = -1; j1 <= 1; j1++)
			{
				for(int k1 = -1; k1 <= 1; k1++)
				{
					if(l > 2)
					{
						return true;
					}
	                if(Block.blocksList[world.getBlockId(i + i1, j + j1, k + k1)] instanceof BlockLeavesBase)
	                {
	                    l++;
	                }
				}
			}
		}
		
		return false;
	}
	
	private void checkLogs(World world, int i, int j, int k)
	{
        if(j < destroyedPlace[1] && destroyedPlace[0] == i && destroyedPlace[2] == k)
        {
            return;
        }
        int l = 1;
        for(int i1 = -l; i1 <= l; i1++)
        {
            for(int j1 = -l; j1 <= l; j1++)
            {
                for(int k1 = 0; k1 <= l; k1++)
                {
                    Block block = Block.blocksList[world.getBlockId(i + i1, j + k1, k + j1)];
                    if(!(block instanceof BlockLog))
                    {
                        continue;
                    }
                    int l1 = world.getBlockMetadata(i + i1, j + k1, k + j1);
                    if(block == null || !world.setBlockWithNotify(i + i1, j + k1, k + j1, 0))
                    {
                        continue;
                    }
                    block.dropBlockAsItem(world, i + i1, j + k1, k + j1, l1);
                    if(dropLeaves)
                    {
                        checkLeaves(world, i + i1, j + k1, k + j1);
                    }
                    checkLogs(world, i + i1, j + k1, k + j1);
                }

            }

        }

	}
	
    private void checkLeaves(World world, int i, int j, int k)
    {
        byte byte0 = 4;
        for(int l = -byte0; l < byte0; l++)
        {
            for(int i1 = -byte0; i1 < byte0; i1++)
            {
                for(int j1 = -byte0; j1 < byte0; j1++)
                {
                    int k1 = world.getBlockId(i + l, j + i1, k + j1);
                    int l1 = world.getBlockMetadata(i + l, j + i1, k + j1);
                    if(Block.blocksList[k1] instanceof BlockLeavesBase)
                    {
                        Block block = Block.blocksList[k1];
                        world.setBlockWithNotify(i + l, j + i1, k + j1, 0);
                        block.dropBlockAsItem(world, i + l, j + i1, k + j1, l1);
                    }
                }

            }

        }

    }
	
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(i == 1)
        {
            return 21;
        }
        if(i == 0)
        {
            return 21;
        }
        
        return blockIndexInTexture;
    }
	
    private int destroyedPlace[];
    private boolean dropLeaves;
}
