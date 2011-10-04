package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.forge.ITextureProvider;

public class XieBlockCrop extends BlockCrops implements ITextureProvider {
	public int drop;
	public int texArray[];
	protected int growthChance;
	protected int grewFrom;
	public int hybridSeed;
	public int maxHeight;
	protected int renderType;

	public XieBlockCrop(int i, int tex[], int gc)
    {
        super(i, tex[0]);
        setTickOnLoad(true);
        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
        growthChance = gc;
        grewFrom = 0;
        texArray=tex;
        maxHeight=1;
        hybridSeed=0;
        renderType=6; // crops type
    }
	
	public XieBlockCrop(int i, int tex[], int gc, int h) {
		this(i,tex,gc);
		maxHeight=h;
	}
	
	public XieBlockCrop(int i, int tex[], int gc, int h, boolean f) {
		this(i,tex,gc,h);
		setAsBillboard(f);
	}
	
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {	
    	for (int n=0; n<maxHeight; n++) {
			int b = world.getBlockId(i, j - n, k);
			if (b == Block.tilledField.blockID) return true;
			if (b != this.blockID) return false;
		}
		
		return false;
	}
	
    public void setAsBillboard(boolean flag) {
    	if (flag) {
    		// as flower
            float f = 0.2F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3F, 0.5F + f);
            renderType=1;
    	} else {
    		// as crops
    		float f = 0.5F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
            renderType=6;
    	}
    }
    
    public int getRenderType()
    {
        return renderType;
    }
    
	public void setSeeds (int i) {
		grewFrom=i;
	}
	
	public void setDrop (int i) {
		drop=i;
	}

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        // super.updateTick(world, i, j, k, random);
    	checkFlowerChangeXie(world, i, j, k);
    	int netGrowthChance = (int)(growthChance/getGrowthRate(world,i,j,k));
    	if (netGrowthChance<=0) netGrowthChance=1;
        if(world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(netGrowthChance) == 0)
        {
            int growth = world.getBlockMetadata(i, j, k);
            if(growth < texArray.length) // number of stages of growth, also number of texture stages
            {
            	world.setBlockMetadataWithNotify(i, j, k, growth + 1);
            	world.markBlocksDirty(i, j-1, k, i, j, k);
            } else
            {
        		world.setBlockMetadataWithNotify(i, j, k, texArray.length-1);
        		if (world.getBlockId(i,j+1,k)==0 && canPlaceBlockAt(world, i, j, k)) world.setBlockAndMetadata(i, j + 1, k, this.blockID, 0);
            }
        }
    }
    
    public void checkFlowerChangeXie(World world, int i, int j, int k)
    {
        if(!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
            world.setBlockWithNotify(i, j, k, 0);
        }
    }

    // copypasta from BlockCrops
    private float getGrowthRate(World world, int i, int j, int k)
    {
        float f = 1.0F;
        int l = world.getBlockId(i, j, k - 1);
        int i1 = world.getBlockId(i, j, k + 1);
        int j1 = world.getBlockId(i - 1, j, k);
        int k1 = world.getBlockId(i + 1, j, k);
        int l1 = world.getBlockId(i - 1, j, k - 1);
        int i2 = world.getBlockId(i + 1, j, k - 1);
        int j2 = world.getBlockId(i + 1, j, k + 1);
        int k2 = world.getBlockId(i - 1, j, k + 1);
        boolean flag = j1 == blockID || k1 == blockID;
        boolean flag1 = l == blockID || i1 == blockID;
        boolean flag2 = l1 == blockID || i2 == blockID || j2 == blockID || k2 == blockID;
        for(int l2 = i - 1; l2 <= i + 1; l2++)
        {
            for(int i3 = k - 1; i3 <= k + 1; i3++)
            {
                int j3 = world.getBlockId(l2, j - 1, i3);
                float f1 = 0.0F;
                if(j3 == Block.tilledField.blockID)
                {
                    f1 = 1.0F;
                    if(world.getBlockMetadata(l2, j - 1, i3) > 0)
                    {
                        f1 = 3F;
                    }
                }
                if(l2 != i || i3 != k)
                {
                    f1 /= 4F;
                }
                f += f1;
            }

        }

        if(flag2 || flag && flag1)
        {
            f /= 2.0F;
        }
        return f;
    }
    
    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return (world.getBlockLightValue(i, j, k) >= 8 || world.canBlockSeeTheSky(i, j, k)) && canPlaceBlockAt(world, i, j-1, k);
    }
    
    public void fertilize(World world, int i, int j, int k) {
    	if (world.getBlockMetadata(i,j,k)<(texArray.length-1)) {
    		world.setBlockMetadataWithNotify(i, j, k, texArray.length-1);
    	} else {
    		if (world.getBlockId(i,j+1,k)==0 && canPlaceBlockAt(world, i, j, k)) world.setBlockAndMetadata(i, j + 1, k, this.blockID, texArray.length-1);
    	}
    }
    
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(j < 0) return texArray[0];
        else if (j >= texArray.length) return texArray[texArray.length-1]; 
        else return texArray[j];
    }
    
    protected void produce(World world, int i, int x, int y, int z) {
    	float f = 0.7F;
        float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
        float f2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
        float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
        EntityItem entityitem = new EntityItem(world, (float)x + f1, (float)y + f2, (float)z + f3, new ItemStack(i,1,0));
        entityitem.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(entityitem);
    }

    public int idDropped(int i, Random random)
    {
    	int bearingHeight = texArray.length-1;
    	if (maxHeight>1) bearingHeight--;
    	if (i>=bearingHeight) return drop;
    	else return -1;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }
    
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f)
    {
        if(world.multiplayerWorld)
        {
            return;
        }
        // drop crop yield if necessary
        int d = quantityDropped(world.rand);
        for(int pass = 0; pass < d; pass++)
        {
            if(world.rand.nextFloat() > f)
            {
                continue;
            }
            int drop = idDropped(l, world.rand);
            if(drop > 0)
            {
                produce(world, drop, i, j, k);
            }
        }
        // drop seeds if necessary
        for(int i1 = 0; i1 < 3; i1++)
        {
            if(world.rand.nextInt(15) <= l)
            {
            	if (hybridSeed>0 && world.rand.nextInt(XieMod.Farming.Settings.hybridSeedChance)==0) {
        			produce(world, hybridSeed, i, j, k);
        		} else {
        			produce(world, grewFrom, i, j, k);
        		}
            }
        }

    }
}
