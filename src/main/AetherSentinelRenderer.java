package com.isekai.client;

import com.isekai.IsekaiMod;
import com.isekai.entity.AetherSentinel;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class AetherSentinelRenderer extends MobRenderer<AetherSentinel, AetherSentinelModel> {
    private static final ResourceLocation TEXTURE =
        new ResourceLocation(IsekaiMod.MOD_ID, "textures/entity/aether_sentinel.png");

    public AetherSentinelRenderer(EntityRendererProvider.Context context) {
        super(context, new AetherSentinelModel(context.bakeLayer(AetherSentinelModel.LAYER_LOCATION)), 0.55F);
    }
    @Override public ResourceLocation getTextureLocation(AetherSentinel entity) { return TEXTURE; }
}
