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

package sct.simplepowerstorage.item;

import java.util.List;

import sct.simplepowerstorage.gui.SPSCreativeTab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemMakeshiftUpgrade extends Item {
	
	protected String[] names;
	private Icon[] icons;

	public ItemMakeshiftUpgrade(int id) {
		super(id);
		setCreativeTab(SPSCreativeTab.tab);
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("sps.upgrade");
		setNames(new String[] { "tier1", "tier2" });
		icons = new Icon[names.length];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir) {
		for (int x = 0; x < names.length; x++) {
			icons[x] = ir.registerIcon(getUnlocalizedName() + "." + names[x]);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage) {
		return icons[Math.min(damage, icons.length - 1)];
	}

	protected void setNames(String[] names) {
		this.names = names;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName() + "." + names[Math.min(stack.getItemDamage(), names.length - 1)];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int itemId, CreativeTabs creativeTab, List subTypes) {
		for (int i = 0; i < names.length; i++) {
			subTypes.add(new ItemStack(itemId, 1, i));
		}
	}

}
