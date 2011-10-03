package net.minecraft.src;

public class FactoryPlantableHybridWheat extends FactoryPlantableStandard implements FactoryPlantable
{
        public FactoryPlantableHybridWheat()
        {
                super(XieMod.hybridSeeds.shiftedIndex, XieMod.hybridWheat.blockID);
        }
        
        @Override
        public boolean canBePlantedHere(int x, int y, int z, ItemStack stack, World world)
        {
                int groundId = world.getBlockId(x, y - 1, z);
                return groundId == Block.dirt.blockID || groundId == Block.grass.blockID || groundId == Block.tilledField.blockID;
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