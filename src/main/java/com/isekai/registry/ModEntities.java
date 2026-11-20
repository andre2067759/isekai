package com.isekai.registry;

import com.isekai.IsekaiMod;
import com.isekai.entity.AetherSentinel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, IsekaiMod.MOD_ID);

    public static final RegistryObject<EntityType<AetherSentinel>> AETHER_SENTINEL =
        ENTITY_TYPES.register("aether_sentinel", () -> EntityType.Builder
            .of(AetherSentinel::new, MobCategory.MONSTER)
            .sized(0.85F, 2.15F)
            .clientTrackingRange(8)
            .build("aether_sentinel"));

    public static void register(IEventBus bus) { ENTITY_TYPES.register(bus); }
}
