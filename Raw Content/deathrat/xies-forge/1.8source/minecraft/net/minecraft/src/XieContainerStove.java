package net.minecraft.src;

public class XieContainerStove extends Container
{

    public XieContainerStove(IInventory iinventory, XieTileEntityStove entity)
    {
        xCookTime = 0;
        xBurnTime = 0;
        xCurrentItemBurnTime = 0;
        stove = entity;
        
        // create crafting slot(s)
        addSlot(new Slot(entity, 0, 80, 56)); // fuel slot
        //addSlot(new XieSlotPot(entity, 1, 80, 20)); // cooking pot slot
        //addSlot(new Slot(campfireEntity, 2, 116, 35));
        
        // create inventory slots
        for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new Slot(iinventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }

        }

        // create quick inventory slots
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
            if(xCookTime != stove.cookBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, stove.cookBurnTime);
            }
            if(xBurnTime != stove.burnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 1, stove.burnTime);
            }
            if(xCurrentItemBurnTime != stove.currentBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 2, stove.currentBurnTime);
            }
        }

        xCookTime = stove.cookBurnTime;
        xBurnTime = stove.burnTime;
        xCurrentItemBurnTime = stove.currentBurnTime;
    }

    public void setVar(int i, int j)
    {
        if(i == 0)
        {
            stove.cookBurnTime = j;
        }
        if(i == 1)
        {
            stove.burnTime = j;
        }
        if(i == 2)
        {
            stove.currentBurnTime = j;
        }
    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer)
    {
        return stove.canInteractWith(entityplayer);
    }
    
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
    	return true;
    }


    private XieTileEntityStove stove;
    private int xCookTime;
    private int xBurnTime;
    private int xCurrentItemBurnTime;
}
