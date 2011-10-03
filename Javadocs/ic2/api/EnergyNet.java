package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.0
 *
 * @section DESCRIPTION
 *
 * The EnergyNet api class provides access to the EnergyNet core class without requiring to include IC2 into the build.
 */

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public final class EnergyNet {
	public static EnergyNet getForWorld(World world) {
		try {
			return new EnergyNet(Class.forName("ic2.common.EnergyNet").getMethod("getForWorld", World.class).invoke(null, world));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private EnergyNet(Object energyNetInstance) {
		this.energyNetInstance = energyNetInstance;
	}
	
	/**
	 * add a tile entity to the energy network
	 *
	 * @note the TE has to be already valid and initialized
	 */
	public void addTileEntity(TileEntity addedTileEntity) {
		try {
			Class.forName("ic2.common.EnergyNet").getMethod("addTileEntity", TileEntity.class).invoke(energyNetInstance, addedTileEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * remove a tile entity from the energy network
	 *
	 * @note the TE has to be still valid
	 */
	public void removeTileEntity(TileEntity removedTileEntity) {
		try {
			Class.forName("ic2.common.EnergyNet").getMethod("removeTileEntity", TileEntity.class).invoke(energyNetInstance, removedTileEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * emit energy from an energy source to the energy network
	 */
	public int emitEnergyFrom(IEnergySource energySource, int amount) {
		try {
			return ((Integer) Class.forName("ic2.common.EnergyNet").getMethod("emitEnergyFrom", IEnergySource.class, Integer.TYPE).invoke(energyNetInstance, energySource, amount)).intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * determine how much energy has been conducted by a single conductor
	 *
	 * @note call this twice with x ticks delay to get the avg. conducted power p = (call2 - call1) / x EU/tick
	 */
	public long getTotalEnergyConducted(TileEntity tileEntity) {
		try {
			return ((Long) Class.forName("ic2.common.EnergyNet").getMethod("getTotalEnergyConducted", TileEntity.class).invoke(energyNetInstance, tileEntity)).longValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	Object energyNetInstance;
}

