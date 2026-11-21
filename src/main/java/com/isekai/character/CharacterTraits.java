package com.isekai.character;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/** Applies persistent, non-stacking racial and class identity bonuses. */
@Mod.EventBusSubscriber(modid = "isekai")
public final class CharacterTraits {
    private static final String ROOT = "isekai_character";
    private static final UUID HUMAN_LUCK = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000001");
    private static final UUID ELF_SPEED = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000002");
    private static final UUID DWARF_ARMOR = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000003");
    private static final UUID DEMON_DAMAGE = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000004");
    private static final UUID WARRIOR_DAMAGE = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000005");
    private static final UUID WARRIOR_HEALTH = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000006");
    private static final UUID MAGE_SPEED = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000007");
    private static final UUID RANGER_SPEED = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000008");
    private static final UUID RANGER_ATTACK_SPEED = UUID.fromString("6e58a2d1-9a9d-4dcb-9a0c-000000000009");

    private CharacterTraits() {}

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) apply(player);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) apply(player);
    }

    public static void apply(ServerPlayer player) {
        CompoundTag data = player.getPersistentData().getCompound(ROOT);
        String race = data.getString("race");
        String characterClass = data.getString("class");

        set(player, Attributes.LUCK, HUMAN_LUCK, "ISEKAI Human Fortune", race.equals("human") ? 1.0D : 0.0D);
        set(player, Attributes.MOVEMENT_SPEED, ELF_SPEED, "ISEKAI Elven Grace", race.equals("elf") ? 0.0015D : 0.0D);
        set(player, Attributes.ARMOR, DWARF_ARMOR, "ISEKAI Dwarven Resilience", race.equals("dwarf") ? 2.0D : 0.0D);
        set(player, Attributes.ATTACK_DAMAGE, DEMON_DAMAGE, "ISEKAI Demonic Might", race.equals("demonkin") ? 1.0D : 0.0D);

        set(player, Attributes.ATTACK_DAMAGE, WARRIOR_DAMAGE, "ISEKAI Warrior Training", characterClass.equals("warrior") ? 1.5D : 0.0D);
        set(player, Attributes.MAX_HEALTH, WARRIOR_HEALTH, "ISEKAI Warrior Vitality", characterClass.equals("warrior") ? 4.0D : 0.0D);
        set(player, Attributes.MOVEMENT_SPEED, MAGE_SPEED, "ISEKAI Mage Focus", characterClass.equals("mage") ? 0.001D : 0.0D);
        set(player, Attributes.MOVEMENT_SPEED, RANGER_SPEED, "ISEKAI Ranger Mobility", characterClass.equals("ranger") ? 0.002D : 0.0D);
        set(player, Attributes.ATTACK_SPEED, RANGER_ATTACK_SPEED, "ISEKAI Ranger Reflexes", characterClass.equals("ranger") ? 0.15D : 0.0D);
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    private static void set(ServerPlayer player, Attribute attribute, UUID id, String name, double amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) return;
        instance.removeModifier(id);
        if (amount != 0.0D) {
            instance.addPermanentModifier(new AttributeModifier(id, name, amount, AttributeModifier.Operation.ADDITION));
        }
    }
}
