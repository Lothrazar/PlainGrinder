package com.lothrazar.plaingrinder.jei;

import com.lothrazar.plaingrinder.grind.GrindRecipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class GrindRecipeWrapper implements IRecipeWrapper {

  private final GrindRecipe recipe;

  public GrindRecipeWrapper(GrindRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<List<ItemStack>> in = new ArrayList<>();
    in.add(Arrays.asList(recipe.getMatchingStacks()));
    ingredients.setInputLists(ItemStack.class, in);
    ingredients.setOutput(ItemStack.class, recipe.getRecipeOutput());
  }
}
