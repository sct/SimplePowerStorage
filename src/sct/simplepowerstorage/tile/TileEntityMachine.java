package sct.simplepowerstorage.tile;

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
	
	public TileEntityMachine() {
		forwardDirection = ForgeDirection.NORTH;
	}
	
	public ForgeDirection getDirectionFacing() {
		return forwardDirection;
	}
	
	public boolean canRotate() {
		return false;
	}
	
	public void rotate() {
		if (!worldObj.isRemote) {
			if (forwardDirection == ForgeDirection.NORTH) {
				forwardDirection = ForgeDirection.EAST;
			} else if (forwardDirection == ForgeDirection.EAST) {
				forwardDirection = ForgeDirection.SOUTH;
			} else if (forwardDirection == ForgeDirection.SOUTH) {
				forwardDirection = ForgeDirection.WEST;
			} else if (forwardDirection == ForgeDirection.WEST){
				forwardDirection = ForgeDirection.NORTH;
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
		if (side < 2) {
			return side;
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
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		rotateDirectlyTo(pkt.customParam1.getInteger("rotation"));
		setClientWorking(pkt.customParam1.getBoolean("working"));
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("rotation", getDirectionFacing().ordinal());
		data.setBoolean("working", isWorking());
		Packet132TileEntityData packet = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, data);
		return packet;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		int rotation = tagCompound.getInteger("rotation");
		rotateDirectlyTo(rotation);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("rotation", getDirectionFacing().ordinal());
	}

}
