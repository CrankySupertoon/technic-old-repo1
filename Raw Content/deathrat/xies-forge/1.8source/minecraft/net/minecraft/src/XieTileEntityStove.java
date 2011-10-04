package net.minecraft.src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;




public class XieTileEntityStove extends TileEntity
    implements IInventory
{	
    public XieTileEntityStove()
    {
        itemStacks = new ItemStack[1];
        burnTime = 0;
        currentBurnTime = 0;
        ingredList = new LinkedList<Item>();
        resetPotState();
    }

     public void setfuel(ItemStack itemstack)
    {
        itemStacks[0] = itemstack;
    }

     public void openChest()
     {
     }
     
     public void closeChest()
     {
     }
     
    public String getInvName()
    {
        return "Stove";
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
//            	itemStacks[byte0] = new ItemStack(nbttagcompound1);
            	itemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        burnTime = nbttagcompound.getShort("BurnTime");
        cookBurnTime = nbttagcompound.getShort("CookTime");
        currentBurnTime = getItemBurnTime(itemStacks[0]);
        
        potState = nbttagcompound.getShort("PotState");
        ingredientCount = nbttagcompound.getShort("Count");
        lastItem = nbttagcompound.getShort("LastItem");
        
        int l = nbttagcompound.getShort("IngredListLength");
        for (int i = 0; i<l; i++) {
        	ingredList.add(new Item(nbttagcompound.getShort("Ingred"+i)));
        }
        cookingBase = nbttagcompound.getShort("CookingBase");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("BurnTime", (short)burnTime);
        nbttagcompound.setShort("CookTime", (short)cookBurnTime);
        
        NBTTagList nbtitemlist = new NBTTagList();
        for(int i = 0; i < itemStacks.length; i++)
        {
            if(itemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                itemStacks[i].writeToNBT(nbttagcompound1);
                nbtitemlist.setTag(nbttagcompound1);
            }
        }
            
        nbttagcompound.setTag("Items", nbtitemlist);
        nbttagcompound.setShort("PotState",(short)potState);
        nbttagcompound.setShort("Count",(short)ingredientCount);
        nbttagcompound.setShort("LastItem",(short)lastItem);
        
        nbttagcompound.setShort("IngredListLength",(short) ingredList.size());
        Iterator<Item> it = ingredList.iterator();
        int i = 0;
        while (it.hasNext())
        {
        	nbttagcompound.setShort("Ingred"+i++, (short) it.next().shiftedIndex);
        }
        nbttagcompound.setShort("CookingBase",(short)cookingBase);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    // Deprecated?
    public int getCookProgressScaled(int i)
    {
        return (cookBurnTime * i) / maxCookTime;
    }

    public int getBurnTimeRemainingScaled(int i)
    {
        if(currentBurnTime == 0)
        {
            currentBurnTime = 200;
        }
        return (burnTime * i) / currentBurnTime;
    }

    public boolean isBurning()
    {
        return burnTime > 0;
    }

    public void updateEntity()
    {
        boolean flag = burnTime > 0;
    	
    	if(burnTime > 0)
        {
            burnTime--;
        }
        
        if(!worldObj.multiplayerWorld)
        {
            // Refuel[0] if needed
        	if(burnTime == 0 && potState != EMPTY)
            {
                currentBurnTime = burnTime = getItemBurnTime(itemStacks[0]);
                if(burnTime > 0)
                {
                    if(itemStacks[0] != null)
                    {
                        itemStacks[0].stackSize--;
                        onInventoryChanged();
                        if(itemStacks[0].stackSize == 0)
                        {
                            itemStacks[0] = null;
                        }
                    }
                }
            }
        	
        	// 
            if(isBurning() && potState!=EMPTY)
            {
                cookBurnTime++;
                
                if (cookBurnTime>=maxCookTime) {
                	cookBurnTime=0;	
                	switch (potState) {
                	case BOILING: setState(BOILED); break;
                	case HEATING: setState(HOT); break;
                	case COOKING:
                		if (cookingBase==NONE) lastItem = getCookingResult(lastItem);
                		setState(COOKED); break;
                	case MILK_HEATING: setState(MILK_HOT); break;
                	}
                }
            } else {
                cookBurnTime = 0;
            }
            
            if(flag != (burnTime > 0))
            {
                XieBlockStove.updateBlockState(burnTime > 0, worldObj, xCoord, yCoord, zCoord);
            }
        }
    }
    
    private int getCookingResult(int i)
    {
    	if(i == Block.plantYellow.blockID) return XieMod.cheese.shiftedIndex;
        if(i == XieMod.pancakeBatter.shiftedIndex) return XieMod.pancake.shiftedIndex;
        
        ItemStack smelted = FurnaceRecipes.smelting().getSmeltingResult(i);
        
        if (smelted!=null) return smelted.itemID;
        
        return XieMod.debugFood.shiftedIndex;
    }

	private boolean canBoil (Item item) {
    	return (XieMod.isFood(item) || XieMod.soupList.contains(new ItemStack(item,1,0).getItem()));
    }
    
    private boolean canFry (Item item) {
    	return XieMod.fryList.contains(new ItemStack(item,1,0).getItem());
    }
    
    private boolean isFlavouring (Item item) {
    	return XieMod.flavorList.contains(item);
    }
    
    private boolean isFoodIngredient (Item item) {
    	return canFry(item) || canBoil(item);
    }
    
    public ItemStack potClicked(ItemStack item) {
    	if (item==null) {
    		if (cookingBase==NONE && (potState==COOKING || potState==COOKED)) {
	    		ItemStack product = new ItemStack(lastItem,ingredientCount,0);
	    		if (lastItem==XieMod.pancakeBatter.shiftedIndex) return null;
	    		resetPotState();
	    		return product;
	    	} else return null;
    	} else {
	    	// check if attempt made to empty pot with bucket or bowl
	    	if (item.itemID==Item.bowlEmpty.shiftedIndex || item.itemID==Item.bucketEmpty.shiftedIndex)
	    		return getPotContents(item);
	    	
	    	// otherwise try to add ingredient to pot
	    	return addToPot(item);
    	}
    }
    
    private ItemStack addToPot (ItemStack itemStack) { 
    	int itemID = itemStack.itemID;
    	Item item = itemStack.getItem();
    	if (potState==EMPTY) {
    		if (itemID==Item.bucketWater.shiftedIndex) { 
    			setState(BOILING);
    			setBase(WATER);
    			// took the water from the bucket, return empty bucket
    			return new ItemStack(Item.bucketEmpty);
    		} else if (itemID==XieMod.oil.shiftedIndex && itemStack.stackSize==1) {
    			setState(HEATING);
    			setBase(OIL);
    			// took the oil from the bowl, return empty bowl
    			return new ItemStack(Item.bowlEmpty);
    		}  else if (itemID==Item.bucketMilk.shiftedIndex) {
    			setState(MILK_HEATING);
    			setBase(MILK);
    			// took the milk from the bucket, return empty bucket
    			return new ItemStack(Item.bucketEmpty);
    		} else if (itemID==XieMod.pancakeBatter.shiftedIndex) {
    			ingredientCount++;
    			lastItem=itemID;
    			setState(COOKING);
    			setBase(NONE);
    			if (itemStack.getItemDamage()>=56) return new ItemStack(Item.bucketEmpty);
    			else return new ItemStack(XieMod.pancakeBatter.shiftedIndex,1,itemStack.getItemDamage()+8);
    		} else {
    			// check to see if it's a "furnacable" food
    			ItemStack whenSmelted = FurnaceRecipes.smelting().getSmeltingResult(itemID);
    			if (whenSmelted!=null) {
    				Item smelted = whenSmelted.getItem();
    				if (XieMod.isFood(smelted)) {
    					ingredientCount=itemStack.stackSize;
    					lastItem=itemID;
    					setState(COOKING);
    					setBase(NONE);
    					return null;
    				}	
    			}
    		}
    	} else if ((potState==COOKED || potState==BOILED || potState==HOT) && (cookingBase==OIL || cookingBase==WATER)){
    		if (canBoil(item) && ingredientCount<2) {
    			ingredientCount++;
    			ingredList.add(item);
    			lastItem=itemID;
    			setState(COOKING);
    			// took the item into the pot, return decremented stack
    			if (itemStack.stackSize<=1) return null;
    			else return new ItemStack(itemStack.itemID, itemStack.stackSize-1,0);
    		} else if (isFlavouring(item) && ingredientCount>0 && !ingredList.contains(item)) {
				ingredList.add(item);
				setState(COOKING);
				// took the item into the pot, return decremented stack
    			if (itemStack.stackSize<=1) return null;
    			else return new ItemStack(itemStack.itemID, itemStack.stackSize-1,0);
    		} 
    	}else if (potState==MILK_HOT) {
			if (itemID==Block.plantYellow.blockID && XieMod.Cooking.Enable.cheese) {
    			// makes cheese!
    			ingredientCount=1;
    			lastItem=itemID;
    			setState(COOKING);
    			setBase(NONE);
    			// took the item into the pot, return decremented stack
    			if (itemStack.stackSize<=1) return null;
    			else return new ItemStack(itemStack.itemID, itemStack.stackSize-1,0);
			}
    	}
    	
    	// return unaffected item, pot won't take it
    	return itemStack;
    }
  
	private ItemStack getPotContents(ItemStack implement)
    {
		// first check that the item isn't in a stack
		if (implement.stackSize>1) return implement;
    	 
		if (implement.itemID == Item.bucketEmpty.shiftedIndex) {
    		 switch (potState) {
    	 		case BOILING: 
    	 		case BOILED:
    	 			resetPotState();
    	 			return new ItemStack(Item.bucketWater);
    	 		case MILK_HEATING:
    	 		case MILK_HOT:
    	 			resetPotState();
    	 			return new ItemStack(Item.bucketMilk);
    		 }
    	 } else if (implement.itemID == Item.bowlEmpty.shiftedIndex) {
    		 if (potState==HEATING || potState==HOT) {
    			 resetPotState();
    			 return new ItemStack(XieMod.oil);
    		 }
    		 
    		 switch (potState) {
 	 			case COOKING:
 	 			case COOKED:
 	 				Item nomable = interpretBowlRecipe();
 	 				if (nomable.shiftedIndex!=Item.bowlEmpty.shiftedIndex) {
 	 					resetPotState();
 	 				}
 	 				return new ItemStack(nomable);
    		 }
    	 }
    	 
		return implement;
    }
	
	private Item interpretBowlRecipe() {
		Item result = Item.bowlEmpty;
		
		Iterator<Item> it = ingredList.iterator();
		List<Item> done = new LinkedList<Item>();
		int variety = 0;
		int flavour = 0;
		
		while (it.hasNext()) {
			Item i = it.next();
			if (isFoodIngredient(i) && !done.contains(i)) variety++;
			if (isFlavouring(i)) flavour++;
			done.add(i);
		}
		
		if (XieMod.DEBUG) System.out.println("Flavour="+flavour+" Variety="+variety);
		
		if (cookingBase==WATER) {
			if (flavour==0 && variety<2) result=XieMod.failSoup;
			if ((flavour>0 && variety>0) || variety>1) result=XieMod.soup;
			if (flavour>0 && variety>1) result=XieMod.stew;
		}
		
		if (cookingBase==OIL) {
			if (flavour==0 && variety<2) result=XieMod.failFry;
			if ((flavour>0 && variety>0) || variety>1) result=XieMod.fry;
			if (flavour>0 && variety>1) result=XieMod.stirfry;
		}
		
		return result;
	}
	
	private void resetPotState () {
			potState=EMPTY;
			cookBurnTime=0;
			lastItem = -1;
			cookingBase = -1;
			ingredientCount=0;
			ingredList = new LinkedList();
	}

    private int getItemBurnTime(ItemStack itemstack)
    {
        if(itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().shiftedIndex;
        if(i < 256 && Block.blocksList[i].blockMaterial == Material.wood)
        {
            return 300;
        }
        if(i == Item.stick.shiftedIndex)
        {
            return 100;
        }
        if(i == Item.coal.shiftedIndex)
        {
            return 1600;
        }
        return i != Item.bucketLava.shiftedIndex ? 0 : 20000;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int getSizeInventory()
    {
        return itemStacks.length;
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
        } else return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        itemStacks[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }
    
    public void setState(int newState) {
    	if (XieMod.DEBUG) System.out.println("XIE: Pot state change: "+stateStr(potState)+"->"+stateStr(newState));
    	
    	potState=newState;
    	cookBurnTime=0;
    	
    	if (potState==EMPTY) resetPotState();
    }
    
    public void setBase(int base) {
    	cookingBase=base;
    }
    
    public int getState() {
    	return potState;
    }
    
    public boolean isState(int checkState) {
    	return potState==checkState;
    }
    
    public String stateStr(int state) {
    	switch (state) {
    	case -1: return "EMPTY";
    	case 0: return "BOILING";
    	case 1: return "BOILED";
    	case 2: return "HEATING";
    	case 3: return "HOT";
    	case 4: return "MILK_HEATING";
    	case 5: return "MILK_HOT";
    	case 6: return "COOKING";
    	case 7: return "COOKED";
    	default: return ("unrecognised: "+state);
    	}

    }
    
	private ItemStack[] itemStacks;
    public int burnTime;
    public int currentBurnTime;
    public int cookBurnTime;
    private int lastItem;
    private int ingredientCount;
    private List<Item> ingredList;
    
    private int potState;
    public final int EMPTY = -1;
    public final int BOILING = 0;
    public final int BOILED = 1;
    public final int HEATING = 2;
    public final int HOT = 3;
    public final int MILK_HEATING = 4;
    public final int MILK_HOT = 5;
    public final int COOKING = 6;
    public final int COOKED = 7;
    
    private int cookingBase;
    public final int NONE = 0;
    public final int WATER = 1;
    public final int OIL = 2;
    public final int MILK = 3;
    
    private final int maxCookTime=150;
}
