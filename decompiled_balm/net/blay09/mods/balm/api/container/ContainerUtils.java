/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerUtils {
    public static ItemStack extractItem(Container container, int slot, int amount, boolean simulate) {
        ExtractionAwareContainer extractionAwareContainer;
        if (container instanceof ExtractionAwareContainer && !(extractionAwareContainer = (ExtractionAwareContainer)container).canExtractItem(slot)) {
            return ItemStack.f_41583_;
        }
        if (amount == 0) {
            return ItemStack.f_41583_;
        }
        if (slot < 0 || slot >= container.m_6643_()) {
            return ItemStack.f_41583_;
        }
        ItemStack existing = container.m_8020_(slot);
        if (existing.m_41619_()) {
            return ItemStack.f_41583_;
        }
        int toExtract = Math.min(amount, existing.m_41741_());
        if (existing.m_41613_() <= toExtract) {
            if (!simulate) {
                container.m_6836_(slot, ItemStack.f_41583_);
                return existing;
            }
            return existing.m_41777_();
        }
        if (!simulate) {
            container.m_6836_(slot, ContainerUtils.copyStackWithSize(existing, existing.m_41613_() - toExtract));
        }
        return ContainerUtils.copyStackWithSize(existing, toExtract);
    }

    public static ItemStack insertItem(Container container, ItemStack itemStack, boolean simulate) {
        if (container == null || itemStack.m_41619_()) {
            return itemStack;
        }
        for (int i = 0; i < container.m_6643_(); ++i) {
            if (!(itemStack = ContainerUtils.insertItem(container, i, itemStack, simulate)).m_41619_()) continue;
            return ItemStack.f_41583_;
        }
        return itemStack;
    }

    public static ItemStack insertItem(Container container, int slot, ItemStack itemStack, boolean simulate) {
        boolean reachedLimit;
        if (container == null || itemStack.m_41619_()) {
            return itemStack;
        }
        if (slot < 0 || slot >= container.m_6643_()) {
            return ItemStack.f_41583_;
        }
        ItemStack existing = container.m_8020_(slot);
        int limit = Math.min(container.m_6893_(), itemStack.m_41741_());
        if (!existing.m_41619_()) {
            if (!Balm.getHooks().canItemsStack(itemStack, existing)) {
                return itemStack;
            }
            limit -= existing.m_41613_();
        }
        if (limit <= 0) {
            return itemStack;
        }
        boolean bl = reachedLimit = itemStack.m_41613_() > limit;
        if (!simulate) {
            if (existing.m_41619_()) {
                container.m_6836_(slot, reachedLimit ? ContainerUtils.copyStackWithSize(itemStack, limit) : itemStack);
            } else {
                existing.m_41769_(reachedLimit ? limit : itemStack.m_41613_());
                container.m_6596_();
            }
        }
        return reachedLimit ? ContainerUtils.copyStackWithSize(itemStack, itemStack.m_41613_() - limit) : ItemStack.f_41583_;
    }

    public static ItemStack insertItemStacked(Container container, ItemStack itemStack, boolean simulate) {
        int i;
        if (container == null || itemStack.m_41619_()) {
            return itemStack;
        }
        if (!itemStack.m_41753_()) {
            return ContainerUtils.insertItem(container, itemStack, simulate);
        }
        int firstEmptySlot = -1;
        for (i = 0; i < container.m_6643_(); ++i) {
            ItemStack slotStack = container.m_8020_(i);
            if (slotStack.m_41619_() && firstEmptySlot == -1) {
                firstEmptySlot = i;
                continue;
            }
            if (slotStack.m_41753_() && ItemStack.m_150942_((ItemStack)slotStack, (ItemStack)itemStack)) {
                itemStack = ContainerUtils.insertItem(container, i, itemStack, simulate);
            }
            if (!itemStack.m_41619_()) continue;
            return ItemStack.f_41583_;
        }
        if (firstEmptySlot != -1) {
            for (i = firstEmptySlot; i < container.m_6643_(); ++i) {
                if (!container.m_8020_(i).m_41619_() || !(itemStack = ContainerUtils.insertItem(container, i, itemStack, simulate)).m_41619_()) continue;
                return ItemStack.f_41583_;
            }
        }
        return itemStack;
    }

    public static void dropItems(Container container, Level level, BlockPos pos) {
        for (int i = 0; i < container.m_6643_(); ++i) {
            ItemStack itemStack = container.m_8020_(i);
            if (itemStack.m_41619_()) continue;
            ItemEntity itemEntity = new ItemEntity(level, (double)((float)pos.m_123341_() + 0.5f), (double)((float)pos.m_123342_() + 0.5f), (double)((float)pos.m_123343_() + 0.5f), itemStack);
            itemEntity.m_20334_(0.0, (double)0.2f, 0.0);
            level.m_7967_((Entity)itemEntity);
        }
        container.m_6211_();
    }

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        if (size == 0) {
            return ItemStack.f_41583_;
        }
        ItemStack copy = itemStack.m_41777_();
        copy.m_41764_(size);
        return copy;
    }
}

