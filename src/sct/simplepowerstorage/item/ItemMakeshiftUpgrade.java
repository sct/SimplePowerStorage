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
