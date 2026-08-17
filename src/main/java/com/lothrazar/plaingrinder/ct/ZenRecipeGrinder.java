package com.lothrazar.plaingrinder.ct;

import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.plaingrinder.Grinder")
@ZenRegister
public class ZenRecipeGrinder {

  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void addRecipe(IIngredient input, IItemStack output) {
    ItemStack outputStack = CraftTweakerMC.getItemStack(output);
    GrindRecipe.addRecipe(CraftTweakerMC.getIngredient(input), outputStack);
    ModMain.LOGGER.info("crafttweaker: Recipe loaded grinder: " + outputStack);
  }

  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    ItemStack outputStack = CraftTweakerMC.getItemStack(output);
    GrindRecipe.removeRecipesForOutput(outputStack);
    ModMain.LOGGER.info("crafttweaker: Recipe removed grinder: " + outputStack);
  }
}
