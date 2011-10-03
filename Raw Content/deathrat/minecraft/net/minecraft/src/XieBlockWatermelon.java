package net.minecraft.src;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.ITextureProvider;


public class XieBlockWatermelon extends Block implements ITextureProvider
{

	public XieBlockWatermelon(int i, int j, boolean flag)
    {
        super(i, Material.pumpkin);
        blockIndexInTexture = j;
        setTickOnLoad(true); // can this be false for watermelons?
        this.setStepSound(Block.soundWoodFootstep);
        this.setHardness(1.0F);
    }

     public int getBlockTextureFromSide(int i)
    {
        if (i == 0 || i == 1) return XieMod.watermelonTop;
        else return blockIndexInTexture;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j, k);
        return (l == 0 || Block.blocksList[l].blockMaterial.getIsLiquid()) && world.isBlockOpaqueCube(i, j - 1, k);
    }

    public int quantityDropped(Random random)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        ItemStack it = mc.thePlayer.getCurrentEquippedItem();
        if (it!=null)
        	if (it.getItem() instanceof ItemHoe) return 2+random.nextInt(2);
        
        return 1;
    }

    public int idDropped(int i, Random random)
    {
    	Minecraft mc = ModLoader.getMinecraftInstance();
        ItemStack it = mc.thePlayer.getCurrentEquippedItem();
        if (it!=null)
        	if (it.getItem() instanceof ItemHoe) return XieMod.melonSeeds.shiftedIndex;
        
        return blockID;
    }
    
    public String getTextureFile()
    {
    	return "/Xie/img/terrain/xie_terrain.png";
    }
    
}
