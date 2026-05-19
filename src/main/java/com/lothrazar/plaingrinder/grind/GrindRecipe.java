package com.lothrazar.plaingrinder.grind;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class GrindRecipe implements Recipe<SingleRecipeInput> {

  private Ingredient input = Ingredient.EMPTY;
  private ItemStack result = ItemStack.EMPTY;

  public GrindRecipe(Ingredient input, ItemStack result) {
    super();
    this.input = input;
    this.result = result;
  }

  public Ingredient getInput() {
    return input;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(SingleRecipeInput inv, Level worldIn) {
    return input.test(inv.getItem(0));
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width == 1 && height == 1;
  }

  @Override
  public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider ra) {
    return getResultItem(ra);
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return result.copy();
  }

  public ItemStack getResultForDisplay() {
    return result.copy();
  }

  @Override
  public RecipeType<?> getType() {
    return RegistryGrinder.GRINDER_RECIPE_TYPE.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RegistryGrinder.GRINDER_RECIPE_SERIALIZER.get();
  }

  public static class SerializeGrinderRecipe implements RecipeSerializer<GrindRecipe> {

    public static final MapCodec<GrindRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(r -> r.input),
            ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(instance, GrindRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GrindRecipe> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
        ItemStack.STREAM_CODEC, r -> r.result,
        GrindRecipe::new);

    public SerializeGrinderRecipe() {}

    @Override
    public MapCodec<GrindRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GrindRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
