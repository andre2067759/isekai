package com.isekai.progression;

import com.isekai.entity.AetherSentinel;
import com.isekai.entity.AetherStalker;
import com.isekai.entity.AetherBrute;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Server-authoritative RPG progression and primary character attributes.
 * All values are stored in persistent player NBT and are independent of client state.
 */
@Mod.EventBusSubscriber(modid = "isekai")
public final class ProgressionEvents {
    private static final String ROOT_KEY = "isekai_progression";
    private static final String LEVEL_KEY = "level";
    private static final String EXPERIENCE_KEY = "experience";
    private static final String POINTS_KEY = "stat_points";
    private static final String STRENGTH_KEY = "strength";
    private static final String VITALITY_KEY = "vitality";
    private static final String AGILITY_KEY = "agility";
    private static final String INTELLIGENCE_KEY = "intelligence";
    private static final String SPIRIT_KEY = "spirit";
    private static final String MANA_KEY = "mana";
    private static final double BASE_MANA = 100.0D;

    private static final int STARTING_LEVEL = 1;
    private static final int MAX_LEVEL = 100;
    private static final int MAX_STAT = 50;

    private static final UUID STRENGTH_MODIFIER = UUID.fromString("9e58a2d1-9a9d-4dcb-9a0c-000000000001");
    private static final UUID VITALITY_MODIFIER = UUID.fromString("9e58a2d1-9a9d-4dcb-9a0c-000000000002");
    private static final UUID AGILITY_MODIFIER = UUID.fromString("9e58a2d1-9a9d-4dcb-9a0c-000000000003");

