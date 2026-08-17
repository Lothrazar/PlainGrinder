// 1.12.2 CraftTweaker2 / ZenScript syntax

import mods.plaingrinder.Grinder;

// delete recipe example
// show advanced tooltips in minecraft and use JEI to see item ids
// removeRecipe matches by OUTPUT item, since 1.12.2 recipes have no name/id
Grinder.removeRecipe(<plaingrinder:dust_emerald>);

// add recipes. output count is set with * amount
// IIngredient input, IItemStack output
Grinder.addRecipe(<minecraft:flint>, <minecraft:ghast_tear> * 16);

Grinder.addRecipe(<minecraft:ghast_tear>, <minecraft:dirt>);
