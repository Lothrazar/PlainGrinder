package com.lothrazar.plaingrinder;

import com.lothrazar.plaingrinder.grind.BlockEntityGrinder;
import com.lothrazar.plaingrinder.grind.BlockGrinder;
import com.lothrazar.plaingrinder.grind.ContainerGrinder;
import com.lothrazar.plaingrinder.grind.GrindRecipe;
import com.lothrazar.plaingrinder.grind.GrindRecipe.SerializeGrinderRecipe;
import com.lothrazar.plaingrinder.handle.BlockHandle;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryGrinder {

  static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModPlainGrinder.MODID);
  static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModPlainGrinder.MODID);
  static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModPlainGrinder.MODID);
  static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, ModPlainGrinder.MODID);
  static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModPlainGrinder.MODID);
  static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModPlainGrinder.MODID);
  private static final ResourceKey<CreativeModeTab> TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(ModPlainGrinder.MODID, "tab"));

  @SubscribeEvent
  public static void onCreativeModeTabRegister(RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(TAB, CreativeModeTab.builder().icon(() -> new ItemStack(ihandle.get()))
          .title(Component.translatable("itemGroup." + ModPlainGrinder.MODID))
          .displayItems((enabledFlags, populator) -> {
            for (RegistryObject<Item> entry : ITEMS.getEntries()) {
              populator.accept(entry.get());
            }
          }).build());
    });
  }

  public static final RegistryObject<Block> GRINDER = BLOCKS.register("grinder", () -> new BlockGrinder(Block.Properties.of().strength(0.9F)));
  public static final RegistryObject<Block> handle = BLOCKS.register("handle", () -> new BlockHandle(Block.Properties.of().strength(0.4F)));
  public static final RegistryObject<Item> igrinder = ITEMS.register("grinder", () -> new BlockItem(GRINDER.get(), new Item.Properties()));
  public static final RegistryObject<Item> ihandle = ITEMS.register("handle", () -> new BlockItem(handle.get(), new Item.Properties()));
  //block entity and container
  public static final RegistryObject<BlockEntityType<BlockEntityGrinder>> TE_GRINDER = TILES.register("grinder", () -> BlockEntityType.Builder.of(BlockEntityGrinder::new, GRINDER.get()).build(null));
  public static final RegistryObject<MenuType<ContainerGrinder>> MENU = MENUS.register("grinder", () -> IForgeMenuType.create(ContainerGrinder::new));
  //two for the recipe
  public static final RegistryObject<RecipeType<GrindRecipe>> GRINDER_RECIPE_TYPE = RECIPE_TYPES.register("grinder", () -> new RecipeType<GrindRecipe>() {
    //yep leave it empty its fine
  });
  public static final RegistryObject<SerializeGrinderRecipe> GRINDER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("grinder", SerializeGrinderRecipe::new);
  //items
  public static final RegistryObject<Item> dust_coal = ITEMS.register("dust_coal", () -> new ItemDustBurnable(new Item.Properties()));
  public static final RegistryObject<Item> dust_charcoal = ITEMS.register("dust_charcoal", () -> new ItemDustBurnable(new Item.Properties()));
  public static final RegistryObject<Item> dust_diamond = ITEMS.register("dust_diamond", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_gold = ITEMS.register("dust_gold", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_iron = ITEMS.register("dust_iron", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_emerald = ITEMS.register("dust_emerald", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_lapis = ITEMS.register("dust_lapis", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_copper = ITEMS.register("dust_copper", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> dust_quartz = ITEMS.register("dust_quartz", () -> new Item(new Item.Properties()));
}