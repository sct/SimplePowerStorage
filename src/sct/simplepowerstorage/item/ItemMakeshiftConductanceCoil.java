package sct.simplepowerstorage.item;

import sct.simplepowerstorage.gui.SPSCreativeTab;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemMakeshiftConductanceCoil extends Item {

	public ItemMakeshiftConductanceCoil(int id) {
		super(id);
		setUnlocalizedName("sps.makeshiftconductance");
		setCreativeTab(SPSCreativeTab.tab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir) {
		itemIcon = ir.registerIcon(getUnlocalizedName());
	}

}
