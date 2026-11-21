package com.isekai.character;

import com.isekai.progression.ProgressionEvents;
import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/** Three class-bound active skills, with persistent cooldowns and mana costs. */
@Mod.EventBusSubscriber(modid = "isekai")
public final class ClassSkillEvents {
    private static final String ROOT = "isekai_character";
    private static final String COOLDOWN = "class_skill_cooldown";
    private static final double MANA_COST = 18.0D;
    private static final long COOLDOWN_TICKS = 100L;

    private ClassSkillEvents() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("skill")
                .then(Commands.literal("use")
                    .then(Commands.literal("warrior_cleave").executes(c -> use(c.getSource().getPlayerOrException(), "warrior", "warrior_cleave")))
                    .then(Commands.literal("arcane_pulse").executes(c -> use(c.getSource().getPlayerOrException(), "mage", "arcane_pulse")))
                    .then(Commands.literal("ranger_volley").executes(c -> use(c.getSource().getPlayerOrException(), "ranger", "ranger_volley"))))));
    }

    private static int use(ServerPlayer player, String requiredClass, String skill) {
        CompoundTag character = player.getPersistentData().getCompound(ROOT);
        if (!requiredClass.equals(character.getString("class"))) {
            player.sendSystemMessage(Component.literal("§cQuesta abilità richiede la classe " + requiredClass + "."));
            return 0;
        }
        long now = player.level().getGameTime();
        long next = character.getLong(COOLDOWN);
        if (now < next) {
            player.sendSystemMessage(Component.literal("§cAbilità in ricarica: " + Math.max(1L, (next - now + 19L) / 20L) + " s."));
            return 0;
        }
        if (ProgressionEvents.getCurrentMana(player) < MANA_COST) {
            player.sendSystemMessage(Component.literal("§cMana insufficiente: servono " + (int) MANA_COST + "."));
            return 0;
        }

        int hits = 0;
        float damage = switch (skill) {
            case "warrior_cleave" -> 9.0F;
            case "arcane_pulse" -> 8.0F;
            default -> 11.0F;
        };
        if (skill.equals("ranger_volley")) {
            Vec3 eye = player.getEyePosition();
            Vec3 look = player.getLookAngle().normalize();
            List<LivingEntity> candidates = player.level().getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(14.0D), e -> e != player && e.isAlive() && !e.isAlliedTo(player));
            LivingEntity best = null;
            double bestDistance = 14.1D;
            for (LivingEntity candidate : candidates) {
                Vec3 offset = candidate.getBoundingBox().getCenter().subtract(eye);
                double distance = offset.length();
                if (distance <= 14.0D && look.dot(offset.normalize()) > 0.94D && distance < bestDistance) {
                    best = candidate;
                    bestDistance = distance;
                }
            }
            if (best != null && best.hurt(player.damageSources().arrow(player, player), damage)) hits++;
            if (hits > 0) best.knockback(0.45D, player.getX() - best.getX(), player.getZ() - best.getZ());
        } else {
            double radius = skill.equals("warrior_cleave") ? 3.5D : 5.0D;
            AABB area = player.getBoundingBox().inflate(radius);
            List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class,
                area, e -> e != player && e.isAlive() && !e.isAlliedTo(player));
            for (LivingEntity target : targets) {
                if (target.hurt(player.damageSources().magic(), damage)) {
                    hits++;
                    target.knockback(skill.equals("warrior_cleave") ? 0.8D : 0.35D,
                        player.getX() - target.getX(), player.getZ() - target.getZ());
                }
            }
        }
        if (hits == 0) {
            player.sendSystemMessage(Component.literal("§7Nessun bersaglio valido: mana e ricarica non consumati."));
            return 0;
        }
        if (!ProgressionEvents.consumeMana(player, MANA_COST)) return 0;
        character.putLong(COOLDOWN, now + COOLDOWN_TICKS);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(skill.equals("arcane_pulse")
                    ? net.minecraft.core.particles.ParticleTypes.ENCHANT
                    : net.minecraft.core.particles.ParticleTypes.CRIT,
                player.getX(), player.getY() + 1.0D, player.getZ(),
                skill.equals("ranger_volley") ? 14 : 28, 0.8D, 0.6D, 0.8D, 0.08D);
        }
        player.sendSystemMessage(Component.literal("§b[ISEKAI] §f" + skill + " §7— §c" + hits
            + " bersagli colpiti §8| §d-" + (int) MANA_COST + " mana §8| §eRicarica: 5 s."));
        return Command.SINGLE_SUCCESS;
    }
}
