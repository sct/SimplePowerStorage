package sct.simplepowerstorage.tile;

import java.util.logging.Level;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import sct.simplepowerstorage.SimplePowerStorage;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;


public class TileEntityMakeshiftBattery extends TileEntityMachinePowered {
	
	private int ticksBetweenConsumption = 20;
	private int outputPulseSize = 100;
	
	private int ticksSinceLastConsumption = 0;

	public TileEntityMakeshiftBattery() {
		super(50);
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
		
		SimplePowerStorage.logger.log(Level.SEVERE, "Found receptor @ " + te.xCoord + ", " + te.yCoord + ", " + te.zCoord);
		
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
		return 10000;
	}

	@Override
	public int getIdleTicksMax() {
		return 0;
	}
	
	@Override
	public int powerRequest(ForgeDirection from) {
		if (getForwardDirection().equals(from)) {
			return 0;
		}
		
		return super.powerRequest(from);
	}
	
}
