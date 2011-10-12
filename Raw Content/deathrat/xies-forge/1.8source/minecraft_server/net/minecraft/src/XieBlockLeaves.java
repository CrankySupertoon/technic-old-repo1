package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.forge.ITextureProvider;

import net.minecraft.server.MinecraftServer;

public class XieBlockLeaves extends BlockLeaves implements ITextureProvider
{
	
	public XieBlockLeaves(int blockID)
	{
		super(blockID, 4);
		setTickOnLoad(true);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
//        setGraphicsLevel(true); 
    }
	
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return 0xffffff;
    }
	
	public void onBlockRemoval(World world, int i, int j, int k)
    {
        int l = 1;
        int i1 = l + 1;
        if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
        {
            for(int j1 = -l; j1 <= l; j1++)
            {
                for(int k1 = -l; k1 <= l; k1++)
                {
                    for(int l1 = -l; l1 <= l; l1++)
                    {
                        int i2 = world.getBlockId(i + j1, j + k1, k + l1);
                        if(i2 == this.blockID)
                        {
                            setDecayFlag(world, i + j1, j + k1, k + l1, true);
                        }
                    }

                }

            }

        }
    }

	public void updateTick(World world, int i, int j, int k, Random random)
    {
        if(world.singleplayerWorld)
        {
            return;
        }
        int l = world.getBlockMetadata(i, j, k);
        if(decayFlag(l))
        {
            byte byte0 = 4;
            int i1 = byte0 + 1;
            byte byte1 = 32;
            int j1 = byte1 * byte1;
            int k1 = byte1 / 2;
            if(adjacentTreeBlocks == null)
            {
            	adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
            }
            if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
            {
                for(int l1 = -byte0; l1 <= byte0; l1++)
                {
                    for(int k2 = -byte0; k2 <= byte0; k2++)
                    {
                        for(int i3 = -byte0; i3 <= byte0; i3++)
                        {
                            int k3 = world.getBlockId(i + l1, j + k2, k + i3);
                            if(k3 == XieMod.log.blockID)
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = 0;
                                continue;
                            }
                            if(k3 == this.blockID)
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -2;
                            } else
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -1;
                            }
                        }

                    }

                }

                for(int i2 = 1; i2 <= 4; i2++)
                {
                    for(int l2 = -byte0; l2 <= byte0; l2++)
                    {
                        for(int j3 = -byte0; j3 <= byte0; j3++)
                        {
                            for(int l3 = -byte0; l3 <= byte0; l3++)
                            {
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] != i2 - 1)
                                {
                                    continue;
                                }
                                if(adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] = i2;
                                }
                                if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] = i2;
                                }
                            }

                        }

                    }

                }

            }
            int j2 = adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
            if(j2 >= 0)
            {
                setDecayFlag(world, i, j, k, false);
            } else
            {
                removeLeaves(world, i, j, k);
            }
        }
    }

    private void removeLeaves(World world, int i, int j, int k)
    {
        dropBlockAsItemWithChance(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
        world.setBlockWithNotify(i, j, k, 0);
    }

    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f)
    {    	
    	int r = getTree(l).saplingDropChance;
    	if (r>0)
    		if (world.rand.nextInt(r)==0) {
    			spawnSapling(world, i, j, k, l);
    			return;
    		}
    	
    	super.dropBlockAsItemWithChance(world, i, j, k, l, 1.0F);
    }

    private void spawnSapling(World world, int i, int j, int k, int l) {
    	ItemStack it = new ItemStack(getTree(l).sapling);
    	
        float f1 = 0.7F;
        double d = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d1 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        double d2 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, it);
        entityitem.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(entityitem);
    }

    private XieTree getTree(int l) {
    	return XieMod.treeManager.get(XieTreeManager.getTreeIDFromMeta(l));
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    // sapling drops are dealt with elsewhere
    public int idDropped(int i, Random random)
    {
    	XieTree t = getTree(i);
    	
    	if (t.dropChance>0)
    		if (random.nextInt(t.dropChance)==0) return t.drop.shiftedIndex;
    	
    	if (XieMod.Farming.Settings.stickDropChance>0)
    		if (random.nextInt(XieMod.Farming.Settings.stickDropChance)==0) return Item.stick.shiftedIndex;
		
        return -1;
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
    	return getTree(j).texID;
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }
    
    // override native leaf block to prevent fruit from being "damaged"
    protected int damageDropped(int i)
    {
        return 0;
    }
    
    protected static int decayFlagBitSet = 8;
    protected static int decayFlagBitUnset = -9;
    
    public static void setDecayFlag(World world, int i, int j, int k, boolean flag) {
    	int meta = world.getBlockMetadata(i, j, k);
    	if (flag) {
    		meta = meta | decayFlagBitSet;
     	} else {
     		meta = meta & decayFlagBitUnset;
     	}
    	world.setBlockMetadataWithNotify(i, j, k, meta);
    }
    
    public static boolean decayFlag(int meta) {
    	return ((meta & decayFlagBitSet) != 0);
    }
}
