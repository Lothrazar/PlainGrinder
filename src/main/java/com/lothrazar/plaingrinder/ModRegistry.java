package com.lothrazar.plaingrinder;

import com.lothrazar.plaingrinder.grind.BlockGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.TileGrinder;
import com.lothrazar.plaingrinder.handle.BlockHandle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ModRegistry {

  public static final CreativeTabs GROUP = new CreativeTabs(ModMain.MODID) {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModRegistry.B_HANDLE);
    }
  };
  public static Block B_HANDLE;
  public static Block B_GRINDER;
  private static final List<Item> ITEMS = new ArrayList<>();

  private static Block register(Block block, String name) {
    block.setRegistryName(new ResourceLocation(ModMain.MODID, name));
    block.setTranslationKey(name);
    block.setCreativeTab(GROUP);
    return block;
  }

  private static Item register(Item item, String name) {
    item.setRegistryName(new ResourceLocation(ModMain.MODID, name));
    item.setTranslationKey(name);
    item.setCreativeTab(GROUP);
    ITEMS.add(item);
    return item;
  }

  @SubscribeEvent
  public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
    B_GRINDER = register(new BlockGrinder(Material.ROCK), "grinder");
    B_HANDLE = register(new BlockHandle(Material.WOOD), "handle");
    event.getRegistry().registerAll(B_GRINDER, B_HANDLE);
    GameRegistry.registerTileEntity(TileGrinder.class, new ResourceLocation(ModMain.MODID, "grinder"));
  }

  @SubscribeEvent
  public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
    event.getRegistry().registerAll(
        register(new ItemBlock(B_GRINDER), "grinder"),
        register(new ItemBlock(B_HANDLE), "handle"),
        register(new ItemDustBurnable(), "dust_coal"),
        register(new Item(), "dust_diamond"),
        register(new Item(), "dust_gold"),
        register(new Item(), "dust_iron"),
        register(new Item(), "dust_emerald"),
        register(new Item(), "dust_lapis"),
        register(new ItemDustBurnable(), "dust_charcoal"),
        register(new Item(), "dust_quartz"));
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void registerModels(ModelRegistryEvent event) {
    for (Item item : ITEMS) {
      ModelLoader.setCustomModelResourceLocation(item, 0,
          new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
  }

  @SubscribeEvent
  public static void onRecipesRegistry(final RegistryEvent.Register<IRecipe> event) {
    event.getRegistry().register(
        new ShapedOreRecipe(new ResourceLocation(ModMain.MODID, "grinder"),
            new ItemStack(B_GRINDER),
            "s s", "sis", "sss",
            's', Blocks.STONE,
            'i', Items.IRON_NUGGET)
            .setRegistryName(new ResourceLocation(ModMain.MODID, "grinder")));
    event.getRegistry().register(
        new ShapedOreRecipe(new ResourceLocation(ModMain.MODID, "coal_block_from_dust"),
            new ItemStack(Blocks.COAL_BLOCK),
            "cc", "cc",
            'c', new ItemStack(ModRegistry.itemDustCoal()))
            .setRegistryName(new ResourceLocation(ModMain.MODID, "coal_block_from_dust")));
    GrindRecipe.initRecipes();
  }

  public static Item itemDustCoal() {
    return Item.getByNameOrId(ModMain.MODID + ":dust_coal");
  }
}
