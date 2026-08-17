package com.lothrazar.plaingrinder.jei;

import com.lothrazar.plaingrinder.ModRegistry;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.GuiGrinder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PluginJEI implements IModPlugin {

  private static final int PLAYER_INV_SIZE = 4 * 9;

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    registry.addRecipeCategories(new RecipeCat(registry.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void register(IModRegistry registry) {
    registry.addRecipeCatalyst(new ItemStack(Item.getItemFromBlock(ModRegistry.B_GRINDER)), RecipeCat.ID);
    registry.handleRecipes(GrindRecipe.class, new IRecipeWrapperFactory<GrindRecipe>() {

      @Override
      public GrindRecipeWrapper getRecipeWrapper(GrindRecipe recipe) {
        return new GrindRecipeWrapper(recipe);
      }
    }, RecipeCat.ID);
    registry.addRecipes(GrindRecipe.RECIPES, RecipeCat.ID);
    registry.addRecipeClickArea(GuiGrinder.class, 72, 10, 34, 36, RecipeCat.ID);
  }
}
