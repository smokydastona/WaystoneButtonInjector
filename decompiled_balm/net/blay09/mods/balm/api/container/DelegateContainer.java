/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.Container
 *  net.minecraft.world.WorldlyContainer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.container;

import java.util.Set;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DelegateContainer
implements Container,
WorldlyContainer,
ExtractionAwareContainer {
    private final Container delegate;

    public DelegateContainer(Container delegate) {
        this.delegate = delegate;
    }

    public int m_6893_() {
        return this.delegate.m_6893_();
    }

    public void m_5856_(Player player) {
        this.delegate.m_5856_(player);
    }

    public void m_5785_(Player player) {
        this.delegate.m_5785_(player);
    }

    public boolean m_7013_(int slot, ItemStack itemStack) {
        return this.delegate.m_7013_(slot, itemStack);
    }

    public int m_18947_(Item item) {
        return this.delegate.m_18947_(item);
    }

    public boolean m_18949_(Set<Item> items) {
        return this.delegate.m_18949_(items);
    }

    public int m_6643_() {
        return this.delegate.m_6643_();
    }

    public boolean m_7983_() {
        return this.delegate.m_7983_();
    }

    public ItemStack m_8020_(int slot) {
        return this.delegate.m_8020_(slot);
    }

    public ItemStack m_7407_(int slot, int count) {
        return this.delegate.m_7407_(slot, count);
    }

    public ItemStack m_8016_(int slot) {
        return this.delegate.m_8016_(slot);
    }

    public void m_6836_(int slot, ItemStack itemStack) {
        this.delegate.m_6836_(slot, itemStack);
    }

    public void m_6596_() {
        this.delegate.m_6596_();
    }

    public boolean m_6542_(Player player) {
        return this.delegate.m_6542_(player);
    }

    public void m_6211_() {
        this.delegate.m_6211_();
    }

    @Override
    public boolean canExtractItem(int slot) {
        Container container = this.delegate;
        if (container instanceof ExtractionAwareContainer) {
            ExtractionAwareContainer extractionAwareContainer = (ExtractionAwareContainer)container;
            return extractionAwareContainer.canExtractItem(slot);
        }
        return true;
    }

    public boolean m_271862_(Container container, int slot, ItemStack itemStack) {
        return this.delegate.m_271862_((Container)this, slot, itemStack);
    }

    public boolean m_216874_(Predicate<ItemStack> predicate) {
        return this.delegate.m_216874_(predicate);
    }

    public int[] m_7071_(Direction direction) {
        Container container = this.delegate;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldContainer = (WorldlyContainer)container;
            return worldContainer.m_7071_(direction);
        }
        int[] slots = new int[this.delegate.m_6643_()];
        for (int i = 0; i < slots.length; ++i) {
            slots[i] = i;
        }
        return slots;
    }

    public boolean m_7155_(int slot, ItemStack itemStack, @Nullable Direction direction) {
        boolean bl;
        Container container = this.delegate;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldContainer = (WorldlyContainer)container;
            bl = worldContainer.m_7155_(slot, itemStack, direction);
        } else {
            bl = this.m_7013_(slot, itemStack);
        }
        return bl;
    }

    public boolean m_7157_(int slot, ItemStack itemStack, Direction direction) {
        boolean bl;
        Container container = this.delegate;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldContainer = (WorldlyContainer)container;
            bl = worldContainer.m_7157_(slot, itemStack, direction);
        } else {
            bl = this.m_271862_(this, slot, itemStack);
        }
        return bl;
    }
}

