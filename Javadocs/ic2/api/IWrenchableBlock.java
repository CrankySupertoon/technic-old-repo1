package net.minecraft.src.ic2.api;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public interface IWrenchableBlock {
	/**
	 * Override this.
	 * Will be called if the wrench is used to rightclick the block implementing this.
	 * Ensure the block doesn't intercept the wrench with onBlockActivated.
	 * @return true if the wrenching was applied and wrench should loose durability
	 */
	public boolean wrench(World world, int x, int y, int z, EntityPlayer entityplayer);
}

