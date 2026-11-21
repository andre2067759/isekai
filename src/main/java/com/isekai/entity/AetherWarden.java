package com.isekai.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Rare world boss prototype: extremely durable and slow, with a heavy strike. */
public class AetherWarden extends AetherSentinel {
    public AetherWarden(EntityType<? extends AetherSentinel> type, Level level) {
        super(type, level);
        this.xpReward = 350;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 260.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.17D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 18.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 16.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 40.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 0.90D);
    }
}
