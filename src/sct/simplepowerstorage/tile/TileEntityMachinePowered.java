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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import sct.simplepowerstorage.power.PowerProviderAdvanced;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;

public abstract class TileEntityMachinePowered extends TileEntityMachineInventory implements IPowerReceptor {
	
	protected static final int wPerEnergy = 10;
	
	private boolean powered = false;
	private int energyStored;
	protected int energyCost;
	
	private IPowerProvider powerProvider;
	
	protected TileEntityMachinePowered(int energyCostMJ) {
		this.energyCost = energyCostMJ;
		powerProvider = new PowerProviderAdvanced();
		powerProvider.configure(10, 10, 200, 1, 100);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		energyStored = Math.min(energyStored, getEnergyStoredMax());
		
		if (worldObj.isRemote) {
			return;
		}
		
		if (getPowerProvider() != null) {
			getPowerProvider().update(this);
			
			int mjRequired = Math.min((getEnergyStoredMax() - getEnergyStored()), 100);
			if (energyStored < getEnergyStoredMax() && getPowerProvider().useEnergy(1, mjRequired, false) > 0) {
				int mjGained = (int) (getPowerProvider().useEnergy(1, mjRequired, true));
				energyStored += mjGained;
			}
		}
		
		setPowered(energyStored >= energyCost * 2);
		
		if (getIdleTicks() > 0) {
			setIdleTicks(getIdleTicks() - 1);
		} else if (isPowered() && isWorking()) {
			energyStored -= energyCost;
			setIdleTicks(getIdleTicksMax());
		}
	}
	
	public int getEnergyStored() {
		return energyStored;
	}
	
	public void setEnergyStored(int energyStored) {
		this.energyStored = energyStored;
	}
	
	public boolean isPowered() {
		return powered;
	}
	
	public void setPowered(boolean powered) {
		this.powered = powered;
	}
	
	public abstract int getEnergyStoredMax();

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {
	}

	@Override
	public int powerRequest(ForgeDirection from) {
		return (int)Math.max(powerProvider.getMaxEnergyStored() - powerProvider.getEnergyStored(), 0);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("energyStored", getEnergyStored());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		
		setEnergyStored(tagCompound.getInteger("energyStored"));
	}

}
