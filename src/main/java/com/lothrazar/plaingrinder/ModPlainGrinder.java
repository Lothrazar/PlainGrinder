package com.lothrazar.plaingrinder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.plaingrinder.data.GrindEvents;
import com.lothrazar.plaingrinder.grind.ScreenGrinder;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModPlainGrinder.MODID)
public class ModPlainGrinder {

  public static final String MODID = "plaingrinder";
  public static final Logger LOGGER = LogManager.getLogger();

  public ModPlainGrinder() {
    new ConfigPlainGrinder();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    //https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes/blob/master/src/main/java/net/darkhax/customrecipeexample/CustomRecipesMod.java
    //    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(RecipeSerializer.class, ModMain::registerRecipeSerializers);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    RegistryGrinder.ITEMS.register(eventBus);
    RegistryGrinder.BLOCKS.register(eventBus);
    RegistryGrinder.MENUS.register(eventBus);
    RegistryGrinder.TILES.register(eventBus);
    RegistryGrinder.RECIPE_TYPES.register(eventBus);
    RegistryGrinder.RECIPE_SERIALIZERS.register(eventBus);
  }

  //todo: mekanism and thermal built in support
  //3x ores in mystical ag - direct recipes
  //ex nihilo ore chunks
  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist  
    MinecraftForge.EVENT_BUS.register(new GrindEvents());
  }

  private void setupClient(final FMLClientSetupEvent event) {
    //for client side only setup
    event.enqueueWork(() -> {
      MenuScreens.register(RegistryGrinder.MENU.get(), ScreenGrinder::new);
    });
  }
  //  public static void registerRecipeSerializers(Register<RecipeSerializer<?>> event) {
}