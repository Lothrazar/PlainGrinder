package com.lothrazar.plaingrinder.jei;

import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.ScreenGrinder;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GrinderRecipeCategory implements IRecipeCategory<GrindRecipe> {

  public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModPlainGrinder.MODID, "grinder");
  static final RecipeType<GrindRecipe> TYPE = new RecipeType<>(ID, GrindRecipe.class);
  private IDrawable gui;
  private IDrawable icon;
  private TexturedProgress progress;

  public GrinderRecipeCategory(IGuiHelper helper) {
    gui = helper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(ModPlainGrinder.MODID, "textures/gui/jei.png"), 0, 0, 130, 20).setTextureSize(130, 20).build();
    icon = helper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(ModPlainGrinder.MODID, "textures/block/grinder_top.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
    this.progress = new TexturedProgress(Minecraft.getInstance().font, 58, 2, ScreenGrinder.SAW);
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public int getWidth() {
    return 130;
  }

  @Override
  public int getHeight() {
    return 20;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(RegistryGrinder.GRINDER.get().getDescriptionId());
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GrindRecipe recipe, IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 4, 2).addIngredients(recipe.getInput());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 2).addItemStack(recipe.getResultForDisplay());
  }

  @Override
  public void draw(GrindRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics ms, double mouseX, double mouseY) {
    gui.draw(ms, 0, 0);
    progress.draw(ms, 1);
  }

  @Override
  public RecipeType<GrindRecipe> getRecipeType() {
    return TYPE;
  }
}
