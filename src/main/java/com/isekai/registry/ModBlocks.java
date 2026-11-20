package com.isekai.registry;

import com.isekai.IsekaiMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, IsekaiMod.MOD_ID);

    public static final RegistryObject<Block> AETHER_BLOCK =
            BLOCKS.register("aether_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE).strength(5.0F, 6.0F)
                    .sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> RAW_AETHER_BLOCK =
            BLOCKS.register("raw_aether_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_CYAN).strength(4.0F, 5.0F)));

    public static final RegistryObject<Block> SUN_AETHER_BLOCK =
            BLOCKS.register("sun_aether_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW).strength(5.0F, 6.0F)));

    public static final RegistryObject<Block> MOON_AETHER_BLOCK =
            BLOCKS.register("moon_aether_block", () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE).strength(5.0F, 6.0F)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
