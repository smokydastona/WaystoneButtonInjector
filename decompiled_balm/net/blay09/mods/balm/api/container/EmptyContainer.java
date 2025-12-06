/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.container.ImplementedContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class EmptyContainer
implements ImplementedContainer {
    public static final EmptyContainer INSTANCE = new EmptyContainer();

    @Override
    public NonNullList<ItemStack> getItems() {
        return NonNullList.m_182647_((int)0);
    }
}

