package com.isekai;

import com.isekai.registry.ModBlocks;
import com.isekai.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IsekaiMod.MOD_ID)
public class IsekaiMod {
    public static final String MOD_ID = "isekai";

    public IsekaiMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
    }
}
