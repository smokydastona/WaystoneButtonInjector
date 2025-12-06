/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.balm.api.capability;

import net.minecraft.resources.ResourceLocation;

public record CapabilityType<TScope, TApi, TContext>(ResourceLocation identifier, Class<TScope> scopeClass, Class<TApi> apiClass, Class<TContext> contextClass, Object backingType) {
}

