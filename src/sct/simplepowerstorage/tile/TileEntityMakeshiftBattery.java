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

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;


public class TileEntityMakeshiftBattery extends TileEntityMachinePowered {
	
	private int ticksBetweenConsumption = 20;
	private int outputPulseSize = 100;
	
	private int ticksSinceLastConsumption = 0;

	public TileEntityMakeshiftBattery() {
		super(1);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if(!worldObj.isRemote && isPowered())
		{
			if (getEnergyStored() > 0) {
				
				
				if(ticksSinceLastConsumption < ticksBetweenConsumption)
				{
					ticksSinceLastConsumption++;
					return;
				}
				ticksSinceLastConsumption = 0;
				
				int mjPulse = Math.min(getEnergyStored(), outputPulseSize);
				
				producePower(mjPulse);
			}
		}
	}
	
	public final int producePower(int mj) {
		
		ForgeDirection dir;
		
		if (getForwardDirection().equals(ForgeDirection.UP) || getForwardDirection().equals(ForgeDirection.DOWN)) {
			dir = getForwardDirection();
		} else {
			dir = getForwardDirection().getOpposite();
		}
		
		TileEntity te = worldObj.getBlockTileEntity(xCoord + dir.offsetX, 
				yCoord + dir.offsetY, zCoord + dir.offsetZ);
		
		if(te == null || !(te instanceof IPowerReceptor))
		{
			return mj;
		}
		
		IPowerReceptor ipr = ((IPowerReceptor)te);
		IPowerProvider pp = ipr.getPowerProvider();
		if(pp != null && pp.preConditions(ipr) && pp.getMinEnergyReceived() <= mj)
		{
			int mjUsed = Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int)Math.floor(pp.getEnergyStored()));
			
			if (te instanceof TileEntityMakeshiftBattery) {
				if (((TileEntityMakeshiftBattery) te).getEnergyStored() < getEnergyStored()) {
					setEnergyStored(getEnergyStored() - mjUsed);
					pp.receiveEnergy(mjUsed, getForwardDirection());
				}
			} else {
				setEnergyStored(getEnergyStored() - mjUsed);
				pp.receiveEnergy(mjUsed, getForwardDirection());
			}
			
			mj -= mjUsed;
			if(mj <= 0)
			{
				return 0;
			}
		}
		
		return mj;
	}
	
	@Override
	public boolean canRotate() {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public int getEnergyStoredMax() {
		switch (getTier()) {
			case 1:
				return 35000;
			case 2:
				return 80000;
			default:
				return 10000;
		}
	}

	@Override
	public int getIdleTicksMax() {
		return 0;
	}
	
	@Override
	public int powerRequest(ForgeDirection from) {
		if (getForwardDirection().getOpposite().equals(from)) {
			return 0;
		}
		
		return super.powerRequest(from);
	}

	@Override
	public boolean canUpgrade() {
		return true;
	}
	
}
