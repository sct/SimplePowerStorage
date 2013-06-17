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

package sct.simplepowerstorage.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.PacketDispatcher;

public abstract class TileEntityMachine extends TileEntity {
	
	private ForgeDirection forwardDirection;
	private boolean working = false;
	private int idleTicks = 0;
	private int tier = 0;
	
	public TileEntityMachine() {
		forwardDirection = ForgeDirection.NORTH;
	}
	
	public ForgeDirection getDirectionFacing() {
		return forwardDirection;
	}
	
	public abstract boolean canRotate();
	
	public abstract boolean canUpgrade();
	
	public void rotate() {
		if (!worldObj.isRemote) {
			if (forwardDirection == ForgeDirection.DOWN) {
				forwardDirection = ForgeDirection.EAST;
			} else if (forwardDirection == ForgeDirection.EAST) {
				forwardDirection = ForgeDirection.SOUTH;
			} else if (forwardDirection == ForgeDirection.SOUTH) {
				forwardDirection = ForgeDirection.WEST;
			} else if (forwardDirection == ForgeDirection.WEST){
				forwardDirection = ForgeDirection.NORTH;
			} else if (forwardDirection == ForgeDirection.NORTH) {
				forwardDirection = ForgeDirection.UP;
			} else if (forwardDirection == ForgeDirection.UP) {
				forwardDirection = ForgeDirection.DOWN;
			} else {
				forwardDirection = ForgeDirection.NORTH;
			}
			
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 50, worldObj.provider.dimensionId, getDescriptionPacket());
		}
	}
	
	public void rotateDirectlyTo(int rotation) {
		forwardDirection = ForgeDirection.getOrientation(rotation);
		if (worldObj != null) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public void setIdleTicks(int idleTicks) {
		this.idleTicks = idleTicks;
	}
	
	public int getIdleTicks() {
		return idleTicks;
	}
	
	public abstract int getIdleTicksMax();
	
	public int getRotatedSide(int side) {
		if (forwardDirection == ForgeDirection.UP) {
			return verticalSide(side, true);
		} else if (forwardDirection == ForgeDirection.DOWN) {
			return verticalSide(side, false);
		} else if (forwardDirection == ForgeDirection.EAST){
			return addToSide(side, 1);
		} else if (forwardDirection == ForgeDirection.SOUTH) {
			return addToSide(side, 2);
		} else if (forwardDirection == ForgeDirection.WEST) {
			return addToSide(side, 3);
		}
		return side;
	}
	
	private int addToSide(int side, int shift) {
		int shiftsRemaining = shift;
		int out = side;
		
		while (shiftsRemaining > 0) {
			if (out == 2) out = 4;
			else if (out == 4) out = 3;
			else if (out == 3) out = 5;
			else if (out == 5) out = 2;
			shiftsRemaining--;
		}
		
		return out;
	}
	
	private int verticalSide(int side, boolean up) {
		if (side < 2) {
			if (up) {
				return side == 0 ? 2 : 3;
			} else {
				return side == 0 ? 3 : 2;
			}
		} else if (side == 3) {
			return 0;
		}
		
		return side;
	}
	
	public ForgeDirection getForwardDirection() {
		return forwardDirection;
	}
	
	public boolean isWorking() {
		return working;
	}
	
	public void setWorking(boolean working) {
		if (worldObj != null && !worldObj.isRemote && this.working != working) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 50, worldObj.provider.dimensionId, getDescriptionPacket());
			this.working = working;
		}	
	}
	
	public void setClientWorking(boolean working) {
		this.working = working;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void upgrade(EntityPlayer player) {
		ItemStack currentItem = player.inventory.getCurrentItem();
		
		if ((currentItem.getItemDamage() + 1) > getTier()) {
			setTier(currentItem.getItemDamage() + 1);
			
			if (currentItem.stackSize > 1) {
				currentItem.stackSize--;
			} else {
				player.destroyCurrentEquippedItem();
			}
		}
	}
	
	public int getTier() {
		return tier;
	}
	
	public void setTier(int tier) {
		if (worldObj != null && !worldObj.isRemote && this.tier != tier) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 50, worldObj.provider.dimensionId, getDescriptionPacket());
			this.tier = tier;
		}
	}
	
	public void setClientTier(int tier) {
		this.tier = tier;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		rotateDirectlyTo(pkt.customParam1.getInteger("rotation"));
		setClientWorking(pkt.customParam1.getBoolean("working"));
		setClientTier(pkt.customParam1.getInteger("tier"));
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("rotation", getDirectionFacing().ordinal());
		data.setBoolean("working", isWorking());
		data.setInteger("tier", getTier());
		Packet132TileEntityData packet = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, data);
		return packet;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		int rotation = tagCompound.getInteger("rotation");
		int newTier = tagCompound.getInteger("tier");
		rotateDirectlyTo(rotation);
		this.tier = newTier;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("rotation", getDirectionFacing().ordinal());
		tagCompound.setInteger("tier", getTier());
	}

}