    private ProgressionEvents() {
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (event.getEntity() instanceof AetherBrute) {
            awardExperience(player, 120);
        } else if (event.getEntity() instanceof AetherStalker) {
            awardExperience(player, 28);
        } else if (event.getEntity() instanceof AetherSentinel) {
            awardExperience(player, 45);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player replacement = event.getEntity();
        CompoundTag oldProgress = original.getPersistentData().getCompound(ROOT_KEY);
        replacement.getPersistentData().put(ROOT_KEY, oldProgress.copy());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % 20 != 0) {
            return;
        }

        CompoundTag progress = getProgress(player);
        double maximum = maxMana(progress);
        double current = Math.max(0.0D, Math.min(maximum, progress.getDouble(MANA_KEY)));
        if (current < maximum) {
            double regeneration = 1.0D + progress.getInt(SPIRIT_KEY) * 0.1D;
            progress.putDouble(MANA_KEY, Math.min(maximum, current + regeneration));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyStatModifiers(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            applyStatModifiers(player);
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("status")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompoundTag progress = getProgress(player);
                    int level = progress.getInt(LEVEL_KEY);
                    int experience = progress.getInt(EXPERIENCE_KEY);
                    int required = experienceRequired(level);
                    int points = progress.getInt(POINTS_KEY);
                    double mana = progress.getDouble(MANA_KEY);
                    double maxMana = maxMana(progress);

                    context.getSource().sendSuccess(() -> Component.literal(
                        "§bISEKAI §7| §fLivello " + level + " §8- §fEsperienza "
                            + experience + "/" + required + " §8- §ePunti statistica: " + points
                    ), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                        "§cForza: " + progress.getInt(STRENGTH_KEY)
                            + " §aVitalità: " + progress.getInt(VITALITY_KEY)
                            + " §bAgilità: " + progress.getInt(AGILITY_KEY)
                    ), false);
                    context.getSource().sendSuccess(() -> Component.literal(
                        "§bMana: " + String.format(java.util.Locale.ROOT, "%.1f/%.1f", mana, maxMana)
                            + " §8- §dIntelligenza: " + progress.getInt(INTELLIGENCE_KEY)
                            + " §5Spirito: " + progress.getInt(SPIRIT_KEY)
                            + " §7(Massimo per statistica: " + MAX_STAT + ")"
                    ), false);
                    return 1;
                }))
            .then(Commands.literal("mana")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompoundTag progress = getProgress(player);
                    context.getSource().sendSuccess(() -> Component.literal(
                        "§bISEKAI Mana §7| §f"
                            + String.format(java.util.Locale.ROOT, "%.1f / %.1f", progress.getDouble(MANA_KEY), maxMana(progress))
                    ), false);
                    return 1;
                }))
            .then(Commands.literal("allocate")
                .then(Commands.argument("stat", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        java.util.List.of("strength", "vitality", "agility", "intelligence", "spirit"), builder))
                    .then(Commands.argument("points", IntegerArgumentType.integer(1, MAX_STAT))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            String stat = StringArgumentType.getString(context, "stat").toLowerCase();
                            int amount = IntegerArgumentType.getInteger(context, "points");
                            return allocatePoints(player, stat, amount);
                        })))));
    }

    public static void awardExperience(ServerPlayer player, int amount) {
        if (amount <= 0) {
            return;
        }

        CompoundTag progress = getProgress(player);
        int level = progress.getInt(LEVEL_KEY);
        if (level >= MAX_LEVEL) {
            return;
        }

        int experience = progress.getInt(EXPERIENCE_KEY) + amount;
        int gainedLevels = 0;

        while (level < MAX_LEVEL && experience >= experienceRequired(level)) {
            experience -= experienceRequired(level);
            level++;
            gainedLevels++;
        }

        progress.putInt(LEVEL_KEY, level);
        progress.putInt(EXPERIENCE_KEY, level >= MAX_LEVEL ? 0 : experience);

        if (gainedLevels > 0) {
            progress.putInt(POINTS_KEY, progress.getInt(POINTS_KEY) + gainedLevels);
        }

        player.sendSystemMessage(Component.literal("§b[ISEKAI] §f+" + amount + " esperienza"));
        if (gainedLevels > 0) {
            player.sendSystemMessage(Component.literal(
                "§6✦ §eLivello aumentato! §fOra sei al livello " + level
                    + ". §a+" + gainedLevels + " punto/i statistica."
            ));
        }
    }

    public static boolean consumeMana(ServerPlayer player, double amount) {
        if (amount <= 0.0D) {
            return true;
        }
        CompoundTag progress = getProgress(player);
        double current = progress.getDouble(MANA_KEY);
        if (current < amount) {
            return false;
        }
        progress.putDouble(MANA_KEY, current - amount);
        return true;
    }

    public static double getCurrentMana(ServerPlayer player) {
        return getProgress(player).getDouble(MANA_KEY);
    }

    public static double getMaximumMana(ServerPlayer player) {
        return maxMana(getProgress(player));
    }

    private static int allocatePoints(ServerPlayer player, String stat, int amount) {
        String key = switch (stat) {
            case "strength" -> STRENGTH_KEY;
            case "vitality" -> VITALITY_KEY;
            case "agility" -> AGILITY_KEY;
            case "intelligence" -> INTELLIGENCE_KEY;
            case "spirit" -> SPIRIT_KEY;
            default -> null;
        };

        if (key == null) {
            player.sendSystemMessage(Component.literal(
                "§cStatistica sconosciuta. Usa: strength, vitality, agility, intelligence, spirit."
            ));
            return 0;
        }

        CompoundTag progress = getProgress(player);
        int current = progress.getInt(key);
        int available = progress.getInt(POINTS_KEY);

        if (current + amount > MAX_STAT) {
            player.sendSystemMessage(Component.literal("§cQuesta statistica ha un limite di " + MAX_STAT + " punti."));
            return 0;
        }
        if (amount > available) {
            player.sendSystemMessage(Component.literal("§cPunti insufficienti. Disponibili: " + available + "."));
            return 0;
        }

        progress.putInt(key, current + amount);
        progress.putInt(POINTS_KEY, available - amount);
        applyStatModifiers(player);
        progress.putDouble(MANA_KEY, Math.min(progress.getDouble(MANA_KEY), maxMana(progress)));

        player.sendSystemMessage(Component.literal(
            "§b[ISEKAI] §f" + statName(stat) + " aumentata a " + (current + amount)
                + ". §ePunti rimanenti: " + (available - amount) + "."
        ));
        return 1;
    }

    private static void applyStatModifiers(ServerPlayer player) {
        CompoundTag progress = getProgress(player);
        applyModifier(player, Attributes.ATTACK_DAMAGE, STRENGTH_MODIFIER,
            "ISEKAI Strength", progress.getInt(STRENGTH_KEY) * 0.5D);
        applyModifier(player, Attributes.MAX_HEALTH, VITALITY_MODIFIER,
            "ISEKAI Vitality", progress.getInt(VITALITY_KEY) * 2.0D);
        applyModifier(player, Attributes.MOVEMENT_SPEED, AGILITY_MODIFIER,
            "ISEKAI Agility", progress.getInt(AGILITY_KEY) * 0.0015D);
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    private static void applyModifier(ServerPlayer player, net.minecraft.world.entity.ai.attributes.Attribute attribute,
                                      UUID id, String name, double amount) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance == null) {
            return;
        }

        instance.removeModifier(id);
        if (amount > 0.0D) {
            instance.addPermanentModifier(new AttributeModifier(
                id, name, amount, AttributeModifier.Operation.ADDITION
            ));
        }
    }

    private static String statName(String stat) {
        return switch (stat) {
            case "strength" -> "Forza";
            case "vitality" -> "Vitalità";
            case "agility" -> "Agilità";
            case "intelligence" -> "Intelligenza";
            case "spirit" -> "Spirito";
            default -> stat;
        };
    }

    private static CompoundTag getProgress(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            CompoundTag initial = new CompoundTag();
            initial.putInt(LEVEL_KEY, STARTING_LEVEL);
            initial.putInt(EXPERIENCE_KEY, 0);
            initial.putInt(POINTS_KEY, 0);
            persistentData.put(ROOT_KEY, initial);
        }

        CompoundTag progress = persistentData.getCompound(ROOT_KEY);
        progress.putInt(LEVEL_KEY, Math.max(STARTING_LEVEL, progress.getInt(LEVEL_KEY)));
        progress.putInt(EXPERIENCE_KEY, Math.max(0, progress.getInt(EXPERIENCE_KEY)));
        progress.putInt(POINTS_KEY, Math.max(0, progress.getInt(POINTS_KEY)));
        progress.putInt(STRENGTH_KEY, Math.max(0, Math.min(MAX_STAT, progress.getInt(STRENGTH_KEY))));
        progress.putInt(VITALITY_KEY, Math.max(0, Math.min(MAX_STAT, progress.getInt(VITALITY_KEY))));
        progress.putInt(AGILITY_KEY, Math.max(0, Math.min(MAX_STAT, progress.getInt(AGILITY_KEY))));
        progress.putInt(INTELLIGENCE_KEY, Math.max(0, Math.min(MAX_STAT, progress.getInt(INTELLIGENCE_KEY))));
        progress.putInt(SPIRIT_KEY, Math.max(0, Math.min(MAX_STAT, progress.getInt(SPIRIT_KEY))));
        if (!progress.contains(MANA_KEY)) {
            progress.putDouble(MANA_KEY, BASE_MANA);
        }
        progress.putDouble(MANA_KEY, Math.max(0.0D, Math.min(maxMana(progress), progress.getDouble(MANA_KEY))));
        return progress;
    }

    private static double maxMana(CompoundTag progress) {
        return BASE_MANA + progress.getInt(INTELLIGENCE_KEY) * 5.0D + progress.getInt(SPIRIT_KEY) * 10.0D;
    }

    private static int experienceRequired(int level) {
        return 100 + Math.max(0, level - 1) * 50;
    }
}
