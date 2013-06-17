/*
 * SimplePowerStorage
 * Copyright (C) 2013  Ryan Cohen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sct.simplepowerstorage.util;

import sct.simplepowerstorage.item.ItemMakeshiftUpgrade;
import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class SPSUtils {

	public static boolean isHoldingTool(EntityPlayer player) {
		if (player.inventory.getCurrentItem() == null) {
			return false;
		}
		Item currentItem = Item.itemsList[player.inventory.getCurrentItem().itemID];
		if (currentItem != null && currentItem instanceof IToolWrench) {
			return true;
		}
		return false;
	}
	
	public static boolean isHoldingUpgrade(EntityPlayer player) {
		if (player.inventory.getCurrentItem() == null) {
			return false;
		}
		Item currentItem = Item.itemsList[player.inventory.getCurrentItem().itemID];
		if (currentItem != null && currentItem instanceof ItemMakeshiftUpgrade) {
			return true;
		}
		return false;
	}
}
