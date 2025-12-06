/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.world.inventory.Slot
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={AbstractContainerScreen.class})
public interface AbstractContainerScreenAccessor {
    @Accessor
    public int getLeftPos();

    @Accessor
    public int getTopPos();

    @Accessor
    public int getImageWidth();

    @Accessor
    public int getImageHeight();

    @Accessor
    public Slot getHoveredSlot();

    @Invoker
    public boolean callIsHovering(Slot var1, double var2, double var4);

    @Invoker
    public void callRenderSlot(GuiGraphics var1, Slot var2);
}

