/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleType
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge.particle;

import java.util.function.Function;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmParticles
implements BalmParticles {
    @Override
    public <T extends ParticleOptions> DeferredObject<ParticleType<T>> registerParticle(Function<ResourceLocation, ParticleType<T>> supplier, ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.PARTICLE_TYPES, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> (ParticleType)supplier.apply(identifier));
        return new DeferredObject<ParticleType<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public SimpleParticleType createSimple(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }
}

