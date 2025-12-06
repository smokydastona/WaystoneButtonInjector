/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.stats.StatFormatter
 *  net.minecraft.stats.Stats
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.minecraftforge.registries.DeferredRegister
 */
package net.blay09.mods.balm.forge.stats;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;

public record ForgeBalmStats(NamespaceResolver namespaceResolver) implements BalmStats
{
    @Override
    public void registerCustomStat(ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(Registries.f_256887_, identifier.m_135827_());
        register.register(identifier.m_135815_(), () -> identifier);
        this.getActiveRegistrations().customStats.add(identifier);
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(this.namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public static class Registrations {
        public final List<ResourceLocation> customStats = new ArrayList<ResourceLocation>();

        @SubscribeEvent
        public void commonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> this.customStats.forEach(it -> Stats.f_12988_.m_12899_(it, StatFormatter.f_12873_)));
        }
    }
}

