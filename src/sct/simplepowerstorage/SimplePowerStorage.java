package sct.simplepowerstorage;

import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sct.simplepowerstorage.block.BlockMakeshiftBattery;
import sct.simplepowerstorage.gui.GuiHandler;
import sct.simplepowerstorage.item.ItemMakeshiftConductanceCoil;
import sct.simplepowerstorage.setup.SPSConfig;
import sct.simplepowerstorage.tile.TileEntityMakeshiftBattery;
import thermalexpansion.api.item.ItemRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = SimplePowerStorage.modId, name = "Simple Power Storage", useMetadata = true, version = SimplePowerStorage.version,
dependencies = "after:BuildCraft|Core;after:BuildCraft|Factory;after:BuildCraft|Energy;after:BuildCraft|Builders;after:BuildCraft|Transport")
@NetworkMod(serverSideRequired = false, clientSideRequired = true)
public class SimplePowerStorage {
	public static final String modId = "simplepowerstorage";
	public static final String modNetworkChannel = "SimplePowerStorage";
	public static final String version = "1.5.2R1.0";
	
	@Instance(modId)
	public static SimplePowerStorage instance;
	
	@SidedProxy(clientSide="sct.simplepowerstorage.ClientProxy", serverSide="sct.simplepowerstorage.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	public static Block makeshiftBattery;
	
	public static Item makeshiftConductance;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent evt) {
		SPSConfig.setConfigFolderBase(evt.getModConfigurationDirectory());
		
		SPSConfig.loadCommonConfig(evt);
		
		SPSConfig.extractLang(new String[] { "en_US" });
		SPSConfig.loadLang();
		
		logger = evt.getModLog();
	}
	
	@Init
	public void init(FMLInitializationEvent evt) {
		makeshiftBattery = new BlockMakeshiftBattery(SPSConfig.makeshiftBatteryId.getInt());
		if (Loader.isModLoaded("ThermalExpansion")) {
			makeshiftConductance = new ItemMakeshiftConductanceCoil(SPSConfig.conductanceCoilId.getInt());
		}
		
		GameRegistry.registerBlock(makeshiftBattery, makeshiftBattery.getUnlocalizedName());
		
		GameRegistry.registerTileEntity(TileEntityMakeshiftBattery.class, "entityMSBattery");
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		if (Loader.isModLoaded("ThermalExpansion")) {
			ItemStack powerCoilGold = ItemRegistry.getItem("powerCoilGold", 1);
			ItemStack powerCoilSilver = ItemRegistry.getItem("powerCoilSilver", 1);
			ItemStack machineFrame = ItemRegistry.getItem("machineFrame", 1);
			
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(makeshiftConductance), new Object[]
					{
					"  G",
					" C ",
					"S  ",
					'G', powerCoilGold,
					'C', Item.clay,
					'S', powerCoilSilver
					}));
			
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(makeshiftBattery), new Object[]
					{
					"ICI",
					"CMC",
					"IXI",
					'I', "ingotIron",
					'C', "ingotCopper",
					'M', machineFrame,
					'X', makeshiftConductance
					}));
		} else {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(makeshiftBattery), new Object[]
					{
					"III",
					"SRS",
					"III",
					'I', "ingotIron",
					'S', Item.stick,
					'R', Item.redstone
					}));
		}
	}
}
