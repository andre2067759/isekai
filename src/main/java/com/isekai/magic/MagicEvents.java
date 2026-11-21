package com.isekai.magic;

import com.isekai.progression.ProgressionEvents;
import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * First playable spell implementation. The command is an interim activation
 * method; a keybind and spell-selection interface can replace it later.
 */
@Mod.EventBusSubscriber(modid = "isekai")
public final class MagicEvents {
    private static final String COOLDOWN_KEY = "isekai_aether_bolt_cooldown";
    private static final double RANGE = 18.0D;
    private static final double MANA_COST = 25.0D;
    private static final long COOLDOWN_TICKS = 40L;
    private static final float DAMAGE = 12.0F;

    private MagicEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("cast")
                .then(Commands.literal("aether_bolt")
                    .executes(context -> castAetherBolt(context.getSource().getPlayerOrException())))
                .then(Commands.literal("void_lance")
                    .executes(context -> castVoidLance(context.getSource().getPlayerOrException())))
                .then(Commands.literal("solar_burst")
                    .executes(context -> castSolarBurst(context.getSource().getPlayerOrException())))
                .then(Commands.literal("moon_step")
                    .executes(context -> castMoonStep(context.getSource().getPlayerOrException())))));
    }

    private static final String SELECTED_SPELL_KEY = "isekai_selected_spell";

    public static void cycleSelectedSpell(ServerPlayer player) {
        int selected = (player.getPersistentData().getInt(SELECTED_SPELL_KEY) + 1) % 4;
        player.getPersistentData().putInt(SELECTED_SPELL_KEY, selected);
        String[] names = {"Dardo di Aether", "Lancia del Vuoto", "Esplosione Solare", "Passo Lunare"};
        player.sendSystemMessage(Component.literal("§b[ISEKAI] §fIncantesimo selezionato: §d" + names[selected]
            + " §7(" + (selected + 1) + "/4)"));
    }

    public static void castSelectedSpell(ServerPlayer player) {
        switch (player.getPersistentData().getInt(SELECTED_SPELL_KEY)) {
            case 1 -> castVoidLance(player);
            case 2 -> castSolarBurst(player);
            case 3 -> castMoonStep(player);
            default -> castAetherBolt(player);
        }
    }

    private static int castAetherBolt(ServerPlayer player) {
        long now = player.level().getGameTime();
        long nextCast = player.getPersistentData().getLong(COOLDOWN_KEY);
        if (now < nextCast) {
            long remainingSeconds = Math.max(1L, (nextCast - now + 19L) / 20L);
            player.sendSystemMessage(Component.literal(
                "§b[ISEKAI] §cL'incantesimo è in ricarica: " + remainingSeconds + " s."
            ));
            return 0;
        }

        if (ProgressionEvents.getCurrentMana(player) < MANA_COST) {
            player.sendSystemMessage(Component.literal(
                "§b[ISEKAI] §cMana insufficiente. Richiesti: " + (int) MANA_COST + "."
            ));
            return 0;
        }

        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle();
        Vec3 end = start.add(direction.scale(RANGE));

        HitResult blockHit = player.level().clip(new ClipContext(
            start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player
        ));
        if (blockHit.getType() == HitResult.Type.BLOCK) {
            end = blockHit.getLocation();
        }

        AABB searchBox = player.getBoundingBox()
            .expandTowards(end.subtract(start))
            .inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
            player,
            start,
            end,
            searchBox,
            target -> target != player && target instanceof LivingEntity
                && target.isPickable() && !target.isSpectator(),
            start.distanceToSqr(end)
        );

        if (entityHit == null) {
            player.sendSystemMessage(Component.literal("§b[ISEKAI] §7Nessun bersaglio nell'area dell'incantesimo."));
            return 0;
        }

        LivingEntity target = (LivingEntity) entityHit.getEntity();
        if (!target.hurt(player.damageSources().magic(), DAMAGE)) {
            player.sendSystemMessage(Component.literal("§b[ISEKAI] §cIl bersaglio è immune all'incantesimo."));
            return 0;
        }

        ProgressionEvents.consumeMana(player, MANA_COST);
        player.getPersistentData().putLong(COOLDOWN_KEY, now + COOLDOWN_TICKS);

        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                ParticleTypes.END_ROD,
                target.getX(),
                target.getY() + target.getBbHeight() * 0.5D,
                target.getZ(),
                18,
                0.25D,
                0.25D,
                0.25D,
                0.03D
            );
        }

        player.sendSystemMessage(Component.literal(
            "§b[ISEKAI] §fDardo di Aether! §d-" + (int) MANA_COST + " mana §7| §c"
                + (int) DAMAGE + " danni magici."
        ));
        return Command.SINGLE_SUCCESS;
    }

    private static boolean spellReady(ServerPlayer player, String key, double mana, long cooldown) {
        long now = player.level().getGameTime();
        long next = player.getPersistentData().getLong(key);
        if (now < next) {
            player.sendSystemMessage(Component.literal("§b[ISEKAI] §cIncantesimo in ricarica: "
                + Math.max(1L, (next - now + 19L) / 20L) + " s."));
            return false;
        }
        if (ProgressionEvents.getCurrentMana(player) < mana) {
            player.sendSystemMessage(Component.literal("§b[ISEKAI] §cMana insufficiente. Richiesti: " + (int) mana + "."));
            return false;
        }
        return true;
    }

    private static void finishSpell(ServerPlayer player, String key, double mana, long cooldown) {
        ProgressionEvents.consumeMana(player, mana);
        player.getPersistentData().putLong(key, player.level().getGameTime() + cooldown);
    }

    private static int castVoidLance(ServerPlayer player) {
        final String key = "isekai_void_lance_cooldown";
        if (!spellReady(player, key, 35.0D, 100L)) return 0;
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getLookAngle().scale(24.0D));
        HitResult wall = player.level().clip(new ClipContext(start, end,
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (wall.getType() == HitResult.Type.BLOCK) end = wall.getLocation();
        AABB box = player.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.1D);
        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, start, end, box,
            target -> target != player && target instanceof LivingEntity && target.isPickable() && !target.isSpectator(),
            start.distanceToSqr(end));
        if (hit == null || !hit.getEntity().hurt(player.damageSources().magic(), 22.0F)) {
            player.sendSystemMessage(Component.literal("§5[ISEKAI] §7La Lancia del Vuoto non ha colpito alcun bersaglio."));
            return 0;
        }
        finishSpell(player, key, 35.0D, 100L);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.PORTAL, hit.getLocation().x, hit.getLocation().y,
                hit.getLocation().z, 36, 0.35D, 0.35D, 0.35D, 0.08D);
        }
        player.sendSystemMessage(Component.literal("§5[ISEKAI] §dLancia del Vuoto! §c22 danni §7| §b-35 mana."));
        return Command.SINGLE_SUCCESS;
    }

    private static int castSolarBurst(ServerPlayer player) {
        final String key = "isekai_solar_burst_cooldown";
        if (!spellReady(player, key, 45.0D, 140L)) return 0;
        Vec3 center = player.position().add(player.getLookAngle().scale(4.0D));
        AABB area = AABB.ofSize(center, 8.0D, 5.0D, 8.0D);
        java.util.List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, area,
            target -> target != player && target.isAlive() && !target.isSpectator());
        int hits = 0;
        for (LivingEntity target : targets) {
            if (target.hurt(player.damageSources().magic(), 16.0F)) {
                target.setSecondsOnFire(3);
                hits++;
            }
        }
        if (hits == 0) {
            player.sendSystemMessage(Component.literal("§6[ISEKAI] §7Nessun nemico raggiunto dall'Esplosione Solare."));
            return 0;
        }
        finishSpell(player, key, 45.0D, 140L);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.FLAME, center.x, center.y, center.z, 55, 2.0D, 1.0D, 2.0D, 0.04D);
            level.sendParticles(ParticleTypes.END_ROD, center.x, center.y, center.z, 24, 1.5D, 0.8D, 1.5D, 0.03D);
        }
        player.sendSystemMessage(Component.literal("§6[ISEKAI] §eEsplosione Solare! §c" + hits
            + " bersagli colpiti §7| §b-45 mana."));
        return Command.SINGLE_SUCCESS;
    }

    private static int castMoonStep(ServerPlayer player) {
        final String key = "isekai_moon_step_cooldown";
        if (!spellReady(player, key, 20.0D, 100L)) return 0;
        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 end = start.add(direction.scale(8.0D));
        HitResult wall = player.level().clip(new ClipContext(start, end,
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if (wall.getType() == HitResult.Type.BLOCK) end = wall.getLocation().subtract(direction.scale(1.2D));
        Vec3 destination = new Vec3(end.x, player.getY(), end.z);
        Vec3 movement = destination.subtract(player.position());
        if (movement.lengthSqr() < 4.0D || !player.level().noCollision(player, player.getBoundingBox().move(movement))) {
            player.sendSystemMessage(Component.literal("§d[ISEKAI] §7Non c'è spazio sicuro per il Passo Lunare."));
            return 0;
        }
        player.teleportTo(destination.x, destination.y, destination.z);
        finishSpell(player, key, 20.0D, 100L);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.END_ROD, destination.x, destination.y + 1.0D,
                destination.z, 28, 0.35D, 0.6D, 0.35D, 0.04D);
        }
        player.sendSystemMessage(Component.literal("§d[ISEKAI] §fPasso Lunare! §b-20 mana."));
        return Command.SINGLE_SUCCESS;
    }
}
