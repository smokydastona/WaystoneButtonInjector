/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.waystones.menu;

import java.util.function.Supplier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WarpPlateAttunementSlot
extends Slot {
    private final Supplier<Boolean> mayPickupFunc;

    public WarpPlateAttunementSlot(Container container, int slot, int x, int y, Supplier<Boolean> mayPickupFunc) {
        super(container, slot, x, y);
        this.mayPickupFunc = mayPickupFunc;
    }

    public boolean m_8010_(Player player) {
        return this.mayPickupFunc.get() != false && super.m_8010_(player);
    }

    public int m_5866_(ItemStack stack) {
        if (this.m_150661_() == 0) {
            return 1;
        }
        return stack.m_41741_();
    }
}

