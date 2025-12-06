/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.components.Checkbox
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package net.blay09.mods.balm.mixin;

import net.minecraft.client.gui.components.Checkbox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Checkbox.class})
public interface CheckboxAccessor {
    @Accessor
    public void setSelected(boolean var1);
}

