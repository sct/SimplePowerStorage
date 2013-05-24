package sct.simplepowerstorage.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import sct.simplepowerstorage.SimplePowerStorage;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SPSCreativeTab extends CreativeTabs {
	
	public static final SPSCreativeTab tab = new SPSCreativeTab("Simple Power Storage");

	public SPSCreativeTab(String label) {
		super(label);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(SimplePowerStorage.makeshiftBattery);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return this.getTabLabel();
	}

}
