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
	
	public static int perkLengthJugg = 20 * 30;
	public static int perkLengthSpeed = 20 * 30;
	public static int perkLengthExStatic = 20 * 3; 
	public static int perkCooldownExStatic = 20 * 30;
	public static int perkLengthCharge = 20 * 2;
	public static int perkCooldownCharge = 20 * 15;
	
	public static int pickupLengthDoublePoints = 20 * 15;
	public static int pickupLengthInstaKill = 20 * 15;
	
	public static int perkMaxComrades = 1;
	public static int perkCostComrades = 1000;
	
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
			
			addBuyItem(new ItemStack(ZCItems.itemSword), priceBase);
			
			addBuyItem(new ItemStack(ZCItems.itemM1911), (int)(priceBase*1.0));
			addBuyItem(new ItemStack(ZCItems.itemDEagle), (int)(priceBase*1.5));
			addBuyItem(new ItemStack(ZCItems.itemRifle), (int)(priceBase*2));
			addBuyItem(new ItemStack(ZCItems.itemShotgun), (int)(priceBase*3));
			addBuyItem(new ItemStack(ZCItems.itemUzi), (int)(priceBase*2.5));
			addBuyItem(new ItemStack(ZCItems.itemAk47), (int)(priceBase*3.5));
			addBuyItem(new ItemStack(ZCItems.itemM4), (int)(priceBase*3));
			addBuyItem(new ItemStack(ZCItems.itemSniper), (int)(priceBase*5));
			addBuyItem(new ItemStack(ZCItems.itemFlamethrower), (int)(priceBase*4));
			addBuyItem(new ItemStack(ZCItems.itemRaygun), (int)(priceBase*7));
			addBuyItem(new ItemStack(ZCItems.itemRPG), (int)(priceBase*10));
			
			addBuyItem(new ItemStack(ZCItems.itemPerkSpeed), (int)(priceBase*3));
			addBuyItem(new ItemStack(ZCItems.itemPerkExStatic), (int)(priceBase*10));
			addBuyItem(new ItemStack(ZCItems.itemPerkJugg), (int)(priceBase*5));
			addBuyItem(new ItemStack(ZCItems.itemPerkCharge), (int)(priceBase*10));
			addBuyItem(new ItemStack(ZCItems.itemPerkComrade), (int)(perkCostComrades));
			
			addBuyItem(new ItemStack(ZCItems.barricadePlaceable), (int)(priceBase*3));
			
			addBuyItem(new ItemStack(ZCItems.itemChickenGun), (int)(priceBase*6));
			
			addBuyItem(new ItemStack(ZCBlocks.betty), (int)(priceBase*4));
			
		}
	}
}
