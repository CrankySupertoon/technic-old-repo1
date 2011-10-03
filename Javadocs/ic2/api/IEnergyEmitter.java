package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.0
 *
 * @section DESCRIPTION
 *
 * The IEnergyEmitter interface is only for _internal_ usage by the EnergyNet code to group objects
 * which can emit energy to an adjacent block (IEnergyConductor and IEnergySource).
 */

import net.minecraft.src.TileEntity;

public interface IEnergyEmitter extends IEnergyTile {
	/**
	 * determine if this can emit current to an adjacent block receiver in the direction specified
	 */
	boolean emitsEnergyTo(TileEntity receiver, Direction direction);
}

