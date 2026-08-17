package com.lothrazar.plaingrinder.jei;

import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.ModRegistry;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class RecipeCat implements IRecipeCategory<GrindRecipeWrapper> {

  public static final String ID = ModMain.MODID + ".grinder";
  private final IDrawable gui;
  private final IDrawable icon;

  public RecipeCat(IGuiHelper helper) {
    gui = helper.drawableBuilder(new ResourceLocation(ModMain.MODID, "textures/gui/jei.png"), 0, 0, 169, 69).setTextureSize(169, 69).build();
    icon = helper.drawableBuilder(new ResourceLocation(ModMain.MODID, "textures/block/grinder_top.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
  }

  @Override
  public String getUid() {
    return ID;
  }

  @Override
  public String getTitle() {
    return lang(ModRegistry.B_GRINDER.getTranslationKey() + ".name");
  }

  @Override
  public String getModName() {
    return "Plain Grinder";
  }

  public static String lang(String message) {
    return new TextComponentTranslation(message).getFormattedText();
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
  public void setRecipe(IRecipeLayout recipeLayout, GrindRecipeWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 3, 18);
    guiItemStacks.init(1, false, 107, 18);
    guiItemStacks.set(ingredients);
  }
}
