/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 *  net.minecraft.world.Container
 *  net.minecraft.world.WorldlyContainer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api.container;

import java.util.ArrayList;
import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SubContainer
implements Container,
WorldlyContainer,
ExtractionAwareContainer {
    private final Container container;
    private final int minSlot;
    private final int maxSlot;

    public SubContainer(Container container, int minSlot, int maxSlot) {
        this.container = container;
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    public int m_6643_() {
        return this.maxSlot - this.minSlot;
    }

    public ItemStack m_8020_(int slot) {
        return this.containsSlot(slot) ? this.container.m_8020_(slot + this.minSlot) : ItemStack.f_41583_;
    }

    public ItemStack m_7407_(int slot, int amount) {
        return this.containsSlot(slot) ? this.container.m_7407_(slot + this.minSlot, amount) : ItemStack.f_41583_;
    }

    public ItemStack m_8016_(int slot) {
        return this.containsSlot(slot) ? this.container.m_8016_(slot + this.minSlot) : ItemStack.f_41583_;
    }

    public void m_6836_(int slot, ItemStack itemStack) {
        if (this.containsSlot(slot)) {
            this.container.m_6836_(slot + this.minSlot, itemStack);
        }
    }

    public void m_5856_(Player player) {
        this.container.m_5856_(player);
    }

    public void m_5785_(Player player) {
        this.container.m_5785_(player);
    }

    public boolean m_7013_(int slot, ItemStack itemStack) {
        return this.containsSlot(slot) && this.container.m_7013_(slot + this.minSlot, itemStack);
    }

    public boolean m_7983_() {
        for (int i = this.minSlot; i < this.maxSlot; ++i) {
            if (this.container.m_8020_(i).m_41619_()) continue;
            return false;
        }
        return true;
    }

    public boolean m_6542_(Player player) {
        return this.container.m_6542_(player);
    }

    public int m_6893_() {
        return this.container.m_6893_();
    }

    public void m_6596_() {
        this.container.m_6596_();
    }

    @Deprecated(since="1.20")
    public boolean containsSlot(int slot) {
        return slot + this.minSlot < this.maxSlot;
    }

    public boolean containsOuterSlot(int slot) {
        return slot >= this.minSlot && slot < this.maxSlot;
    }

    public void m_6211_() {
        for (int i = this.minSlot; i < this.maxSlot; ++i) {
            this.container.m_6836_(i, ItemStack.f_41583_);
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        Container container = this.container;
        if (container instanceof ExtractionAwareContainer) {
            ExtractionAwareContainer extractionAwareContainer = (ExtractionAwareContainer)container;
            return this.containsSlot(slot) && extractionAwareContainer.canExtractItem(slot + this.minSlot);
        }
        return this.containsSlot(slot);
    }

    public boolean m_271862_(Container container, int slot, ItemStack itemStack) {
        return this.containsSlot(slot) && this.container.m_271862_(this.container, slot + this.minSlot, itemStack);
    }

    public int[] m_7071_(Direction direction) {
        Container container = this.container;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldContainer = (WorldlyContainer)container;
            int[] original = worldContainer.m_7071_(direction);
            ArrayList<Integer> result = new ArrayList<Integer>();
            for (int outerSlot : original) {
                if (!this.containsOuterSlot(outerSlot)) continue;
                result.add(outerSlot - this.minSlot);
            }
            return result.stream().mapToInt(i -> i).toArray();
        }
        int[] result = new int[this.m_6643_()];
        for (int i2 = 0; i2 < result.length; ++i2) {
            result[i2] = i2;
        }
        return result;
    }

    public int[] getOuterSlotsForFace(Direction direction) {
        Container container = this.container;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldContainer = (WorldlyContainer)container;
            int[] original = worldContainer.m_7071_(direction);
            ArrayList<Integer> result = new ArrayList<Integer>();
            for (int outerSlot : original) {
                if (!this.containsOuterSlot(outerSlot)) continue;
                result.add(outerSlot);
            }
            return result.stream().mapToInt(i -> i).toArray();
        }
        int[] slots = new int[this.maxSlot - this.minSlot];
        for (int i2 = 0; i2 < slots.length; ++i2) {
            slots[i2] = i2 + this.minSlot;
        }
        return slots;
    }

    public boolean m_7155_(int slot, ItemStack itemStack, @Nullable Direction direction) {
        Container container = this.container;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldlyContainer = (WorldlyContainer)container;
            return this.containsSlot(slot) && worldlyContainer.m_7155_(slot + this.minSlot, itemStack, direction);
        }
        return this.m_7013_(slot, itemStack);
    }

    public boolean m_7157_(int slot, ItemStack itemStack, Direction direction) {
        Container container = this.container;
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldlyContainer = (WorldlyContainer)container;
            return this.containsSlot(slot) && worldlyContainer.m_7157_(slot + this.minSlot, itemStack, direction);
        }
        return this.m_271862_(this, slot, itemStack);
    }
}

