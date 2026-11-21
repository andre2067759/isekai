package com.isekai.progression;

import com.isekai.entity.AetherSentinel;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Server-authoritative first layer of ISEKAI's RPG progression.
 * Progress is stored in the player's persistent NBT, so it survives relogs
 * and is copied when the player entity is recreated.
 */
@Mod.EventBusSubscriber(modid = "isekai")
public final class ProgressionEvents {
    private static final String ROOT_KEY = "isekai_progression";
    private static final String LEVEL_KEY = "level";
    private static final String EXPERIENCE_KEY = "experience";
    private static final int STARTING_LEVEL = 1;
    private static final int MAX_LEVEL = 100;

    private ProgressionEvents() {
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // First authored reward: the Aether Sentinel is the introductory elite enemy.
        if (event.getEntity() instanceof AetherSentinel) {
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
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("status")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompoundTag progress = getProgress(player);
                    int level = progress.getInt(LEVEL_KEY);
                    int experience = progress.getInt(EXPERIENCE_KEY);
                    int required = experienceRequired(level);
                    context.getSource().sendSuccess(
                        () -> Component.literal("§bISEKAI §7| §fLivello " + level
                            + " §8- §fEsperienza " + experience + "/" + required
                            + (level >= MAX_LEVEL ? " §6(LIVELLO MASSIMO)" : "")),
                        false
                    );
                    return 1;
                })));
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
        boolean leveledUp = false;

        while (level < MAX_LEVEL && experience >= experienceRequired(level)) {
            experience -= experienceRequired(level);
            level++;
            leveledUp = true;
        }

        progress.putInt(LEVEL_KEY, level);
        progress.putInt(EXPERIENCE_KEY, level >= MAX_LEVEL ? 0 : experience);

        player.sendSystemMessage(Component.literal("§b[ISEKAI] §f+" + amount + " esperienza"));
        if (leveledUp) {
            player.sendSystemMessage(Component.literal("§6✦ §eLivello aumentato! §fOra sei al livello " + level + "."));
        }
    }

    private static CompoundTag getProgress(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(ROOT_KEY, Tag.TAG_COMPOUND)) {
            CompoundTag initial = new CompoundTag();
            initial.putInt(LEVEL_KEY, STARTING_LEVEL);
            initial.putInt(EXPERIENCE_KEY, 0);
            persistentData.put(ROOT_KEY, initial);
        }

        CompoundTag progress = persistentData.getCompound(ROOT_KEY);
        if (!progress.contains(LEVEL_KEY)) {
            progress.putInt(LEVEL_KEY, STARTING_LEVEL);
        }
        if (!progress.contains(EXPERIENCE_KEY)) {
            progress.putInt(EXPERIENCE_KEY, 0);
        }
        return progress;
    }

    private static int experienceRequired(int level) {
        return 100 + Math.max(0, level - 1) * 50;
    }
}
