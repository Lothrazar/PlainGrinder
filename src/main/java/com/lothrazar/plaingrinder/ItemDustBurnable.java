package com.lothrazar.plaingrinder;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;

public class ItemDustBurnable extends Item {

  public ItemDustBurnable(Properties properties) {
    super(properties);
  }

  @Override
  public int getBurnTime(ItemStack item, @Nullable IRecipeType<?> recipeType) {
    return 60 * 20; // charcoal/coal is 80 . blaze rod is 120; oak log is 15
  }
}
