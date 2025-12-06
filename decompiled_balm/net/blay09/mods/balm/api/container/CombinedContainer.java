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

import java.util.Arrays;
import java.util.HashSet;
import net.blay09.mods.balm.api.container.EmptyContainer;
import net.blay09.mods.balm.api.container.ExtractionAwareContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CombinedContainer
implements Container,
WorldlyContainer,
ExtractionAwareContainer {
    private final Container[] containers;
    private final int[] baseIndex;
    private final int totalSlots;

    public CombinedContainer(Container ... containers) {
        this.containers = containers;
        this.baseIndex = new int[containers.length];
        int index = 0;
        for (int i = 0; i < containers.length; ++i) {
            this.baseIndex[i] = index += containers[i].m_6643_();
        }
        this.totalSlots = index;
    }

    private int getContainerIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        }
        for (int i = 0; i < this.baseIndex.length; ++i) {
            if (slot - this.baseIndex[i] >= 0) continue;
            return i;
        }
        return -1;
    }

    private Container getContainerFromIndex(int index) {
        return index >= 0 && index < this.containers.length ? this.containers[index] : EmptyContainer.INSTANCE;
    }

    private int getInnerSlotFromIndex(int slot, int index) {
        return index > 0 && index < this.baseIndex.length ? slot - this.baseIndex[index - 1] : slot;
    }

    private int getOuterSlotFromIndex(int slot, int index) {
        return index < this.baseIndex.length ? slot - this.baseIndex[index] : slot;
    }

    public int m_6643_() {
        return this.totalSlots;
    }

    public boolean m_7983_() {
        return Arrays.stream(this.containers).allMatch(Container::m_7983_);
    }

    public ItemStack m_8020_(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.m_8020_(this.getInnerSlotFromIndex(slot, containerIndex));
    }

    public ItemStack m_7407_(int slot, int amount) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.m_7407_(this.getInnerSlotFromIndex(slot, containerIndex), amount);
    }

    public ItemStack m_8016_(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.m_8016_(this.getInnerSlotFromIndex(slot, containerIndex));
    }

    public void m_6836_(int slot, ItemStack itemStack) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        container.m_6836_(this.getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    public void m_6596_() {
        for (Container container : this.containers) {
            container.m_6596_();
        }
    }

    public boolean m_6542_(Player player) {
        return Arrays.stream(this.containers).allMatch(container -> container.m_6542_(player));
    }

    public void m_6211_() {
        for (Container container : this.containers) {
            container.m_6211_();
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        if (container instanceof ExtractionAwareContainer) {
            ExtractionAwareContainer extractionAwareContainer = (ExtractionAwareContainer)container;
            return extractionAwareContainer.canExtractItem(this.getInnerSlotFromIndex(slot, containerIndex));
        }
        return true;
    }

    public void m_5856_(Player player) {
        for (Container container : this.containers) {
            container.m_5856_(player);
        }
    }

    public void m_5785_(Player player) {
        for (Container container : this.containers) {
            container.m_5785_(player);
        }
    }

    public boolean m_7013_(int slot, ItemStack itemStack) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        return container.m_7013_(this.getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    public boolean m_271862_(Container container, int slot, ItemStack itemStack) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container subContainer = this.getContainerFromIndex(containerIndex);
        return container.m_271862_(subContainer, this.getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    public int[] m_7071_(Direction direction) {
        HashSet<Integer> slots = new HashSet<Integer>();
        for (int index = 0; index < this.containers.length; ++index) {
            Container container = this.containers[index];
            if (container instanceof WorldlyContainer) {
                WorldlyContainer worldlyContainer = (WorldlyContainer)container;
                for (int i2 : worldlyContainer.m_7071_(direction)) {
                    slots.add(i2);
                }
                continue;
            }
            for (int i3 = 0; i3 < container.m_6643_(); ++i3) {
                slots.add(this.getOuterSlotFromIndex(i3, index));
            }
        }
        return slots.stream().mapToInt(i -> i).toArray();
    }

    public boolean m_7155_(int slot, ItemStack itemStack, @Nullable Direction direction) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldlyContainer = (WorldlyContainer)container;
            return worldlyContainer.m_7155_(this.getInnerSlotFromIndex(slot, containerIndex), itemStack, direction);
        }
        return this.m_7013_(slot, itemStack);
    }

    public boolean m_7157_(int slot, ItemStack itemStack, Direction direction) {
        int containerIndex = this.getContainerIndexForSlot(slot);
        Container container = this.getContainerFromIndex(containerIndex);
        if (container instanceof WorldlyContainer) {
            WorldlyContainer worldlyContainer = (WorldlyContainer)container;
            return worldlyContainer.m_7157_(slot, itemStack, direction);
        }
        return this.m_271862_(this, slot, itemStack);
    }
}

