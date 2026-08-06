package com.lothrazar.plaingrinder.ct;

import org.openzen.zencode.java.ZenCodeType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

@SuppressWarnings("rawtypes")
@ZenRegister
@ZenCodeType.Name("mods.plaingrinder.grinder")
public class ZenRecipeGrinder implements IRecipeManager {

  @Override
  public RecipeType getRecipeType() {
    return RegistryGrinder.GRINDER_RECIPE_TYPE.get();
  }

  @SuppressWarnings("unchecked")
  @ZenCodeType.Method
  public void addRecipe(String name, IIngredient input, IItemStack output) {
    name = fixRecipeName(name);
    GrindRecipe recipe = new GrindRecipe(
        input.asVanillaIngredient(),
        output.asImmutable().getInternal());
    RecipeHolder<GrindRecipe> holder = new RecipeHolder<>(
        Identifier.fromNamespaceAndPath("crafttweaker", name), recipe);
    CraftTweakerAPI.apply(new ActionAddRecipe<GrindRecipe>(this, holder, ""));
    ModPlainGrinder.LOGGER.info("crafttweaker: Recipe loaded " + name);
  }

  @ZenCodeType.Method
  public void removeRecipe(String... names) {
    removeByName(names);
    ModPlainGrinder.LOGGER.info("crafttweaker: Recipe removed " + names);
  }
}
