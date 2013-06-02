package sct.simplepowerstorage.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import sct.simplepowerstorage.tile.TileEntityMachinePowered;
import sct.simplepowerstorage.tile.TileEntityMakeshiftBattery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMakeshiftBattery extends Container {
	protected TileEntity te;
	
	public ContainerMakeshiftBattery(InventoryPlayer invPlayer, TileEntity te) {
		this.te = te;
		
		bindPlayerInventory(invPlayer);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < crafters.size(); i++) {
			((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, 0, ((TileEntityMachinePowered)te).getEnergyStored());
		}
	}
	
	protected void bindPlayerInventory(InventoryPlayer invPlayer) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 9; y++) {
				addSlotToContainer(new Slot(invPlayer, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
			}
		}
		
		for (int x = 0; x < 9; x++) {
			addSlotToContainer(new Slot(invPlayer, x, 8 + x * 18, 142));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int var, int value) {
		super.updateProgressBar(var, value);
		
		if (var == 0) {
			((TileEntityMachinePowered)te).setEnergyStored(value);
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return ((TileEntityMakeshiftBattery) te).isUseableByPlayer(player);
	}
}
