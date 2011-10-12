package net.minecraft.src;

public class XieBlockLog extends BlockLog {

	public XieBlockLog(int i) {
		super(i);
		blockIndexInTexture = Block.wood.blockIndexInTexture;
		setHardness(2.0F);
		setStepSound(soundWoodFootstep);
		setBlockName("xieLog");
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
	
}
