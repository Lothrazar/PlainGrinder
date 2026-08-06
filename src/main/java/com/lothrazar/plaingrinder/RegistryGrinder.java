package com.lothrazar.plaingrinder;

import java.util.function.UnaryOperator;

import com.lothrazar.plaingrinder.grind.BlockEntityGrinder;
import com.lothrazar.plaingrinder.grind.BlockGrinder;
import com.lothrazar.plaingrinder.grind.ContainerGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.handle.BlockEntityHandleAuto;
import com.lothrazar.plaingrinder.handle.BlockHandle;
import com.lothrazar.plaingrinder.handle.BlockHandleAuto;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = ModPlainGrinder.MODID)
public class RegistryGrinder {

  static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModPlainGrinder.MODID);
  static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModPlainGrinder.MODID);
  static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModPlainGrinder.MODID);
  static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ModPlainGrinder.MODID);
  static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModPlainGrinder.MODID);
  static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModPlainGrinder.MODID);
  private static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(ModPlainGrinder.MODID, "tab"));

  @SubscribeEvent
  public static void onCreativeModeTabRegister(RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(TAB, CreativeModeTab.builder().icon(() -> new ItemStack(ihandle.get()))
          .title(Component.translatable("itemGroup." + ModPlainGrinder.MODID))
          .displayItems((enabledFlags, populator) -> {
            ITEMS.getEntries().forEach(entry -> populator.accept(entry.get()));
          }).build());
    });
  }

  public static final DeferredBlock<BlockGrinder> GRINDER = BLOCKS.register("grinder", id -> new BlockGrinder(Block.Properties.of().strength(0.9F).setId(ResourceKey.create(Registries.BLOCK, id))));
  public static final DeferredBlock<BlockHandle> handle = BLOCKS.register("handle", id -> new BlockHandle(Block.Properties.of().strength(0.4F).setId(ResourceKey.create(Registries.BLOCK, id))));
  public static final DeferredItem<BlockItem> igrinder = ITEMS.registerSimpleBlockItem("grinder", GRINDER, UnaryOperator.identity());
  public static final DeferredItem<BlockItem> ihandle = ITEMS.registerSimpleBlockItem("handle", handle, UnaryOperator.identity());
  //auto handle
  public static final DeferredBlock<BlockHandleAuto> HANDLE_AUTO = BLOCKS.register("handle_auto", id -> new BlockHandleAuto(Block.Properties.of().strength(0.4F).setId(ResourceKey.create(Registries.BLOCK, id))));
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHandleAuto>> TE_HANDLE = TILES.register("handle_auto", () -> new BlockEntityType<>(BlockEntityHandleAuto::new, HANDLE_AUTO.get()));
  public static final DeferredItem<BlockItem> iauto_handle = ITEMS.registerSimpleBlockItem("handle_auto", HANDLE_AUTO, UnaryOperator.identity());
  //block entity and container
  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityGrinder>> TE_GRINDER = TILES.register("grinder", () -> new BlockEntityType<>(BlockEntityGrinder::new, GRINDER.get()));
  public static final DeferredHolder<MenuType<?>, MenuType<ContainerGrinder>> MENU = MENUS.register("grinder", () -> IMenuTypeExtension.create(ContainerGrinder::new));
  //two for the recipe
  public static final DeferredHolder<RecipeType<?>, RecipeType<GrindRecipe>> GRINDER_RECIPE_TYPE = RECIPE_TYPES.register("grinder", () -> new RecipeType<GrindRecipe>() {
    @Override
    public String toString() {
      return ModPlainGrinder.MODID + ":grinder";
    }
  });
  public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrindRecipe>> GRINDER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("grinder", () -> GrindRecipe.SERIALIZER);
  //items
  public static final DeferredItem<ItemDustBurnable> dust_coal = ITEMS.register("dust_coal", id -> new ItemDustBurnable(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<ItemDustBurnable> dust_charcoal = ITEMS.register("dust_charcoal", id -> new ItemDustBurnable(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_diamond = ITEMS.register("dust_diamond", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_gold = ITEMS.register("dust_gold", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_iron = ITEMS.register("dust_iron", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_emerald = ITEMS.register("dust_emerald", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_lapis = ITEMS.register("dust_lapis", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_copper = ITEMS.register("dust_copper", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
  public static final DeferredItem<Item> dust_quartz = ITEMS.register("dust_quartz", id -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
}
