package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class XieTreeManager {

	private ArrayList<XieTree> treeList;
	
	public XieTreeManager() {
		treeList = new ArrayList<XieTree>();
	}
	
	public void addTree(XieTree tree) {
		if ((tree.id+1)>treeList.size()) {
			for (int i=treeList.size(); i<(tree.id+1); i++)
				treeList.add(tree);
		}
		treeList.set(tree.id, tree);
	}
	
	public void addTree(int i, String n) {
		XieTree t = new XieTree(i,n);
		addTree(t);
	}
	
	public void addTree(int treeID, String treeName, Item treeDrop, int chance, int saplingID, int saplingChance, int growthC, int growthT) {
		XieTree t = new XieTree(treeID, treeName, treeDrop, chance, saplingID, saplingChance, growthC, growthT);
		addTree(t);
	}
	
	public XieTree get(int i) {
		return treeList.get(i);
	}
	
	// Untested
	public XieTree get(String s) {
		s.toLowerCase();
		Iterator<XieTree> it = treeList.iterator();
		if (it!=null) {
			while (it.hasNext()) {
				XieTree t = it.next();
				String n = t.name.toLowerCase();
				if (n.equals(s)) return t;
			}
		}
		
		return null; 
	}
	
	// TODO make this search through array for empty element
	public int getFreeIndex() {
		return treeList.size();
	}
	
	public boolean generate(String s, World world, Random random, int i, int j, int k) {
		XieTree t = get(s);
		if (t!=null) return generate(t.id, world, random, i, j, k);
		else return false;
	}
	
	public boolean generate(int treeID, World world, Random random, int i, int j, int k)
    {
        int l = random.nextInt(3) + 4;
        boolean flag = true;
        if(j < 1 || j + l + 1 > 128)
        {
            return false;
        }
        for(int i1 = j; i1 <= j + 1 + l; i1++)
        {
            byte byte0 = 1;
            if(i1 == j)
            {
                byte0 = 0;
            }
            if(i1 >= (j + 1 + l) - 2)
            {
                byte0 = 2;
            }
            for(int i2 = i - byte0; i2 <= i + byte0 && flag; i2++)
            {
                for(int l2 = k - byte0; l2 <= k + byte0 && flag; l2++)
                {
                    if(i1 >= 0 && i1 < 128)
                    {
                        int j3 = world.getBlockId(i2, i1, l2);
                        if(j3 != 0 && j3 != XieMod.leaves.blockID)
                        {
                            flag = false;
                        }
                    } else
                    {
                        flag = false;
                    }
                }

            }

        }

        if(!flag)
        {
            return false;
        }
        int j1 = world.getBlockId(i, j - 1, k);
        if(j1 != Block.grass.blockID && j1 != Block.dirt.blockID || j >= 128 - l - 1)
        {
            return false;
        }
        world.setBlock(i, j - 1, k, Block.dirt.blockID);
        for(int k1 = (j - 3) + l; k1 <= j + l; k1++)
        {
            int j2 = k1 - (j + l);
            int i3 = 1 - j2 / 2;
            for(int k3 = i - i3; k3 <= i + i3; k3++)
            {
                int l3 = k3 - i;
                for(int i4 = k - i3; i4 <= k + i3; i4++)
                {
                    int j4 = i4 - k;
                    if((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && !Block.opaqueCubeLookup[world.getBlockId(k3, k1, i4)])
                    {
                    	int meta = getMetaForTreeID(0, treeID);
                    	world.setBlockAndMetadata(k3, k1, i4, XieMod.leaves.blockID, meta);
                    }
                }

            }

        }

        for(int l1 = 0; l1 < l; l1++)
        {
            int k2 = world.getBlockId(i, j + l1, k);
            if(k2 == 0 || k2 == XieMod.leaves.blockID)
            {
                world.setBlockAndMetadata(i, j + l1, k, XieMod.log.blockID, 0);
            }
        }

        return true;
    }
	
	private final static int treeIDBitMask = 7;
	
	public static int getTreeIDFromMeta(int m) {
    	int treeID = m & treeIDBitMask;
    	if (treeID>=XieMod.treeManager.treeList.size()) return 0;
    	else return treeID;
    }
    
    public static int getMetaForTreeID(int m, int treeID) {
    	int meta = m | treeID;
    	return meta;
    }
}
