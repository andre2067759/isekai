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
import net.minecraft.world.phys.ClipContext;
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
                    .executes(context -> castAetherBolt(context.getSource().getPlayerOrException())))));
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
}
