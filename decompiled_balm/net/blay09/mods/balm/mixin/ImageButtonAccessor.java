/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.ImageButton
 *  net.minecraft.resources.ResourceLocation
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={ImageButton.class})
public interface ImageButtonAccessor {
    @Accessor
    public ResourceLocation getResourceLocation();
}

