package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.1
 *
 * @section DESCRIPTION
 *
 * The IEnergyConductor interface has to be implemented by all objects (TEs) which conduct energy
 * pulses without buffering them, i.e. mostly cable-type blocks.
 */

public interface IEnergyConductor extends IEnergyAcceptor, IEnergyEmitter {
	/**
	 * conduction loss per block in eu
	 */
	double getConductionLoss();
	
	/**
	 * amount of energy which will be held back by the insulation
	 */
	int getInsulationEnergyAbsorption();
	
	/**
	 * amount of energy required to permanently remove the insulation
	 *
	 * @note ensure getInsulationBreakdownEnergy() > getInsulationEnergyAbsorption() + 64
	 */
	int getInsulationBreakdownEnergy();
	
	/**
	 * amount of energy required to permanently remove the conductor
	 */
	int getConductorBreakdownEnergy();
	
	/**
	 * remove the conductor's insulation if the breakdown energy was exceeded
	 */
	void removeInsulation();
	
	/**
	 * remove the whole cable if the breakdown energy was exceeded
	 */
	void removeConductor();
}

