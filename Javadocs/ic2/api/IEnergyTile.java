package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.0
 *
 * @section DESCRIPTION
 *
 * The IEnergyTile interface is only for _internal_ usage by the EnergyNet code to group all
 * objects which can be part of the EnergyNet
 */

public interface IEnergyTile {
	/**
	 * determine if this has been added to the EnergyNet
	 */
	boolean isAddedToEnergyNet();
}

