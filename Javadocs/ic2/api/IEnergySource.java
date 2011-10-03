package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.1
 *
 * @section DESCRIPTION
 *
 * The IEnergySource interface has to be implemented by all objects (TEs) which create new energy
 * pulses, usually from a generator mechanism or an internal energy storage buffer.
 */

public interface IEnergySource extends IEnergyEmitter {
	/**
	 * max. energy output pulse to optimize HV calculations
	 */
	int getMaxEnergyOutput();
}

