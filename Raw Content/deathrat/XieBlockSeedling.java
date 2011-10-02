package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.forge.ITextureProvider;


public class XieBlockSeedling extends BlockFlower implements ITextureProvider {
	public int growsInto;
	public int texArray[];
	protected int growthChance;
	protected int grewFrom;

	public XieBlockSeedling(int i, int tex[], int growsIntoThis, int gc)
    {
        super(i, tex[0]);
        setTickOnLoad(true);
        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
        growsInto=growsIntoThis;
        growthChance = gc;
        grewFrom = -1;
        texArray=tex;
    }
	
	public void setSeeds (int i) {
		grewFrom=i;
	}

    public void updateTick(World world, int i, int j, int k, Random random)
    {
    	
        super.updateTick(world, i, j, k, random);
        if(world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(growthChance) == 0)
        {
            int growth = world.getBlockMetadata(i, j, k);
            if(growth < texArray.length) // number of stages of growth, also number of texture stages
            {
            	world.setBlockMetadataWithNotify(i, j, k, growth + 1);
            } else
            {
            	fertilize(world, i, j, k);
            }
        }
    }
    
    public void fertilize(World world, int i, int j, int k)
    {
    	world.setBlockWithNotify(i, j, k, growsInto);
    }
    
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if(j < 0) return texArray[0];
        else if (j >= texArray.length) return texArray[texArray.length-1]; 
        else return texArray[j];
    }

    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
        super.onBlockDestroyedByPlayer(world, i, j, k, l);
       
        if(!world.multiplayerWorld)
        {
            for(int i1 = 0; i1 < 3; i1++)
            {
            	if(world.rand.nextInt(10) <= l)
                {
                    float f = 0.7F;
                    float f1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float f2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    float f3 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
                    EntityItem entityitem = new EntityItem(world, (float)i + f1, (float)j + f2, (float)k + f3, new ItemStack(grewFrom,1,0));
                    entityitem.delayBeforeCanPickup = 10;
                    world.entityJoinedWorld(entityitem);
                }
            }

        }
    }
    
    public String getTextureFile()
    {
    	return "/xie/img/terrain/xie_terrain.png";
    }

    public int idDropped(int i, Random random)
    {
    	return -1;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }
}
