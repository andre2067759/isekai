package com.isekai;

import com.isekai.registry.ModBlocks;
import com.isekai.registry.ModItems;
import com.isekai.worldgen.ModWorldGen;
import com.isekai.registry.ModEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IsekaiMod.MOD_ID)
public class IsekaiMod {
    public static final String MOD_ID = "isekai";
    public IsekaiMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(bus);
        ModItems.register(bus);
        ModWorldGen.register(bus);
        ModEntities.register(bus);
    }
}
