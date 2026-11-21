package com.isekai.registry;

import com.isekai.IsekaiMod;
import com.isekai.item.AetherTier;
import com.isekai.item.AetherArmorMaterial;
import com.isekai.registry.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IsekaiMod.MOD_ID);
    public static final RegistryObject<Item> RAW_AETHER = reg("raw_aether");
    public static final RegistryObject<Item> AETHER_INGOT = reg("aether_ingot");
    public static final RegistryObject<Item> SUN_AETHER_INGOT = reg("sun_aether_ingot");
    public static final RegistryObject<Item> MOON_AETHER_INGOT = reg("moon_aether_ingot");
    public static final RegistryObject<Item> AETHER_SHARD = reg("aether_shard");
    public static final RegistryObject<Item> AETHER_DUST = reg("aether_dust");
    public static final RegistryObject<Item> AETHER_CRYSTAL = reg("aether_crystal");
    public static final RegistryObject<Item> SUN_CRYSTAL = reg("sun_crystal");
    public static final RegistryObject<Item> MOON_CRYSTAL = reg("moon_crystal");

    public static final RegistryObject<Item> AETHER_SWORD = ITEMS.register("aether_sword",
        () -> new SwordItem(AetherTier.AETHER, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_PICKAXE = ITEMS.register("aether_pickaxe",
        () -> new PickaxeItem(AetherTier.AETHER, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_AXE = ITEMS.register("aether_axe",
        () -> new AxeItem(AetherTier.AETHER, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_SHOVEL = ITEMS.register("aether_shovel",
        () -> new ShovelItem(AetherTier.AETHER, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_HOE = ITEMS.register("aether_hoe",
        () -> new HoeItem(AetherTier.AETHER, -2, 0.0F, new Item.Properties()));

    public static final RegistryObject<Item> SUN_SWORD = ITEMS.register("sun_sword",
        () -> new SwordItem(AetherTier.SUN, 4, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> MOON_SWORD = ITEMS.register("moon_sword",
        () -> new SwordItem(AetherTier.MOON, 3, -2.2F, new Item.Properties()));

    public static final RegistryObject<Item> AETHER_HELMET = armor("aether_helmet", AetherArmorMaterial.AETHER, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> AETHER_CHESTPLATE = armor("aether_chestplate", AetherArmorMaterial.AETHER, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> AETHER_LEGGINGS = armor("aether_leggings", AetherArmorMaterial.AETHER, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> AETHER_BOOTS = armor("aether_boots", AetherArmorMaterial.AETHER, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> SUN_HELMET = armor("sun_helmet", AetherArmorMaterial.SUN, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> SUN_CHESTPLATE = armor("sun_chestplate", AetherArmorMaterial.SUN, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> SUN_LEGGINGS = armor("sun_leggings", AetherArmorMaterial.SUN, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> SUN_BOOTS = armor("sun_boots", AetherArmorMaterial.SUN, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> MOON_HELMET = armor("moon_helmet", AetherArmorMaterial.MOON, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> MOON_CHESTPLATE = armor("moon_chestplate", AetherArmorMaterial.MOON, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> MOON_LEGGINGS = armor("moon_leggings", AetherArmorMaterial.MOON, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> MOON_BOOTS = armor("moon_boots", AetherArmorMaterial.MOON, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> AETHER_BOW = ITEMS.register("aether_bow",
        () -> new BowItem(new Item.Properties().durability(850)));
    public static final RegistryObject<Item> AETHER_FRUIT = ITEMS.register("aether_fruit",
        () -> new Item(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
            .nutrition(6).saturationMod(0.8F).alwaysEat().build())));

    // Expanded materials, relic weapons and collectible accessories.
    public static final RegistryObject<Item> VOIDSTEEL_INGOT = reg("voidsteel_ingot");
    public static final RegistryObject<Item> EMBERSTEEL_INGOT = reg("embersteel_ingot");
    public static final RegistryObject<Item> STARLIGHT_INGOT = reg("starlight_ingot");
    public static final RegistryObject<Item> VOID_CRYSTAL = reg("void_crystal");
    public static final RegistryObject<Item> EMBER_CRYSTAL = reg("ember_crystal");
    public static final RegistryObject<Item> STARLIGHT_CRYSTAL = reg("starlight_crystal");
    public static final RegistryObject<Item> VOID_SWORD = ITEMS.register("void_sword",
        () -> new SwordItem(AetherTier.MOON, 6, -2.5F, new Item.Properties()));
    public static final RegistryObject<Item> SUN_GREATBLADE = ITEMS.register("sun_greatblade",
        () -> new SwordItem(AetherTier.SUN, 8, -3.2F, new Item.Properties()));
    public static final RegistryObject<Item> MOON_DAGGER = ITEMS.register("moon_dagger",
        () -> new SwordItem(AetherTier.MOON, 2, -1.6F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_SPEAR = ITEMS.register("aether_spear",
        () -> new SwordItem(AetherTier.AETHER, 4, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_STAFF = reg("aether_staff");
    public static final RegistryObject<Item> VOID_AMULET = reg("void_amulet");
    public static final RegistryObject<Item> SUN_RING = reg("sun_ring");
    public static final RegistryObject<Item> MOON_TALISMAN = reg("moon_talisman");
    public static final RegistryObject<Item> GREATER_AETHER_FRUIT = ITEMS.register("greater_aether_fruit",
        () -> new Item(new Item.Properties().food(new net.minecraft.world.food.FoodProperties.Builder()
            .nutrition(10).saturationMod(1.0F).alwaysEat().build())));

    public static final RegistryObject<Item> AETHER_WRAITH_SPAWN_EGG = ITEMS.register("aether_wraith_spawn_egg",
        () -> new SpawnEggItem(ModEntities.AETHER_WRAITH, 0x17152B, 0x8B5CF6, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_RAVAGER_SPAWN_EGG = ITEMS.register("aether_ravager_spawn_egg",
        () -> new SpawnEggItem(ModEntities.AETHER_RAVAGER, 0x252A35, 0xD97736, new Item.Properties()));
    public static final RegistryObject<Item> AETHER_WARDEN_SPAWN_EGG = ITEMS.register("aether_warden_spawn_egg",
        () -> new SpawnEggItem(ModEntities.AETHER_WARDEN, 0x101A2B, 0x38BDF8, new Item.Properties()));

    public static final RegistryObject<Item> AETHER_BLOCK_ITEM = blockItem("aether_block", ModBlocks.AETHER_BLOCK);
    public static final RegistryObject<Item> RAW_AETHER_BLOCK_ITEM = blockItem("raw_aether_block", ModBlocks.RAW_AETHER_BLOCK);
    public static final RegistryObject<Item> SUN_AETHER_BLOCK_ITEM = blockItem("sun_aether_block", ModBlocks.SUN_AETHER_BLOCK);
    public static final RegistryObject<Item> MOON_AETHER_BLOCK_ITEM = blockItem("moon_aether_block", ModBlocks.MOON_AETHER_BLOCK);
    public static final RegistryObject<Item> AETHER_ORE_ITEM = blockItem("aether_ore", ModBlocks.AETHER_ORE);
    public static final RegistryObject<Item> DEEPSLATE_AETHER_ORE_ITEM = blockItem("deepslate_aether_ore", ModBlocks.DEEPSLATE_AETHER_ORE);

    private static RegistryObject<Item> armor(String name, AetherArmorMaterial material, ArmorItem.Type type) {
        return ITEMS.register(name, () -> new ArmorItem(material, type, new Item.Properties()));
    }
    private static RegistryObject<Item> reg(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
    private static RegistryObject<Item> blockItem(String name, RegistryObject<net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IsekaiMod.MOD_ID);
    public static final RegistryObject<CreativeModeTab> ISEKAI_TAB = TABS.register("isekai_tab",
        () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.isekai"))
            .icon(() -> AETHER_INGOT.get().getDefaultInstance())
            .displayItems((params, out) -> ITEMS.getEntries().forEach(item -> out.accept(item.get())))
            .build());

    public static void register(IEventBus bus) { ITEMS.register(bus); TABS.register(bus); }
}
