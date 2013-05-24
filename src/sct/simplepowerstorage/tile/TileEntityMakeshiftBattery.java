package sct.simplepowerstorage.tile;

import net.minecraft.tileentity.TileEntity;
import sct.simplepowerstorage.core.BlockPosition;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.IEngineProvider;


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
		
		BlockPosition ourbp = BlockPosition.fromMachineTile(this);
		
		for(BlockPosition bp : ourbp.getAdjacent(true))
		{
			TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);
			if(te == null || !(te instanceof IPowerReceptor) || te instanceof IEngineProvider)
			{
				continue;
			}
			
			IPowerReceptor ipr = ((IPowerReceptor)te);
			IPowerProvider pp = ipr.getPowerProvider();
			if(pp != null && pp.preConditions(ipr) && pp.getMinEnergyReceived() <= mj)
			{
				int mjUsed = Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int)Math.floor(pp.getEnergyStored()));
				
				if (te instanceof TileEntityMakeshiftBattery) {
					if (((TileEntityMakeshiftBattery) te).getEnergyStored() < getEnergyStored()) {
						setEnergyStored(getEnergyStored() - mjUsed);
						pp.receiveEnergy(mjUsed, bp.orientation);
					}
				} else {
					setEnergyStored(getEnergyStored() - mjUsed);
					pp.receiveEnergy(mjUsed, bp.orientation);
				}
				
				mj -= mjUsed;
				if(mj <= 0)
				{
					return 0;
				}
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
	
}
