package zombiecraft.Core;

import java.util.LinkedList;
import java.util.List;

import zombiecraft.Forge.ZombieCraftMod;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class Buyables {

	//Static managers
	public static boolean hasInit = false;
	public static List<Buyables> items = new LinkedList();
	public static int priceBase = 50;
	public static int barrierCost = 500;
	
	public static int barricadeRepairRedeem = 5;
	public static int barricadeRepairCooldown = 20;
	
	//Contents
	public ItemStack item;
	public int cost;
	
	public Buyables(ItemStack parItem, int parCost) {
		this.item = parItem;
		this.cost = parCost;
	}
	
	public static int count() {
		return items.size();
	}
	
	public static void addBuyItem(ItemStack parItem, int parCost) {
		items.add(new Buyables(parItem, parCost));
	}
	
	public static ItemStack getBuyItem(int index) {
		if (!hasInit) initItems();
		return items.get(index).item;
	}
	
	public static int getBuyItemCost(int index) {
		if (!hasInit) initItems();
		return items.get(index).cost;
	}
	
	public static void initItems() {
		if (!hasInit) {
			hasInit = true;
			
			addBuyItem(new ItemStack(Item.swordSteel), priceBase);
			
			addBuyItem(new ItemStack(ZombieCraftMod.itemDEagle), (int)(priceBase*1.5));
			addBuyItem(new ItemStack(ZombieCraftMod.itemAk47), (int)(priceBase*2.5));
			addBuyItem(new ItemStack(ZombieCraftMod.itemM4), (int)(priceBase*3));
			addBuyItem(new ItemStack(ZombieCraftMod.itemShotgun), (int)(priceBase*3));
			
			addBuyItem(new ItemStack(ZombieCraftMod.itemFlamethrower), (int)(priceBase*4));
			addBuyItem(new ItemStack(ZombieCraftMod.itemSniper), (int)(priceBase*5));
			
			addBuyItem(new ItemStack(ZombieCraftMod.itemPerkSpeed), (int)(priceBase*5));
			addBuyItem(new ItemStack(ZombieCraftMod.itemPerkExStatic), (int)(priceBase*5));
			addBuyItem(new ItemStack(ZombieCraftMod.itemPerkJugg), (int)(priceBase*5));
			
		}
	}
}
