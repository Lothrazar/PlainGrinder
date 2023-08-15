package com.lothrazar.plaingrinder.jei;

import java.util.List;
import java.util.Objects;
import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.ModRegistry;
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

  private static final ResourceLocation ID = new ResourceLocation(ModMain.MODID, "jei");

  @Override
  public ResourceLocation getPluginUid() {
    return ID;
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(ModRegistry.igrinder.get()), RecipeCat.TYPE);
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new RecipeCat(guiHelper));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
    registry.addRecipes(RecipeCat.TYPE, List.copyOf(world.getRecipeManager().getAllRecipesFor(ModRegistry.GRINDER_RECIPE_TYPE.get())));
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    registry.addRecipeClickArea(ScreenGrinder.class,
        72, 10,
        34, 36, RecipeCat.TYPE);
  }
}