package com.isekai.registry;

import com.isekai.IsekaiMod;
import com.isekai.client.AetherSentinelRenderer;
import com.isekai.entity.AetherSentinel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import com.isekai.client.AetherSentinelModel;

@Mod.EventBusSubscriber(modid = IsekaiMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.AETHER_SENTINEL.get(), AetherSentinel.createAttributes().build());
        event.put(ModEntities.AETHER_STALKER.get(), com.isekai.entity.AetherStalker.createAttributes().build());
        event.put(ModEntities.AETHER_BRUTE.get(), com.isekai.entity.AetherBrute.createAttributes().build());
    }

    @Mod.EventBusSubscriber(modid = IsekaiMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(AetherSentinelModel.LAYER_LOCATION, AetherSentinelModel::createBodyLayer);
        }

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(ModEntities.AETHER_SENTINEL.get(), AetherSentinelRenderer::new);
                EntityRenderers.register(ModEntities.AETHER_STALKER.get(), AetherSentinelRenderer::new);
                EntityRenderers.register(ModEntities.AETHER_BRUTE.get(), AetherSentinelRenderer::new);
            });
        }
    }
}
