package com.lothrazar.plaingrinder.grind;

import com.lothrazar.plaingrinder.ModMain;
import com.lothrazar.plaingrinder.ModRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class GrindRecipe {

  public static final List<GrindRecipe> RECIPES = new ArrayList<>();

  public final Ingredient input;
  private final ItemStack result;

  public GrindRecipe(Ingredient input, ItemStack result) {
    this.input = input;
    this.result = result;
  }

  public boolean matches(ItemStack stack) {
    return !stack.isEmpty() && input.apply(stack);
  }

  public ItemStack getRecipeOutput() {
    return result.copy();
  }

  public ItemStack[] getMatchingStacks() {
    return input.getMatchingStacks();
  }

  public static void addRecipe(Ingredient input, ItemStack result) {
    RECIPES.add(new GrindRecipe(input, result));
    ModMain.LOGGER.info("Recipe loaded grinder: " + result);
  }

  public static void removeRecipesForOutput(ItemStack output) {
    RECIPES.removeIf(r -> ItemStack.areItemsEqual(r.result, output));
  }

  /**
   * Ore dictionary based recipe for mod-compat materials that don't have a fixed 1.12.2 item id
   * (e.g. tin/silver/osmium dusts from whichever ore mod is loaded). Skipped entirely if nothing
   * on the modpack registers the output ore dictionary name yet - equivalent to the original
   * mod's forge:mod_loaded json conditions.
   */
  private static void addOreToOreRecipe(String inputOreDict, String outputOreDict, int outputCount) {
    List<ItemStack> outputs = OreDictionary.getOres(outputOreDict, false);
    if (outputs.isEmpty()) {
      return;
    }
    ItemStack result = outputs.get(0).copy();
    result.setCount(outputCount);
    addRecipe(new OreIngredient(inputOreDict), result);
  }

  private static void addItemToOreRecipe(ItemStack inputStack, String outputOreDict, int outputCount) {
    List<ItemStack> outputs = OreDictionary.getOres(outputOreDict, false);
    if (outputs.isEmpty()) {
      return;
    }
    ItemStack result = outputs.get(0).copy();
    result.setCount(outputCount);
    addRecipe(Ingredient.fromStacks(inputStack), result);
  }

  public static void initRecipes() {
    Item dustCoal = ModRegistry.itemDustCoal();
    Item dustDiamond = Item.getByNameOrId(ModMain.MODID + ":dust_diamond");
    Item dustGold = Item.getByNameOrId(ModMain.MODID + ":dust_gold");
    Item dustIron = Item.getByNameOrId(ModMain.MODID + ":dust_iron");
    Item dustEmerald = Item.getByNameOrId(ModMain.MODID + ":dust_emerald");
    Item dustLapis = Item.getByNameOrId(ModMain.MODID + ":dust_lapis");
    Item dustCharcoal = Item.getByNameOrId(ModMain.MODID + ":dust_charcoal");
    Item dustQuartz = Item.getByNameOrId(ModMain.MODID + ":dust_quartz");
    // ores -> plaingrinder's own dusts
    addRecipe(new OreIngredient("oreIron"), new ItemStack(dustIron, 2));
    addRecipe(new OreIngredient("oreCoal"), new ItemStack(dustCoal, 2));
    addRecipe(new OreIngredient("oreDiamond"), new ItemStack(dustDiamond, 2));
    addRecipe(new OreIngredient("oreEmerald"), new ItemStack(dustEmerald, 2));
    addRecipe(new OreIngredient("oreGold"), new ItemStack(dustGold, 2));
    addRecipe(new OreIngredient("oreLapis"), new ItemStack(dustLapis, 3));
    addRecipe(new OreIngredient("oreQuartz"), new ItemStack(dustQuartz, 4));
    // vanilla item -> vanilla/self item, no ore dictionary needed
    addRecipe(Ingredient.fromStacks(new ItemStack(Items.COAL, 1, 1)), new ItemStack(dustCharcoal, 2)); // charcoal = coal meta 1
    addRecipe(new OreIngredient("oreRedstone"), new ItemStack(Items.REDSTONE, 3));
    addRecipe(Ingredient.fromItem(Items.BLAZE_ROD), new ItemStack(Items.BLAZE_POWDER, 3));
    addRecipe(Ingredient.fromItem(Items.BONE), new ItemStack(Items.DYE, 4, 15)); // bone_meal = dye meta 15
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.CLAY)), new ItemStack(Items.CLAY_BALL, 4));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.COBBLESTONE)), new ItemStack(Blocks.GRAVEL, 1));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.DIRT, 1, 1)), new ItemStack(Blocks.DIRT, 1, 0)); // coarse_dirt(meta1) -> dirt
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)), new ItemStack(Items.FLINT, 1));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.GLOWSTONE)), new ItemStack(Items.GLOWSTONE_DUST, 4));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.TNT)), new ItemStack(Items.GUNPOWDER, 5));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.PRISMARINE, 1, 0)), new ItemStack(Items.PRISMARINE_SHARD, 4));
    addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.STONE, 1, 0)), new ItemStack(Blocks.COBBLESTONE, 1));
    addRecipe(Ingredient.fromItem(Items.REEDS), new ItemStack(Items.SUGAR, 2));
    // mod-compat: ore dictionary in, ore dictionary out. silently skipped if no mod on the pack registers the output name.
    addOreToOreRecipe("oreCopper", "dustCopper", 2);
    addOreToOreRecipe("oreLead", "dustLead", 2);
    addOreToOreRecipe("oreNickel", "dustNickel", 2);
    addOreToOreRecipe("oreOsmium", "dustOsmium", 2);
    addOreToOreRecipe("oreSilver", "dustSilver", 2);
    addOreToOreRecipe("oreSulfur", "dustSulfur", 2);
    addOreToOreRecipe("oreTin", "dustTin", 2);
    addOreToOreRecipe("oreUranium", "dustUranium", 2);
    addItemToOreRecipe(new ItemStack(Blocks.OBSIDIAN), "dustObsidian", 4);
    addItemToOreRecipe(new ItemStack(Items.ENDER_PEARL), "dustEnderPearl", 1);
  }
}
