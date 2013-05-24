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
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess iBlockAccess, int x,
			int y, int z, int side) {
		int md = iBlockAccess.getBlockMetadata(x, y, z);
		TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityMachine) {
			side = ((TileEntityMachine) te).getRotatedSide(side);
		}
		return this.getIcon(side, md);
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
