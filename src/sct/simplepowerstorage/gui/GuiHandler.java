package sct.simplepowerstorage.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import sct.simplepowerstorage.gui.client.GuiMakeshiftBattery;
import sct.simplepowerstorage.gui.container.ContainerMakeshiftBattery;
import sct.simplepowerstorage.tile.TileEntityMakeshiftBattery;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityMakeshiftBattery) {
			return new ContainerMakeshiftBattery(player.inventory, (TileEntityMakeshiftBattery) tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityMakeshiftBattery) {
			return new GuiMakeshiftBattery(player.inventory, (TileEntityMakeshiftBattery) tileEntity);
		}
		return null;
	}

}
