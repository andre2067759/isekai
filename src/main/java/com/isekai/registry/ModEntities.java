package com.isekai.registry;
import com.isekai.IsekaiMod;
import com.isekai.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModEntities {
 public static final DeferredRegister<EntityType<?>> ENTITY_TYPES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,IsekaiMod.MOD_ID);
 public static final RegistryObject<EntityType<AetherSentinel>> AETHER_SENTINEL=ENTITY_TYPES.register("aether_sentinel",()->EntityType.Builder.of(AetherSentinel::new,MobCategory.MONSTER).sized(0.85F,2.15F).clientTrackingRange(8).build("aether_sentinel"));
 public static final RegistryObject<EntityType<AetherStalker>> AETHER_STALKER=ENTITY_TYPES.register("aether_stalker",()->EntityType.Builder.of(AetherStalker::new,MobCategory.MONSTER).sized(0.72F,1.9F).clientTrackingRange(8).build("aether_stalker"));
 public static final RegistryObject<EntityType<AetherBrute>> AETHER_BRUTE=ENTITY_TYPES.register("aether_brute",()->EntityType.Builder.of(AetherBrute::new,MobCategory.MONSTER).sized(1.25F,2.75F).clientTrackingRange(10).build("aether_brute"));
 public static final RegistryObject<EntityType<AetherWraith>> AETHER_WRAITH=ENTITY_TYPES.register("aether_wraith",()->EntityType.Builder.of(AetherWraith::new,MobCategory.MONSTER).sized(0.75F,2.0F).clientTrackingRange(8).build("aether_wraith"));
 public static final RegistryObject<EntityType<AetherRavager>> AETHER_RAVAGER=ENTITY_TYPES.register("aether_ravager",()->EntityType.Builder.of(AetherRavager::new,MobCategory.MONSTER).sized(1.0F,2.3F).clientTrackingRange(10).build("aether_ravager"));
 public static final RegistryObject<EntityType<AetherWarden>> AETHER_WARDEN=ENTITY_TYPES.register("aether_warden",()->EntityType.Builder.of(AetherWarden::new,MobCategory.MONSTER).sized(1.45F,3.1F).clientTrackingRange(12).build("aether_warden"));
 public static void register(IEventBus bus){ENTITY_TYPES.register(bus);}
}