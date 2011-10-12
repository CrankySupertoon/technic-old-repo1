package net.minecraft.src;



public class XieTileEntityCampfire extends TileEntity
    implements IInventory
{

	private int startBurnTime;
	
	public XieTileEntityCampfire() {
		this(3);
	}
	
    public XieTileEntityCampfire(int v)
    {
        itemStacks = new ItemStack[3];
        cookBurnTime = 0;
        voracity = v;
        startBurnTime=(v>3)?4800:400; // 4 logs worth / 4 sticks worth
        burnTime = startBurnTime;
        currentBurnTime = startBurnTime;
    }

    public int getSizeInventory()
    {
        return itemStacks.length;
    }
    
    public void openChest()
    {
    }
    
    public void closeChest()
    {
    }

    public ItemStack getStackInSlot(int i)
    {
        return itemStacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(itemStacks[i] != null)
        {
            if(itemStacks[i].stackSize <= j)
            {
                ItemStack itemstack = itemStacks[i];
                itemStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = itemStacks[i].splitStack(j);
            if(itemStacks[i].stackSize == 0)
            {
                itemStacks[i] = null;
            }
            return itemstack1;
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        itemStacks[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Campfire";
    }
    
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        itemStacks = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if(byte0 >= 0 && byte0 < itemStacks.length)
            {
            	//itemStacks[byte0] = new ItemStack(nbttagcompound1);
                itemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        burnTime = nbttagcompound.getShort("BurnTime");
        cookBurnTime = nbttagcompound.getShort("CookTime");
        voracity = nbttagcompound.getShort("Voracity");
        startBurnTime = (voracity>3)?4800:400;
        currentBurnTime = getItemBurnTime(itemStacks[1]);
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)burnTime);
        nbttagcompound.setShort("CookTime", (short)cookBurnTime);
        nbttagcompound.setShort("Voracity", (short)voracity);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < itemStacks.length; i++)
        {
            if(itemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                itemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getCookProgressScaled(int i)
    {
        return (cookBurnTime * i) / 200;
    }

    public int getBurnTimeRemainingScaled(int i)
    {
        if(currentBurnTime == 0)
        {
            currentBurnTime = startBurnTime;
        }
        return (burnTime * i) / currentBurnTime;
    }

    public boolean isBurning()
    {
        return true;//return burnTime > 0;
    }

    public void updateEntity()
    {
    	boolean invChange = false;
        
        if(burnTime > 0)
        {
            burnTime--;
        }
        
        if(worldObj.singleplayerWorld)
        {
            // check for fresh fuel
        	if(burnTime == 0) // && canCook()) // check for more fuel regardless of cooking, campfires don't go out
            {
                currentBurnTime = burnTime = getItemBurnTime(itemStacks[1]);
                if(burnTime > 0)
                {
                    invChange = true;
                    if(itemStacks[1] != null)
                    {
                        itemStacks[1].stackSize--;
                        if(itemStacks[1].stackSize == 0)
                        {
                            itemStacks[1] = null;
                        }
                    }
                } else {
                	// failed to find new fuel, campfire goes out!
                	// eject inventory
                	ejectInventory();
                	// destroy block (which in turn will destroy tile entity)
                	worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
                }
            }
            
            if(isBurning() && canCook())
            {
                cookBurnTime++;
                if(cookBurnTime >= 200)
                {
                    cookBurnTime = 0;
                    cookItem();
                    invChange = true;
                }
            } else
            {
                cookBurnTime = 0;
            }
        }
        if(invChange)
        {
            onInventoryChanged();
        }
    }

    private boolean canCook()
    {
        if(itemStacks[0] == null)
        {
            return false;
        }
        int i = getCookingResult(itemStacks[0].getItem().shiftedIndex);
        
        if(i < 0) return false;

        if(	itemStacks[2] == null) return true; 

        if (itemStacks[2].itemID != i) return false;
        
        if (itemStacks[2].stackSize < getInventoryStackLimit() && itemStacks[2].stackSize < itemStacks[2].getMaxStackSize())
        	return true;
        
        return itemStacks[2].stackSize < Item.itemsList[i].getItemStackLimit();
    }

    public void cookItem()
    {
        if(!canCook())
        {
            return;
        }
       
        int i = getCookingResult(itemStacks[0].getItem().shiftedIndex);
        // add cooking result to product
        if(itemStacks[2] == null)
        {
            itemStacks[2] = new ItemStack(i, 1, 0);
        } else
        if(itemStacks[2].itemID == i)
        {
            itemStacks[2].stackSize++;
        }
        
        // remove a raw ingredient
        /*if (itemStacks[0].itemID==XieMod.pancakeBatter.shiftedIndex) {
        	itemStacks[0].damageItem(16,null);
        	if (itemStacks[0].getItemDamage()>=itemStacks[0].getMaxDamage()) 
        		itemStacks[0]=new ItemStack(Item.bucketEmpty);
        } else { */
        	itemStacks[0].stackSize--;
        	if(itemStacks[0].stackSize <= 0)
        	{
        		itemStacks[0] = null;
        	}
        //}
    }

    private int getCookingResult(int i)
    {
    	ItemStack whenSmelted = FurnaceRecipes.smelting().getSmeltingResult(i);
		if (whenSmelted!=null) {
			Item smelted = whenSmelted.getItem();
			if (XieMod.isFood(smelted)) {
				return smelted.shiftedIndex;
			}
		}
        
        return -1;
    }

    private int getItemBurnTime(ItemStack itemstack)
    {
        if(itemstack == null)
        	return 0;
        
        int i = itemstack.getItem().shiftedIndex;
        
        if(i==Block.wood.blockID) return 1200;
        
        if(i < 256 && Block.blocksList[i].blockMaterial == Material.wood)
        	return 300;
        
        if(i == Item.stick.shiftedIndex)
        	return 100;
        
        return 0;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

	public void ejectInventory() {
		for (int n = 0; n<3; n++) if (itemStacks[n]!=null) {
			EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord, zCoord,itemStacks[n]);
			entityitem.delayBeforeCanPickup = 10;
			worldObj.entityJoinedWorld(entityitem);
		}
	}
	
    private ItemStack itemStacks[];
    public int burnTime;
    public int currentBurnTime;
    public int cookBurnTime;
    
    public int voracity;


}
