package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.1
 *
 * @section DESCRIPTION
 *
 * The IEnergySink interface has to be implemented by all objects (TEs) which consume energy pulses
 * from the EnergyNet like machines or the input-part of an energy storage buffer.
 */

public interface IEnergySink extends IEnergyAcceptor {
	/**
	 * determine if the sink requires energy (operation pending, input energy storage not full)
	 */
	boolean demandsEnergy();

	/**
	 * transfer energy to the sink
	 *
	 * @param directionFrom direction which the energy comes from
	 * @param amount energy to be transferred
	 * @return energy not consumed (left over)
	 */
	int injectEnergy(Direction directionFrom, int amount);
}

