package com.lothrazar.plaingrinder.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.ModRecipeType;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@SuppressWarnings("rawtypes")
@ZenRegister
@ZenCodeType.Name("mods.plaingrinder.grinder")
public class ZenRecipeGrinder implements IRecipeManager {

  @Override
  public IRecipeType getRecipeType() {
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
