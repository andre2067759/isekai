package com.isekai.character;

import com.isekai.entity.AetherSentinel;
import com.isekai.progression.ProgressionEvents;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Locale;

/**
 * First playable slice for character identity, a repeatable world activity,
 * a small economy and one active combat skill.
 */
@Mod.EventBusSubscriber(modid = "isekai")
public final class CharacterSystemsEvents {
    private static final String ROOT = "isekai_character";
    private static final String RACE = "race";
    private static final String CLASS = "class";
    private static final String COINS = "coins";
    private static final String SENTINEL_KILLS = "sentinel_kills";
    private static final String FIRST_HUNT_CLAIMED = "first_hunt_claimed";
    private static final String BURST_COOLDOWN = "aether_burst_cooldown";

    private static final int FIRST_HUNT_TARGET = 3;
    private static final int FIRST_HUNT_COINS = 75;
    private static final int FIRST_HUNT_XP = 150;
    private static final double BURST_MANA_COST = 20.0D;
    private static final float BURST_DAMAGE = 6.0F;

    private CharacterSystemsEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("profile")
                .executes(context -> showProfile(context.getSource().getPlayerOrException())))
            .then(Commands.literal("race")
                .then(Commands.literal("choose")
                    .then(Commands.argument("race", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                            List.of("human", "elf", "dwarf", "demonkin"), builder))
                        .executes(context -> chooseRace(
                            context.getSource().getPlayerOrException(),
                            StringArgumentType.getString(context, "race"))))))
            .then(Commands.literal("class")
                .then(Commands.literal("choose")
                    .then(Commands.argument("class", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                            List.of("warrior", "mage", "ranger"), builder))
                        .executes(context -> chooseClass(
                            context.getSource().getPlayerOrException(),
                            StringArgumentType.getString(context, "class"))))))
            .then(Commands.literal("quest")
                .executes(context -> showQuest(context.getSource().getPlayerOrException())))
            .then(Commands.literal("wallet")
                .executes(context -> showWallet(context.getSource().getPlayerOrException())))
            .then(Commands.literal("skill")
                .then(Commands.literal("list")
                    .executes(context -> showSkills(context.getSource().getPlayerOrException())))
                .then(Commands.literal("use")
                    .then(Commands.literal("aether_burst")
                        .executes(context -> useAetherBurst(context.getSource().getPlayerOrException()))))));
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)
            || !(event.getEntity() instanceof AetherSentinel)) {
            return;
        }

        CompoundTag data = getData(player);
        int kills = data.getInt(SENTINEL_KILLS) + 1;
        data.putInt(SENTINEL_KILLS, kills);

        if (!data.getBoolean(FIRST_HUNT_CLAIMED) && kills >= FIRST_HUNT_TARGET) {
            data.putBoolean(FIRST_HUNT_CLAIMED, true);
            data.putInt(COINS, data.getInt(COINS) + FIRST_HUNT_COINS);
            player.sendSystemMessage(Component.literal(
                "§6[ISEKAI] §eMissione completata: Caccia alle Sentinelle! §f+"
                    + FIRST_HUNT_COINS + " monete e +" + FIRST_HUNT_XP + " PE."
            ));
            ProgressionEvents.awardExperience(player, FIRST_HUNT_XP);
        } else if (!data.getBoolean(FIRST_HUNT_CLAIMED)) {
            player.sendSystemMessage(Component.literal(
                "§b[ISEKAI] §fCaccia alle Sentinelle: " + kills + "/" + FIRST_HUNT_TARGET
            ));
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            getData(player);
        }
    }

    private static int showProfile(ServerPlayer player) {
        CompoundTag data = getData(player);
        player.sendSystemMessage(Component.literal("§b§lISEKAI §7— Profilo del personaggio"));
        player.sendSystemMessage(Component.literal("§fRazza: §d" + displayRace(data.getString(RACE))));
        player.sendSystemMessage(Component.literal("§fClasse: §6" + displayClass(data.getString(CLASS))));
        player.sendSystemMessage(Component.literal("§fLivello e statistiche: §7/isekai status"));
        return Command.SINGLE_SUCCESS;
    }

    private static int chooseRace(ServerPlayer player, String requested) {
        String race = requested.toLowerCase(Locale.ROOT);
        if (!List.of("human", "elf", "dwarf", "demonkin").contains(race)) {
            player.sendSystemMessage(Component.literal("§cRazza non valida."));
            return 0;
        }

        CompoundTag data = getData(player);
        if (!data.getString(RACE).isEmpty()) {
            player.sendSystemMessage(Component.literal(
                "§cHai già scelto la razza " + displayRace(data.getString(RACE)) + ". La personalizzazione avanzata arriverà in seguito."
            ));
            return 0;
        }

        data.putString(RACE, race);
        player.sendSystemMessage(Component.literal(
            "§d[ISEKAI] §fRazza scelta: " + displayRace(race) + "."
        ));
        player.sendSystemMessage(Component.literal(
            "§7La scelta è registrata nel profilo; evoluzioni e tratti razziali saranno ampliati nelle prossime versioni."
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static int chooseClass(ServerPlayer player, String requested) {
        String characterClass = requested.toLowerCase(Locale.ROOT);
        if (!List.of("warrior", "mage", "ranger").contains(characterClass)) {
            player.sendSystemMessage(Component.literal("§cClasse non valida."));
            return 0;
        }

        CompoundTag data = getData(player);
        if (!data.getString(CLASS).isEmpty()) {
            player.sendSystemMessage(Component.literal(
                "§cHai già scelto la classe " + displayClass(data.getString(CLASS)) + ". Le specializzazioni arriveranno in seguito."
            ));
            return 0;
        }

        data.putString(CLASS, characterClass);
        player.sendSystemMessage(Component.literal(
            "§6[ISEKAI] §fClasse scelta: " + displayClass(characterClass) + "."
        ));
        player.sendSystemMessage(Component.literal(
            "§7Albero abilità e specializzazioni saranno ampliati nelle prossime versioni."
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static int showQuest(ServerPlayer player) {
        CompoundTag data = getData(player);
        int kills = Math.min(FIRST_HUNT_TARGET, data.getInt(SENTINEL_KILLS));
        boolean claimed = data.getBoolean(FIRST_HUNT_CLAIMED);
        player.sendSystemMessage(Component.literal("§6§lMISSIONE: Caccia alle Sentinelle"));
        player.sendSystemMessage(Component.literal(
            "§fSconfiggi Sentinelle dell'Aether: §b" + kills + "/" + FIRST_HUNT_TARGET
        ));
        player.sendSystemMessage(Component.literal(claimed
            ? "§aCompletata — ricompensa ricevuta."
            : "§7Ricompensa: §e" + FIRST_HUNT_COINS + " monete §7e §b" + FIRST_HUNT_XP + " PE."));
        return Command.SINGLE_SUCCESS;
    }

    private static int showWallet(ServerPlayer player) {
        int coins = getData(player).getInt(COINS);
        player.sendSystemMessage(Component.literal("§6[ISEKAI] §fMonete: §e" + coins));
        return Command.SINGLE_SUCCESS;
    }

    private static int showSkills(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§b§lABILITÀ ISEKAI"));
        player.sendSystemMessage(Component.literal("§dDardo di Aether §7— /isekai cast aether_bolt"));
        player.sendSystemMessage(Component.literal("§bEsplosione di Aether §7— /isekai skill use aether_burst"));
        player.sendSystemMessage(Component.literal("§7L'albero abilità, i requisiti di classe e i livelli abilità saranno ampliati."));
        return Command.SINGLE_SUCCESS;
    }

    private static int useAetherBurst(ServerPlayer player) {
        long now = player.level().getGameTime();
        CompoundTag data = getData(player);
        long nextUse = data.getLong(BURST_COOLDOWN);
        if (now < nextUse) {
            long seconds = Math.max(1L, (nextUse - now + 19L) / 20L);
            player.sendSystemMessage(Component.literal("§cAbilità in ricarica: " + seconds + " s."));
            return 0;
        }
        if (ProgressionEvents.getCurrentMana(player) < BURST_MANA_COST) {
            player.sendSystemMessage(Component.literal("§cMana insufficiente: servono " + (int) BURST_MANA_COST + "."));
            return 0;
        }

        AABB area = player.getBoundingBox().inflate(4.0D);
        List<LivingEntity> targets = player.level().getEntitiesOfClass(
            LivingEntity.class, area,
            entity -> entity != player && entity.isAlive() && !entity.isAlliedTo(player)
        );
        if (targets.isEmpty()) {
            player.sendSystemMessage(Component.literal("§7Nessun nemico vicino."));
            return 0;
        }

        if (!ProgressionEvents.consumeMana(player, BURST_MANA_COST)) {
            return 0;
        }
        for (LivingEntity target : targets) {
            target.hurt(player.damageSources().magic(), BURST_DAMAGE);
            target.knockback(0.65D, player.getX() - target.getX(), player.getZ() - target.getZ());
        }
        data.putLong(BURST_COOLDOWN, now + 160L);

        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.END_ROD,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                36, 1.2D, 0.7D, 1.2D, 0.08D);
        }
        player.sendSystemMessage(Component.literal(
            "§b[ISEKAI] §fEsplosione di Aether! §d-" + (int) BURST_MANA_COST
                + " mana §7| §c" + targets.size() + " bersagli coinvolti."
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static CompoundTag getData(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    private static String displayRace(String race) {
        return switch (race) {
            case "human" -> "Umano";
            case "elf" -> "Elfo";
            case "dwarf" -> "Nano";
            case "demonkin" -> "Demone";
            default -> "Non scelta";
        };
    }

    private static String displayClass(String characterClass) {
        return switch (characterClass) {
            case "warrior" -> "Guerriero";
            case "mage" -> "Mago";
            case "ranger" -> "Arciere";
            default -> "Non scelta";
        };
    }
}
