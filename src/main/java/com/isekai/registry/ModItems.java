package com.isekai.registry;

import com.isekai.IsekaiMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, IsekaiMod.MOD_ID);

    public static final RegistryObject<Item> AETHER_INGOT =
            ITEMS.register("aether_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_AETHER =
            ITEMS.register("raw_aether", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SUN_AETHER_INGOT =
            ITEMS.register("sun_aether_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MOON_AETHER_INGOT =
            ITEMS.register("moon_aether_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> AETHER_BLOCK_ITEM =
            ITEMS.register("aether_block", () -> new BlockItem(ModBlocks.AETHER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> RAW_AETHER_BLOCK_ITEM =
            ITEMS.register("raw_aether_block", () -> new BlockItem(ModBlocks.RAW_AETHER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUN_AETHER_BLOCK_ITEM =
            ITEMS.register("sun_aether_block", () -> new BlockItem(ModBlocks.SUN_AETHER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> MOON_AETHER_BLOCK_ITEM =
            ITEMS.register("moon_aether_block", () -> new BlockItem(ModBlocks.MOON_AETHER_BLOCK.get(), new Item.Properties()));

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(ForgeRegistries.Keys.CREATIVE_MODE_TABS, IsekaiMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ISEKAI_TAB =
            TABS.register("isekai_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.isekai"))
                    .icon(() -> AETHER_INGOT.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(AETHER_INGOT.get());
                        output.accept(RAW_AETHER.get());
                        output.accept(SUN_AETHER_INGOT.get());
                        output.accept(MOON_AETHER_INGOT.get());
                        output.accept(AETHER_BLOCK_ITEM.get());
                        output.accept(RAW_AETHER_BLOCK_ITEM.get());
                        output.accept(SUN_AETHER_BLOCK_ITEM.get());
                        output.accept(MOON_AETHER_BLOCK_ITEM.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        TABS.register(eventBus);
    }
}
