package com.isekai.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Fast, lightly armoured hunter that pressures ranged characters. */
public class AetherStalker extends AetherSentinel {
    public AetherStalker(EntityType<? extends AetherSentinel> type, Level level) {
        super(type, level);
        this.xpReward = 12;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 26.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.36D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 2.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 34.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 0.05D);
    }
}
