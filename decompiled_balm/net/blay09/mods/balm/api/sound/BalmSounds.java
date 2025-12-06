/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 */
package net.blay09.mods.balm.api.sound;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public interface BalmSounds {
    public DeferredObject<SoundEvent> register(ResourceLocation var1);
}

