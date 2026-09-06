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
import mezz.jei.common.Internal;
import net.minecraft.resources.Identifier;
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


  // Full Recipe objects are no longer synced to the client via vanilla's own protocol (only recipe-book
  // display data is). JEI fills that gap itself: when JEI is installed on the server with a matching mod
  // loader, it syncs the full RecipeManager - every recipe type, not just vanilla's - into a client-side
  // RecipeMap.
  // TODO: probably change this if Internal.getClientSyncedRecipes ever gets access in jei's-API
  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    List<GrindRecipe> recipes = Internal.getClientSyncedRecipes()
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
