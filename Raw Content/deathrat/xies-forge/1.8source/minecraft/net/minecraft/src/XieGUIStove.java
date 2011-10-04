package net.minecraft.src;
// Copy-pasta from GuiFurnace.java, which was
//		Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// 		Jad home page: http://www.kpdus.com/jad.html
// 		Decompiler options: packimports(3) braces deadcode 
// Edited by Xie 14/1/11

import org.lwjgl.opengl.GL11;

public class XieGUIStove extends GuiContainer
{

    public XieGUIStove(InventoryPlayer inventoryplayer, XieTileEntityStove entity)
    {
        super(new XieContainerStove(inventoryplayer, entity));
        stove = entity;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Stove", 75, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    // FYI: drawTexturedModalRect(int screenX, int screenY, int imageX, int imageY, int sizeX, int sizeY)
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/Xie/img/gui/xiestove.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int minX = (width - xSize) / 2;
        int minY = (height - ySize) / 2;
        drawTexturedModalRect(minX, minY, 0, 0, xSize, ySize);
        if(stove.isBurning())
        {
            int l = stove.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(minX + 81, (minY + 40 + 12) - l, 176, 12 - l, 14, l + 2);
        }
        
        // stove code for slightly more complex progress indicator :P
        if (!stove.isState(stove.EMPTY)) {
    		// draw boiling, heating or cooking animated state indicator
    		int tick = stove.cookBurnTime % 4;
    		int texX = baseX + tick*18;
    		int texY = baseY + stove.getState() * 18;
        	drawTexturedModalRect(minX + potX, minY + potY, texX, texY, 18, 18);
        }
    }
    
    // overide of super class function to allow for unique pot slot interaction 
    // FYI: mouseClicked(int x, int y, int mouseButton)
    protected void mouseClicked(int i, int j, int mouseButton)
    {
        int minX = (width - xSize) / 2;
        int minY = (height - ySize) / 2;
    	//if (Xie.DEBUG) System.out.println("XIE: stove GUI clicked!! ["+i+","+j+","+k+"]");
    	
    	// first check for pot slot interaction
    	if (i>=(minX+potX) && i<=(minX+potX+potSize) && j>=(minY+potY) && j<=(minY+potY+potSize)) {    		
    		// check for wooden bowl/bucket or ingredient, and add/remove as necessary
    		ItemStack heldStack = mc.thePlayer.inventory.getItemStack();
    		mc.thePlayer.inventory.setItemStack(stove.potClicked(heldStack));
    	} else
     	// continue with normal mouse click behaviour 
        super.mouseClicked(i,j,mouseButton);
    	/*if(mouseButton == 0 || mouseButton == 1)
        {
            Slot slot = getSlotAtPosition(i, j);
            boolean clickOutOfBounds = i < minX || j < minY || i >= minX + xSize || j >= minY + ySize;
            int itemCode = -1;
            if(slot != null)  itemCode = slot.slotNumber;
            if(clickOutOfBounds) itemCode = -999; 
            
            if(itemCode != -1) 
            	mc.playerController.windowClick(inventorySlots.windowId, itemCode, mouseButton, false, mc.thePlayer);     
        }*/
    }
    
    // function copy-pasta from super class. Could change visibility, but this prevents altering native class
    private Slot getSlotAtPosition(int i, int j)
    {
       // for(int k = 0; k < inventorySlots.slots.size(); k++)
    	for(int k = 0; k < inventorySlots.inventorySlots.size(); k++)
        {
          //  Slot slot = (Slot)inventorySlots.slots.get(k);
    		Slot slot = (Slot)inventorySlots.inventorySlots.get(k);
            if(getIsMouseOverSlot(slot, i, j))
            {
                return slot;
            }
        }

        return null;
    }

    // function copy-pasta from super class. Could change visibility, but this prevents altering native class
    private boolean getIsMouseOverSlot(Slot slot, int i, int j)
    {
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        i -= k;
        j -= l;
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + 16 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + 16 + 1;
    }

    private XieTileEntityStove stove;
    private static final int potX = 79;
    private static final int potY = 19;
    private static final int potSize = 18;
    private static final int baseX = 176;
    private static final int baseY = 14;
    
}