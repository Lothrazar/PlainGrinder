package com.lothrazar.plaingrinder.jei;

import java.util.List;
import java.util.Objects;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class PluginJEI implements IModPlugin {

  private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModPlainGrinder.MODID, "jei");

  @Override
  public ResourceLocation getPluginUid() {
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

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
    List<GrindRecipe> recipes = world.getRecipeManager()
        .getAllRecipesFor(RegistryGrinder.GRINDER_RECIPE_TYPE.get())
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
