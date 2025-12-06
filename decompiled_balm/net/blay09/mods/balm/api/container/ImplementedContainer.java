/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.Container
 *  net.minecraft.world.ContainerHelper
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.balm.api.container;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ImplementedContainer
extends Container {
    public static ImplementedContainer of(NonNullList<ItemStack> items) {
        return () -> items;
    }

    public static ImplementedContainer ofSize(int size) {
        return ImplementedContainer.of((NonNullList<ItemStack>)NonNullList.m_122780_((int)size, (Object)ItemStack.f_41583_));
    }

    public static NonNullList<ItemStack> deserializeInventory(CompoundTag tag, int minimumSize) {
        int size = Math.max(minimumSize, tag.m_128425_("Size", 3) ? tag.m_128451_("Size") : minimumSize);
        NonNullList items = NonNullList.m_122780_((int)size, (Object)ItemStack.f_41583_);
        ListTag itemTags = tag.m_128437_("Items", 10);
        for (int i = 0; i < itemTags.size(); ++i) {
            CompoundTag itemTag = itemTags.m_128728_(i);
            int slot = itemTag.m_128451_("Slot");
            if (slot < 0 || slot >= items.size()) continue;
            items.set(slot, (Object)ItemStack.m_41712_((CompoundTag)itemTag));
        }
        return items;
    }

    public NonNullList<ItemStack> getItems();

    default public int m_6643_() {
        return this.getItems().size();
    }

    default public boolean m_7983_() {
        for (int i = 0; i < this.m_6643_(); ++i) {
            ItemStack stack = this.m_8020_(i);
            if (stack.m_41619_()) continue;
            return false;
        }
        return true;
    }

    default public ItemStack m_8020_(int slot) {
        return (ItemStack)this.getItems().get(slot);
    }

    default public ItemStack m_7407_(int slot, int count) {
        ItemStack result = ContainerHelper.m_18969_(this.getItems(), (int)slot, (int)count);
        if (!result.m_41619_()) {
            this.m_6596_();
        }
        this.slotChanged(slot);
        return result;
    }

    default public ItemStack m_8016_(int slot) {
        ItemStack itemStack = ContainerHelper.m_18966_(this.getItems(), (int)slot);
        this.slotChanged(slot);
        return itemStack;
    }

    default public void m_6836_(int slot, ItemStack stack) {
        this.getItems().set(slot, (Object)stack);
        if (stack.m_41613_() > this.m_6893_()) {
            stack.m_41764_(this.m_6893_());
        }
        this.m_6596_();
        this.slotChanged(slot);
    }

    default public void m_6211_() {
        this.getItems().clear();
        for (int i = 0; i < this.getItems().size(); ++i) {
            this.slotChanged(i);
        }
    }

    default public void m_6596_() {
    }

    default public void slotChanged(int slot) {
    }

    default public boolean m_6542_(Player player) {
        return true;
    }

    default public CompoundTag serializeInventory() {
        NonNullList<ItemStack> items = this.getItems();
        ListTag itemTags = new ListTag();
        for (int i = 0; i < items.size(); ++i) {
            if (((ItemStack)items.get(i)).m_41619_()) continue;
            CompoundTag itemTag = new CompoundTag();
            itemTag.m_128405_("Slot", i);
            ((ItemStack)items.get(i)).m_41739_(itemTag);
            itemTags.add((Object)itemTag);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.m_128365_("Items", (Tag)itemTags);
        nbt.m_128405_("Size", items.size());
        return nbt;
    }
}

