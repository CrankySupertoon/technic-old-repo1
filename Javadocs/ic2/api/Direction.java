package net.minecraft.src.ic2.api;

/**
 * @file
 * @author Player
 * @version 1.0
 *
 * @section DESCRIPTION
 *
 * The Direction enum represents the 6 possible directions along the axis of a block (cube)
 */

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public enum Direction {
	XN(0),
	XP(1),
	
	YN(2),
	YP(3),
	
	ZN(4),
	ZP(5);
	
	Direction(int dir) {
		this.dir = dir;
	}
	
	/*public CoordinateTuple ApplyToCoordinates(CoordinateTuple coordinates) {
		CoordinateTuple ret = new CoordinateTuple(coordinates);
		
		ret.coords[dir/2] += GetSign();
		
		return ret;
	}*/
	
	/**
	 * Get the TileEntity next to tileEntity in the direction this
	 *
	 * @return adjacent TileEntity or null if none exists
	 */
	public TileEntity applyToTileEntity(World world, TileEntity tileEntity) {
		int coords[] = { tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord };
		
		coords[dir/2] += getSign();
		
		if (world.blockExists(coords[0], coords[1], coords[2])) {
			return world.getBlockTileEntity(coords[0], coords[1], coords[2]);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the inverse direction (xn -> xp, xp -> xn, ...)
	 */
	public Direction getInverse() {
		int inverseDir = dir - getSign();
		
		for (Direction direction: Direction.values()) {
			if (direction.dir == inverseDir) return direction;
		}
		
		return this;
	}
	
	public int toSideValue() {
		return (dir + 4) % 6;
	}
	
	/**
	 * Determine direction sign (N or P)
	 *
	 * @return -1 if dir is negative, +1 if dir is positive
	 */
	private int getSign() {
		return (dir % 2) * 2 - 1;
	}
	
	private int dir;
}

