package com.lothrazar.plaingrinder.ct;

import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.ModRecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("rawtypes")
@ZenRegister
@ZenCodeType.Name("mods.plaingrinder.grinder")
public class ZenRecipeGrinder implements IRecipeManager {

  @Override
  public RecipeType getRecipeType() {
    return ModRecipeType.GRIND;
  }

  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient input, IItemStack output) {
    name = fixRecipeName(name);
    GrindRecipe m = new GrindRecipe(new ResourceLocation("crafttweaker", name),
        input.asVanillaIngredient(),
        output.asImmutable().getInternal());
    CraftTweakerAPI.apply(new ActionAddRecipe(this, m, ""));
    ModMain.LOGGER.info("crafttweaker: Recipe loaded " + m.getId().toString());
  }

  @ZenCodeType.Method
  public void removeRecipe(String name) {
    removeByName(name);
    ModMain.LOGGER.info("crafttweaker: Recipe removed " + name);
  }
}
