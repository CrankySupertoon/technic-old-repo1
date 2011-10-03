package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.0
 *
 * @section DESCRIPTION
 *
 * The IEnergyAcceptor interface is only for _internal_ usage by the EnergyNet code to group
 * objects which can accept energy from an adjacent block (IEnergyConductor and IEnergySink).
 */

import net.minecraft.src.TileEntity;

public interface IEnergyAcceptor extends IEnergyTile {
	/**
	 * determine if this can accept current from an adjacent block emitter from the direction
	 * specified
	 */
	boolean acceptsEnergyFrom(TileEntity emitter, Direction direction);
}

