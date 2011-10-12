/*
 *  XieFoodLists.java
 *  Xie 17th June 2011
 *  
 *  This class contains lists for foodstuffs which are referenced by my other mods
 *  The lists may be added to by other mods in order to allow my mods to support those foods
 * 
 */

package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

public class XieFoodLists {
	public static boolean initialised = false;
	
	public static void init() {
		if (initialised) return;
		
		setDefaults();
		
		initialised = true;
	}
	
	private static void setDefaults() {
		// add native foods to defaults
		// native foods/ingredients supported: 
		// apple, golden apple, red mushroom, brown mushroom, bread, porkchop raw, porkchop cooked
		// mushroom stew, fish raw, fish cooked, sugar, cookie, cocoa bean, wheat
		
		// sammich stuffs
		addItemsToList(new Item [] {Item.porkCooked, Item.fishCooked}, sammichList);
		
		// fruits
		addItemsToList(new Item[] {Item.appleGold, Item.appleRed}, fruitList);
		
		// flavourings
		addItemsToList(new Item[] {Item.sugar, Item.dyePowder}, flavorList);
		
		// salad ingredients
		addItemsToList(new Item[] {Item.appleRed, Item.appleGold}, saladList);
		
		// juicy foods (i.e. foods that provide liquid as well as food)
		addItemsToList(new Item[] {Item.appleRed, Item.appleGold, Item.fishRaw}, juicyList);
		
	}
	
	public static void addItemToLists(Item item, List<Item> [] lists) {
		for (int i=0; i<lists.length; i++) {
			lists[i].add(item);
		}
	}
	
	public static void addItemsToList(Item [] items, List<Item> list) {
		for (int i=0; i<items.length; i++) {
			list.add(items[i]);
		}
	}

	// Food Lists
	public static List<Item> sammichList = new LinkedList<Item>();
	public static List<Item> sauceList = new LinkedList<Item>();
	public static List<Item> fruitList = new LinkedList<Item>();
	public static List<Item> flavorList = new LinkedList<Item>();
	public static List<Item> saladList = new LinkedList<Item>();
	// juicy foods contribute a little to thirst, juice foods contribute mostly to thirst, water foods contribute only to thirst
	// foods not on any of these lists contribute only to hunger relief
	public static List<Item> juicyList = new LinkedList<Item>();
	public static List<Item> juiceList = new LinkedList<Item>();
	public static List<Item> waterList = new LinkedList<Item>();
	// Custom food list is for food items from other mods that are food but don't inherit the food item class
	public static List<Item> customFoodList = new LinkedList<Item>();
}
