package com.isekai.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/** Armoured mid-tier executioner with high melee pressure. */
public class AetherRavager extends AetherSentinel {
    public AetherRavager(EntityType<? extends AetherSentinel> type, Level level) {
        super(type, level);
        this.xpReward = 90;
    }

    public static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder createAttributes() {
        return net.minecraft.world.entity.monster.Monster.createMonsterAttributes()
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 68.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.23D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 11.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 9.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.FOLLOW_RANGE, 32.0D)
            .add(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE, 0.48D);
    }
}
