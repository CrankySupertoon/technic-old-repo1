package net.minecraft.src;

public class XieContainerCampfire extends Container
{

    public XieContainerCampfire(IInventory iinventory, XieTileEntityCampfire campfireEntity)
    {
        xCookTime = 0;
        xBurnTime = 0;
        xCurrentBurnTime = 0;
        campfire = campfireEntity;
        addSlot(new Slot(campfireEntity, 0, 56, 17));
        addSlot(new Slot(campfireEntity, 1, 56, 53));
        addSlot(new Slot(campfireEntity, 2, 116, 35));
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new Slot(iinventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        for(int j = 0; j < 9; j++)
        {
            addSlot(new Slot(iinventory, j, 8 + j * 18, 142));
        }

    }

    public void updateCraftingResults()
    {
        super.updateCraftingResults();
        for(int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);
            if(xCookTime != campfire.cookBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, campfire.cookBurnTime);
            }
            if(xBurnTime != campfire.burnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 1, campfire.burnTime);
            }
            if(xCurrentBurnTime != campfire.currentBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 2, campfire.currentBurnTime);
            }
        }

        xCookTime = campfire.cookBurnTime;
        xBurnTime = campfire.burnTime;
        xCurrentBurnTime = campfire.currentBurnTime;
    }

    public void setVar(int i, int j)
    {
        if(i == 0)
        {
            campfire.cookBurnTime = j;
        }
        if(i == 1)
        {
            campfire.burnTime = j;
        }
        if(i == 2)
        {
            campfire.currentBurnTime = j;
        }
    }
    
    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
    	return campfire.canInteractWith(entityplayer);
	}
    
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
    	return true;
    }

    private XieTileEntityCampfire campfire;
    private int xCookTime;
    private int xBurnTime;
    private int xCurrentBurnTime;

}
