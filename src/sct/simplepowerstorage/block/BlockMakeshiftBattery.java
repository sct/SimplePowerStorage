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

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sct.simplepowerstorage.gui.SPSCreativeTab;
import sct.simplepowerstorage.tile.TileEntityMachine;
import sct.simplepowerstorage.tile.TileEntityMakeshiftBattery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMakeshiftBattery extends BlockMachine {
	
	private Icon[] icons = new Icon[6];
	private Icon[] iconsTierOne = new Icon[6];
	private Icon[] iconsTierTwo = new Icon[6];

	public BlockMakeshiftBattery(int id) {
		super(id);
		setUnlocalizedName("sps.makeshiftbattery");
		setStepSound(soundMetalFootstep);
		setCreativeTab(SPSCreativeTab.tab);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir) {
		icons[0] = ir.registerIcon(getUnlocalizedName() + ".bottom");
		icons[1] = ir.registerIcon(getUnlocalizedName() + ".bottom");
		icons[2] = ir.registerIcon(getUnlocalizedName() + ".back");
		icons[3] = ir.registerIcon(getUnlocalizedName() + ".front");
		icons[4] = ir.registerIcon(getUnlocalizedName() + ".left");
		icons[5] = ir.registerIcon(getUnlocalizedName() + ".right");
		
		iconsTierOne[0] = ir.registerIcon(getUnlocalizedName() + ".1.bottom");
		iconsTierOne[1] = ir.registerIcon(getUnlocalizedName() + ".1.bottom");
		iconsTierOne[2] = ir.registerIcon(getUnlocalizedName() + ".1.side");
		iconsTierOne[3] = ir.registerIcon(getUnlocalizedName() + ".1.front");
		iconsTierOne[4] = ir.registerIcon(getUnlocalizedName() + ".1.side");
		iconsTierOne[5] = ir.registerIcon(getUnlocalizedName() + ".1.side");
		
		iconsTierTwo[0] = ir.registerIcon(getUnlocalizedName() + ".2.bottom");
		iconsTierTwo[1] = ir.registerIcon(getUnlocalizedName() + ".2.bottom");
		iconsTierTwo[2] = ir.registerIcon(getUnlocalizedName() + ".2.side");
		iconsTierTwo[3] = ir.registerIcon(getUnlocalizedName() + ".2.front");
		iconsTierTwo[4] = ir.registerIcon(getUnlocalizedName() + ".2.side");
		iconsTierTwo[5] = ir.registerIcon(getUnlocalizedName() + ".2.side");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x,
			int y, int z, int side) {
		
		TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityMachine) {
			side = ((TileEntityMachine) te).getRotatedSide(side);
		}
		return this.getIconTier(side, ((TileEntityMachine) te).getTier());
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconTier(int side, int tier) {
		switch (tier) {
			case 1:
				return iconsTierOne[side];
			case 2:
				return iconsTierTwo[side];
			default:
				return icons[side];
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return icons[side];
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMakeshiftBattery();
	}

}
