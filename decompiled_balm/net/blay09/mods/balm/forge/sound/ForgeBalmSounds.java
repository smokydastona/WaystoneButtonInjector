/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge.sound;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmSounds
implements BalmSounds {
    @Override
    public DeferredObject<SoundEvent> register(ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.SOUND_EVENTS, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> SoundEvent.m_262824_((ResourceLocation)identifier));
        return new DeferredObject<SoundEvent>(identifier, (Supplier<SoundEvent>)registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }
}

