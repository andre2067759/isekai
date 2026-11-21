package com.isekai.character;

import com.isekai.registry.ModItems;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/** Small persistent-currency shop. Purchases are validated and charged server-side. */
@Mod.EventBusSubscriber(modid = "isekai")
public final class ShopEvents {
    private static final String ROOT = "isekai_character";
    private static final String COINS = "coins";
    private static final List<String> OFFERS = List.of(
        "aether_fruit", "aether_dust", "aether_shard", "aether_crystal",
        "aether_ingot", "sun_aether_ingot", "moon_aether_ingot",
        "aether_sword", "aether_bow"
    );

    private ShopEvents() {}

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("isekai")
            .then(Commands.literal("shop")
                .executes(context -> showShop(context.getSource().getPlayerOrException()))
                .then(Commands.literal("buy")
                    .then(Commands.argument("item", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(OFFERS, builder))
                        .executes(context -> buy(context.getSource().getPlayerOrException(),
                            StringArgumentType.getString(context, "item")))))));
    }

    private static int showShop(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("§6§lMERCANTE DI AETHER §7— /isekai shop buy <oggetto>"));
        player.sendSystemMessage(Component.literal("§bAether Fruit §7— 25 monete §8(aether_fruit)"));
        player.sendSystemMessage(Component.literal("§bPolvere di Aether §7— 10 monete §8(aether_dust)"));
        player.sendSystemMessage(Component.literal("§bScheggia di Aether §7— 15 monete §8(aether_shard)"));
        player.sendSystemMessage(Component.literal("§dCristallo di Aether §7— 60 monete §8(aether_crystal)"));
        player.sendSystemMessage(Component.literal("§fLingotto Aether §7— 90 monete §8(aether_ingot)"));
        player.sendSystemMessage(Component.literal("§6Lingotto Solare §7— 130 monete §8(sun_aether_ingot)"));
        player.sendSystemMessage(Component.literal("§9Lingotto Lunare §7— 130 monete §8(moon_aether_ingot)"));
        player.sendSystemMessage(Component.literal("§bSpada Aether §7— 300 monete §8(aether_sword)"));
        player.sendSystemMessage(Component.literal("§dArco Aether §7— 250 monete §8(aether_bow)"));
        player.sendSystemMessage(Component.literal("§7Saldo: §e" + getData(player).getInt(COINS) + " monete"));
        return Command.SINGLE_SUCCESS;
    }

    private static int buy(ServerPlayer player, String id) {
        Offer offer = offer(id);
        if (offer == null) {
            player.sendSystemMessage(Component.literal("§cOggetto non disponibile. Usa /isekai shop."));
            return 0;
        }
        CompoundTag data = getData(player);
        int balance = data.getInt(COINS);
        if (balance < offer.price()) {
            player.sendSystemMessage(Component.literal("§cMonete insufficienti: servono "
                + offer.price() + ", ne possiedi " + balance + "."));
            return 0;
        }
        data.putInt(COINS, balance - offer.price());
        ItemStack stack = new ItemStack(offer.item());
        if (!player.getInventory().add(stack)) player.drop(stack, false);
        player.sendSystemMessage(Component.literal("§aAcquisto completato: §f" + id
            + " §7(-" + offer.price() + " monete). Saldo: §e" + data.getInt(COINS)));
        return Command.SINGLE_SUCCESS;
    }

    private static Offer offer(String id) {
        return switch (id.toLowerCase(java.util.Locale.ROOT)) {
            case "aether_fruit" -> new Offer(ModItems.AETHER_FRUIT.get(), 25);
            case "aether_dust" -> new Offer(ModItems.AETHER_DUST.get(), 10);
            case "aether_shard" -> new Offer(ModItems.AETHER_SHARD.get(), 15);
            case "aether_crystal" -> new Offer(ModItems.AETHER_CRYSTAL.get(), 60);
            case "aether_ingot" -> new Offer(ModItems.AETHER_INGOT.get(), 90);
            case "sun_aether_ingot" -> new Offer(ModItems.SUN_AETHER_INGOT.get(), 130);
            case "moon_aether_ingot" -> new Offer(ModItems.MOON_AETHER_INGOT.get(), 130);
            case "aether_sword" -> new Offer(ModItems.AETHER_SWORD.get(), 300);
            case "aether_bow" -> new Offer(ModItems.AETHER_BOW.get(), 250);
            default -> null;
        };
    }

    private static CompoundTag getData(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT, Tag.TAG_COMPOUND)) persistent.put(ROOT, new CompoundTag());
        return persistent.getCompound(ROOT);
    }

    private record Offer(Item item, int price) {}
}
