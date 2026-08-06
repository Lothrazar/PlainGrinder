package com.lothrazar.plaingrinder.jei;

import java.util.List;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.ScreenGrinder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class PluginJEI implements IModPlugin {

  private static final Identifier ID = Identifier.fromNamespaceAndPath(ModPlainGrinder.MODID, "jei");

  @Override
  public Identifier getPluginUid() {
    return ID;
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(RegistryGrinder.igrinder.get()), GrinderRecipeCategory.TYPE);
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new GrinderRecipeCategory(guiHelper));
  }

  // Full Recipe objects are no longer synced to the client at all (only recipe-book display data is,
  // via ClientRecipeContainer). Reading the local integrated server's RecipeManager directly is the only
  // way to get real GrindRecipe instances here; this only works in singleplayer/LAN-hosted worlds, so a
  // JEI-connected dedicated-server client simply won't see this category populated.
  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
    if (server == null) {
      return;
    }
    List<GrindRecipe> recipes = server.getRecipeManager()
        .recipeMap()
        .byType(RegistryGrinder.GRINDER_RECIPE_TYPE.get())
        .stream()
        .map(holder -> holder.value())
        .toList();
    registry.addRecipes(GrinderRecipeCategory.TYPE, recipes);
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    registry.addRecipeClickArea(ScreenGrinder.class,
        72, 10,
        34, 36, GrinderRecipeCategory.TYPE);
  }
}
