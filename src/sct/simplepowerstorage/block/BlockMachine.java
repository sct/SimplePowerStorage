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

package sct.simplepowerstorage.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import sct.simplepowerstorage.SimplePowerStorage;
import sct.simplepowerstorage.gui.SPSCreativeTab;
import sct.simplepowerstorage.tile.TileEntityMachine;
import sct.simplepowerstorage.util.SPSUtils;

public abstract class BlockMachine extends BlockContainer {

	public BlockMachine(int id) {
		super(id, Material.rock);
		setHardness(0.5F);
		setStepSound(soundMetalFootstep);
		setCreativeTab(SPSCreativeTab.tab);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null || player.isSneaking()) {
			return false;
		}
		
		if (SPSUtils.isHoldingTool(player) && ((TileEntityMachine) te).canRotate()) {
			((TileEntityMachine) te).rotate();
		} else if (SPSUtils.isHoldingUpgrade(player) && ((TileEntityMachine) te).canUpgrade()) {
			((TileEntityMachine) te).upgrade(player);
		} else {
			player.openGui(SimplePowerStorage.instance, 0, world, x, y, z);
		}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack stack)
	{
		if(entity == null)
		{
			return;
		}
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if(stack.getTagCompound() != null)
		{
			stack.getTagCompound().setInteger("x", x);
			stack.getTagCompound().setInteger("y", y);
			stack.getTagCompound().setInteger("z", z);
			te.readFromNBT(stack.getTagCompound());
		}

		if(te instanceof TileEntityMachine && ((TileEntityMachine)te).canRotate())
		{
			int facing = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
			if(facing == 0)
			{
				((TileEntityMachine)te).rotateDirectlyTo(3);
			}
			else if(facing == 1)
			{
				((TileEntityMachine)te).rotateDirectlyTo(4);
			}
			else if(facing == 2)
			{
				((TileEntityMachine)te).rotateDirectlyTo(2);
			}
			else if(facing == 3)
			{
				((TileEntityMachine)te).rotateDirectlyTo(5);
			}
		}
	}

}
