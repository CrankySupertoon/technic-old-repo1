package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.forge.ITextureProvider;

public class XieBlockBush extends XieBlockCrop implements ITextureProvider {

	public XieBlockBush(int i, int tex[], int gc)
    {
        super(i, tex, gc);

        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setHardness(0.1F);
    }

    public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
    {
    	if(!world.singleplayerWorld)
        {
    		if (l<texArray.length-1) {
	            for(int i1 = 0; i1 < 3; i1++)
	            {
	            	if(world.rand.nextInt(10) <= l)
	                {
	                    produce(world, grewFrom, i, j, k);
	                }
	            }
	        } else {
	        	produce(world, drop, i, j, k);
	        	world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, texArray.length-3);
	        }
        }
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }

    public int idDropped(int i, Random random)
    {
    	if (i>=texArray.length-1) return drop;
    	else return -1;
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }
    
    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f)
    {
    	// let the onblockdestroyed handle dropping stuff for bushes
    }
}
