package com.isekai.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** A swift, fragile ambusher that rewards awareness and mobility. */
public class AetherWraith extends AetherSentinel {
    public AetherWraith(EntityType<? extends AetherSentinel> type, Level level) {
        super(type, level);
        this.xpReward = 55;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 38.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.34D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 9.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 3.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 38.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 0.10D);
    }
}
