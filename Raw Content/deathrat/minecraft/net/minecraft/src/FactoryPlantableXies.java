package net.minecraft.src;

public class FactoryPlantableXies extends FactoryPlantableStandard implements FactoryPlantable
{
        public FactoryPlantableXies(int i, int j)
        {
        		super(i, j);
        }
        
        @Override
        public boolean canBePlantedHere(int x, int y, int z, ItemStack stack, World world)
        {
    			int ourId = world.getBlockId(x, y, z);
                int groundId = world.getBlockId(x, y - 1, z);
                return (groundId == Block.dirt.blockID || groundId == Block.grass.blockID || groundId == Block.tilledField.blockID) && ourId == 0;
        }

        @Override
        public void prePlant(int x, int y, int z, ItemStack stack, World world)
        {
                int groundId = world.getBlockId(x, y - 1, z);
                if(groundId != Block.tilledField.blockID)
                {
                        world.setBlockWithNotify(x, y - 1, z, Block.tilledField.blockID);
                }
                
        }
}