package com.lothrazar.plaingrinder.jei;

import com.lothrazar.plaingrinder.ModPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RecipeCat implements IRecipeCategory<GrindRecipe> {

  public static final ResourceLocation ID = new ResourceLocation(RegistryGrinder.GRINDER_RECIPE_TYPE.getId().toString());
  static final RecipeType<GrindRecipe> TYPE = new RecipeType<>(ID, GrindRecipe.class);
  private IDrawable gui;
  private IDrawable icon;

  public RecipeCat(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModPlainGrinder.MODID, "textures/gui/jei.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(ModPlainGrinder.MODID, "textures/block/grinder_top.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public IDrawable getBackground() {
    return gui;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(RegistryGrinder.GRINDER.get().getDescriptionId());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GrindRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 19).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 19).addItemStack(recipe.getResultForDisplay());
  }

  @Override
  public RecipeType<GrindRecipe> getRecipeType() {
    return TYPE;
  }
}