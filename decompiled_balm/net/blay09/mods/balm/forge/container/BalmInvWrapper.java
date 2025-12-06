/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.items.wrapper.InvWrapper
 */
package net.blay09.mods.balm.forge.container;

import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BalmInvWrapper
extends InvWrapper {
    public BalmInvWrapper(Container inv) {
        super(inv);
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ExtractionAwareContainer extractionAwareContainer;
        Container container = this.getInv();
        if (container instanceof ExtractionAwareContainer && !(extractionAwareContainer = (ExtractionAwareContainer)container).canExtractItem(slot)) {
            return ItemStack.f_41583_;
        }
        return super.extractItem(slot, amount, simulate);
    }
}

