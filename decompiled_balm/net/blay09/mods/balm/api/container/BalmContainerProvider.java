/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.world.Container
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.container.ContainerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface BalmContainerProvider {
    public Container getContainer();

    default public Container getContainer(Direction side) {
        return this.getContainer();
    }

    default public void dropItems(Level level, BlockPos pos) {
        Container container = this.getContainer();
        ContainerUtils.dropItems(container, level, pos);
    }

    default public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Container container = this.getContainer();
        return ContainerUtils.extractItem(container, slot, amount, simulate);
    }

    default public ItemStack insertItem(ItemStack itemStack, int slot, boolean simulate) {
        Container container = this.getContainer();
        return ContainerUtils.insertItem(container, slot, itemStack, simulate);
    }

    default public ItemStack insertItemStacked(ItemStack itemStack, boolean simulate) {
        Container container = this.getContainer();
        return ContainerUtils.insertItemStacked(container, itemStack, simulate);
    }
}

