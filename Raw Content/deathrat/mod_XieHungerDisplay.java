package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class mod_XieHungerDisplay extends BaseMod {
	
	public mod_XieHungerDisplay() {
		// set update for each render frame
		ModLoader.SetInGameHook(this, true, false);
		ModLoader.SetInGUIHook(this, true, false);
	}
	
	// Caution, these are being called each frame, so be as computationally frugal as you can
	public boolean OnTickInGame(Minecraft game) {
		display(game);
		return true;
	}
	public void OnTickInGui(Minecraft game) {
		display(game);
	}

    public void display(Minecraft minecraft) {
    	if (mod_XieHunger.useBars) displayBars(minecraft);
    	else displayIcons(minecraft);
    }
    
    // drawTexturedModalRect(screenX, screenY, texX, texY, sizeX, sizeY);
    public void displayBars(Minecraft minecraft) {
    	ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
    	int displayX = (scaledresolution.getScaledWidth()/2)-91;
        int displayY = scaledresolution.getScaledHeight() - 41;
        GL11.glBindTexture(3553, minecraft.renderEngine.getTexture(mod_XieHunger.iconFile));
        
        // check for water bubbles, and if they're there, bump the bars up a bit
        if (minecraft.thePlayer.isInsideOfMaterial(Material.water)) {
        	displayY-=9;
        }
        
        // draw hunger, thirst and fatigue bars
        if (mod_XieHunger.hungerEnabled) {
        	int xOffset=0;
        	int yOffset=0;
        	if (!mod_XieHunger.thirstEnabled) yOffset+=3;
        	if (!mod_XieHunger.fatigueEnabled) yOffset+=3;
        	for (int i=0; i<mod_XieHunger.hunger; i++) {
        		minecraft.ingameGUI.drawTexturedModalRect(displayX+xOffset,displayY+yOffset, 8, 0, 4, 3);
        		xOffset+=4;
        	}
        }
        if (mod_XieHunger.thirstEnabled) {
        	int xOffset=0;
        	int yOffset=3;
        	if (!mod_XieHunger.fatigueEnabled) yOffset+=3;
        	for (int i=0; i<mod_XieHunger.thirst; i++) {
        		minecraft.ingameGUI.drawTexturedModalRect(displayX+xOffset,displayY+yOffset, 12, 0, 4, 3);
        		xOffset+=4;
        	}
        }
        if (mod_XieHunger.fatigueEnabled) {
        	int xOffset=0;
        	int yOffset=6;
        	int fat4 = mod_XieHunger.fatigueScaled/4;
        	for (int i=0; i<fat4; i++) {
        		minecraft.ingameGUI.drawTexturedModalRect(displayX+xOffset,displayY+yOffset, 16, 0, 4, 3);
        		xOffset+=4;
        	}
        	int rem = mod_XieHunger.fatigueScaled%4;
        	if (rem>0) {
        		minecraft.ingameGUI.drawTexturedModalRect(displayX+xOffset,displayY+yOffset, 16, 0, rem, 3);
        	}
        }

    	int skullX = (scaledresolution.getScaledWidth()/2)-8;
        int skullY = scaledresolution.getScaledHeight() - 40;
        if (mod_XieHunger.dying) {
        	if (mod_XieHunger.hungerEnabled || mod_XieHunger.thirstEnabled || mod_XieHunger.fatigueEnabled) {
            	minecraft.ingameGUI.drawTexturedModalRect(skullX,skullY,32,0,8,8);
        	}
        }
    }
    
    public void displayIcons(Minecraft minecraft) {
    	ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
    	int xieHungerIconX = (scaledresolution.getScaledWidth()/2)-8;
        int xieHungerIconY = scaledresolution.getScaledHeight() - 40;
        GL11.glBindTexture(3553, minecraft.renderEngine.getTexture(mod_XieHunger.iconFile));
        // draw hunger, thirst and fatigue icons
        if (mod_XieHunger.hungerEnabled)
        	minecraft.ingameGUI.drawTexturedModalRect(xieHungerIconX,xieHungerIconY+8,mod_XieHunger.hungerState*8,8,8,8); // screenX, screenY, texX, texY, sizeX, sizeY
        if (mod_XieHunger.thirstEnabled)
        	minecraft.ingameGUI.drawTexturedModalRect(xieHungerIconX+8,xieHungerIconY+8,mod_XieHunger.thirstState*8,16,8,8);
        if (mod_XieHunger.fatigueEnabled)
        	minecraft.ingameGUI.drawTexturedModalRect(xieHungerIconX+8,xieHungerIconY,mod_XieHunger.fatigueState*8,24,8,8);
        if (mod_XieHunger.dying) {
        	if (mod_XieHunger.hungerEnabled || mod_XieHunger.thirstEnabled || mod_XieHunger.fatigueEnabled) {
            	minecraft.ingameGUI.drawTexturedModalRect(xieHungerIconX,xieHungerIconY,32,0,8,8);
        	}
        }
    }
        
	public String Version() {
		return "XieHungerDisplay for XieHunger v1.7b";
	}
}