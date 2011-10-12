package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public class XieItemAxe extends ItemAxe {

	public XieItemAxe(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, enumtoolmaterial);
	}
	
	public static List<Block> effective = new LinkedList<Block>();
	
	public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        for(int i = 0; i < effective.size(); i++)
        {
            if(effective.get(i) == block)
            {
                return toolMaterial.getEfficiencyOnProperMaterial();
            }
        }

        return super.getStrVsBlock(itemstack, block);
    }

}
