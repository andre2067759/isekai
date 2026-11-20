package com.isekai.item;

import com.isekai.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum AetherTier implements Tier {
    AETHER(1800, 8.5F, 4.0F, 3, 18),
    SUN(2200, 9.5F, 5.0F, 4, 22),
    MOON(1600, 8.0F, 3.5F, 3, 26);

    private final int uses;
    private final float speed;
    private final float damage;
    private final int level;
    private final int enchant;
    AetherTier(int uses, float speed, float damage, int level, int enchant) {
        this.uses=uses; this.speed=speed; this.damage=damage; this.level=level; this.enchant=enchant;
    }
    public int getUses() { return uses; }
    public float getSpeed() { return speed; }
    public float getAttackDamageBonus() { return damage; }
    public int getLevel() { return level; }
    public int getEnchantmentValue() { return enchant; }
    public Ingredient getRepairIngredient() {
        return Ingredient.of(this == SUN ? ModItems.SUN_AETHER_INGOT.get() :
            this == MOON ? ModItems.MOON_AETHER_INGOT.get() : ModItems.AETHER_INGOT.get());
    }
}
