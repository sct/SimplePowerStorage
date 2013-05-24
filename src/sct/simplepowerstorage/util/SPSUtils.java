package sct.simplepowerstorage.util;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class SPSUtils {

	public static boolean isHoldingTool(EntityPlayer player)
	{
		if (player.inventory.getCurrentItem() == null) {
			return false;
		}
		Item currentItem = Item.itemsList[player.inventory.getCurrentItem().itemID];
		if (currentItem != null && currentItem instanceof IToolWrench) {
			return true;
		}
		return false;
	}
}
