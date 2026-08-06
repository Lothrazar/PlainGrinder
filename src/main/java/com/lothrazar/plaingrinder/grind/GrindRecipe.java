package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.RegistryGrinder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;

public class GrindRecipe extends SingleItemRecipe {

  public static final RecipeSerializer<GrindRecipe> SERIALIZER = new RecipeSerializer<>(
      simpleMapCodec(GrindRecipe::new), simpleStreamCodec(GrindRecipe::new));

  public GrindRecipe(Recipe.CommonInfo commonInfo, Ingredient input, ItemStackTemplate result) {
    super(commonInfo, input, result);
  }

  public GrindRecipe(Ingredient input, ItemStack result) {
    this(new Recipe.CommonInfo(true), input, ItemStackTemplate.fromNonEmptyStack(result));
  }

  public Ingredient getInput() {
    return input();
  }

  public ItemStack getResultForDisplay() {
    return result().create();
  }

  @Override
  public RecipeType<GrindRecipe> getType() {
    return RegistryGrinder.GRINDER_RECIPE_TYPE.get();
  }

  @Override
  public RecipeSerializer<GrindRecipe> getSerializer() {
    return RegistryGrinder.GRINDER_RECIPE_SERIALIZER.get();
  }

  @Override
  public String group() {
    return "";
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.STONECUTTER;
  }
}
