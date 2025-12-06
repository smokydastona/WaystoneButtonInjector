/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.world.WorldlyContainer
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.container.ImplementedContainer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DefaultContainer
implements ImplementedContainer,
WorldlyContainer {
    private NonNullList<ItemStack> items;

    public DefaultContainer(int size) {
        this.items = NonNullList.m_122780_((int)size, (Object)ItemStack.f_41583_);
    }

    public DefaultContainer(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void deserialize(CompoundTag tag) {
        this.items = ImplementedContainer.deserializeInventory(tag, this.items.size());
    }

    public CompoundTag serialize() {
        return this.serializeInventory();
    }

    public int[] m_7071_(Direction direction) {
        int[] slots = new int[this.items.size()];
        for (int i = 0; i < slots.length; ++i) {
            slots[i] = i;
        }
        return slots;
    }

    public boolean m_7155_(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return this.m_7013_(slot, itemStack);
    }

    public boolean m_7157_(int slot, ItemStack itemStack, Direction direction) {
        return this.m_271862_(this, slot, itemStack);
    }
}

