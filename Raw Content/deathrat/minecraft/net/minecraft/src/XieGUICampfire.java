package net.minecraft.src;
// Copy-pasta from GuiFurnace.java, which was
//		Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// 		Jad home page: http://www.kpdus.com/jad.html
// 		Decompiler options: packimports(3) braces deadcode 
// Edited by Xie 14/1/11

import org.lwjgl.opengl.GL11;

public class XieGUICampfire extends GuiContainer
{

    public XieGUICampfire(InventoryPlayer inventoryplayer, XieTileEntityCampfire entity)
    {
        super(new XieContainerCampfire(inventoryplayer, entity));
        fire = entity;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        if (fire.voracity>4){
        	fontRenderer.drawString("Bonfire", 60, 6, 0x404040);
        } else fontRenderer.drawString("Campfire", 60, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/furnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        if(fire.isBurning())
        {
            int l = fire.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(j + 56, (k + 36 + 12) - l, 176, 12 - l, 14, l + 2);
        }
        int i1 = fire.getCookProgressScaled(24);
        drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
    }

    private XieTileEntityCampfire fire;
}
