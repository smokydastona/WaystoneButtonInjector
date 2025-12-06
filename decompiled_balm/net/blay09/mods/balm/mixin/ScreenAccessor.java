/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.narration.NarratableEntry
 *  net.minecraft.client.gui.screens.Screen
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package net.blay09.mods.balm.mixin;

import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Screen.class})
public interface ScreenAccessor {
    @Accessor(value="children")
    public List<GuiEventListener> balm_getChildren();

    @Accessor(value="narratables")
    public List<NarratableEntry> balm_getNarratables();

    @Accessor(value="renderables")
    public List<Renderable> balm_getRenderables();
}

