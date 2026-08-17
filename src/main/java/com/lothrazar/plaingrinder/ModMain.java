package com.lothrazar.plaingrinder;

import com.lothrazar.plaingrinder.data.GrindEvents;
import com.lothrazar.plaingrinder.grind.ContainerGrinder;
import com.lothrazar.plaingrinder.grind.GuiGrinder;
import com.lothrazar.plaingrinder.grind.TileGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.IFuelHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModMain.MODID, name = "Plain Grinder", version = "@VERSION@",
    certificateFingerprint = "@FINGERPRINT@",
    updateJSON = "https://raw.githubusercontent.com/Lothrazar/PlainGrinder/trunk/1.12/update.json",
    acceptedMinecraftVersions = "[1.12,1.13)")
public class ModMain implements IGuiHandler {

  public static final String MODID = "plaingrinder";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final int GUI_GRINDER = 0;
  @Instance(ModMain.MODID)
  public static ModMain instance;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    ConfigManager.setup(config);
    MinecraftForge.EVENT_BUS.register(ModRegistry.class);
    MinecraftForge.EVENT_BUS.register(new GrindEvents());
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, this);
    GameRegistry.registerFuelHandler(new IFuelHandler() {

      @Override
      public int getBurnTime(ItemStack fuel) {
        if (!fuel.isEmpty() && fuel.getItem() instanceof ItemDustBurnable) {
          return ItemDustBurnable.BURN_TIME;
        }
        return 0;
      }
    });
  }

  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    if (id == GUI_GRINDER) {
      TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
      if (te instanceof TileGrinder) {
        return new ContainerGrinder(player.inventory, (TileGrinder) te);
      }
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    if (id == GUI_GRINDER) {
      TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
      if (te instanceof TileGrinder) {
        InventoryPlayer inv = player.inventory;
        return new GuiGrinder(new ContainerGrinder(inv, (TileGrinder) te), inv);
      }
    }
    return null;
  }
}
