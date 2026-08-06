package com.lothrazar.plaingrinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.plaingrinder.data.GrindEvents;
import com.lothrazar.plaingrinder.data.IItemHandlerResourceHandler;
import com.lothrazar.plaingrinder.grind.ScreenGrinder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(ModPlainGrinder.MODID)
public class ModPlainGrinder {

  public static final String MODID = "plaingrinder";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModPlainGrinder(IEventBus modEventBus, ModContainer modContainer) {
    modContainer.registerConfig(ModConfig.Type.COMMON, ConfigPlainGrinder.COMMON_CONFIG);
    modEventBus.addListener(this::setup);
    modEventBus.addListener(this::registerScreens);
    modEventBus.addListener(this::registerCapabilities);
    //https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes/blob/master/src/main/java/net/darkhax/customrecipeexample/CustomRecipesMod.java
    RegistryGrinder.ITEMS.register(modEventBus);
    RegistryGrinder.BLOCKS.register(modEventBus);
    RegistryGrinder.MENUS.register(modEventBus);
    RegistryGrinder.TILES.register(modEventBus);
    RegistryGrinder.RECIPE_TYPES.register(modEventBus);
    RegistryGrinder.RECIPE_SERIALIZERS.register(modEventBus);
  }

  //todo: mekanism and thermal built in support
  //3x ores in mystical ag - direct recipes
  //ex nihilo ore chunks
  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    NeoForge.EVENT_BUS.register(new GrindEvents());
  }

  private void registerScreens(final RegisterMenuScreensEvent event) {
    event.register(RegistryGrinder.MENU.get(), ScreenGrinder::new);
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(
        Capabilities.Item.BLOCK,
        RegistryGrinder.TE_GRINDER.get(),
        (be, side) -> ConfigPlainGrinder.AUTOMATION_ALLOWED.get() ? new IItemHandlerResourceHandler(be.getInventory()) : null);
  }
}
