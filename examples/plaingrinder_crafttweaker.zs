// https://docs.blamejared.com/

var grinder = <recipetype:plaingrinder:grinder>;

// delete recipe example 
//show advanced tooltips in minecraft and use JEI is one way to see these IDs
grinder.removeRecipe("plaingrinder:grind_emerald");


// add recipes. 
// IMPORTANT: the name must be unique
grinder.addRecipe("ghast_tear_test1", <item:minecraft:flint>, <item:minecraft:ghast_tear> * 16);

grinder.addRecipe("ghast_tear_test2", <item:minecraft:ghast_tear>, <item:minecraft:dirt>);

