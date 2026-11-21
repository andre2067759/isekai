package com.isekai.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Heavy elite guardian: slow, durable and dangerous at close range. */
public class AetherBrute extends AetherSentinel {
    public AetherBrute(EntityType<? extends AetherSentinel> type, Level level) {
        super(type, level);
        this.xpReward = 45;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 92.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.19D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 14.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 12.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 30.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 0.72D);
    }
}
