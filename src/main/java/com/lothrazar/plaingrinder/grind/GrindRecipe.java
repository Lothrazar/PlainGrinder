package com.lothrazar.plaingrinder.grind;

import java.util.HashSet;
import java.util.Set;
import com.google.gson.JsonObject;
import com.lothrazar.plaingrinder.ModPlainGrinder;
import com.lothrazar.plaingrinder.RegistryGrinder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class GrindRecipe implements Recipe<BlockEntityGrinder> {

  private static final Set<String> HASHES = new HashSet<>();
  public static final Set<GrindRecipe> RECIPES = new HashSet<>();
  private final ResourceLocation id;
  private Ingredient input = Ingredient.EMPTY;
  private ItemStack result = ItemStack.EMPTY;

  public GrindRecipe(ResourceLocation id, Ingredient input, ItemStack result) {
    super();
    this.id = id;
    this.input = input;
    this.result = result;
  }

  public Ingredient getInput() {
    return input;
  }

  @Override
  public boolean matches(BlockEntityGrinder inv, Level worldIn) {
    for (ItemStack test : input.getItems()) {
      if (matchingStacks(test, inv.inputSlots.getStackInSlot(0))) {
        return true;
      }
    }
    return false;
  }

  public static boolean matchingStacks(ItemStack current, ItemStack in) {
    //first one fails if size is off 
    return ItemStack.isSameItem(current, in) // isSameIgnoreDurability
        && ItemStack.isSameItemSameTags(current, in);
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width == 1 && height == 1;
  }

  @Override
  public ItemStack assemble(BlockEntityGrinder inv, RegistryAccess ra) {
    return getResultItem(ra);
  }

  @Override
  public ItemStack getResultItem(RegistryAccess ra) {
    return result.copy();
  }

  public ItemStack getResultForDisplay() {
    return result.copy();
  }

  @Override
  public ResourceLocation getId() {
    return id;
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

    public SerializeGrinderRecipe() {
      // This registry name is what people will specify in their json files.
      //      this.setRegistryName(new ResourceLocation(ModMain.MODID, "grinder"));
    }

    @Override
    public GrindRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      GrindRecipe r = null;
      try {
        Ingredient inputFirst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        r = new GrindRecipe(recipeId, inputFirst, resultStack);
        addRecipe(r);
        return r;
      }
      catch (Exception e) {
        ModPlainGrinder.LOGGER.error("Error loading recipe" + recipeId, e);
        return null;
      }
    }

    @Override
    public GrindRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      GrindRecipe r = new GrindRecipe(recipeId, Ingredient.fromNetwork(buffer), buffer.readItem());
      //server reading recipe from client or vice/versa 
      addRecipe(r);
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, GrindRecipe recipe) {
      recipe.input.toNetwork(buffer);
      buffer.writeItem(recipe.result);
    }
  }

  public static boolean addRecipe(GrindRecipe r) {
    ResourceLocation id = r.getId();
    if (HASHES.contains(id.toString())) {
      return false;
    }
    RECIPES.add(r);
    HASHES.add(id.toString());
    ModPlainGrinder.LOGGER.info("Recipe loaded " + id.toString());
    return true;
  }
}