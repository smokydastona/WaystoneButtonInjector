/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.Container
 *  net.minecraft.world.SimpleContainer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.inventory.SimpleContainerData
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.waystones.menu;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WarpPlateAttunementSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WarpPlateContainer
extends AbstractContainerMenu {
    private final IWaystone waystone;
    private final Container container;
    private final ContainerData containerData;

    public WarpPlateContainer(int windowId, Inventory playerInventory, IWaystone waystone) {
        this(windowId, playerInventory, waystone, (Container)new SimpleContainer(5), (ContainerData)new SimpleContainerData(3));
    }

    public WarpPlateContainer(int windowId, Inventory playerInventory, IWaystone waystone, Container container, ContainerData containerData) {
        super((MenuType)ModMenus.warpPlate.get(), windowId);
        this.waystone = waystone;
        this.container = container;
        this.containerData = containerData;
        WarpPlateContainer.m_38886_((ContainerData)containerData, (int)1);
        this.m_38897_(new WarpPlateAttunementSlot(container, 0, 80, 45, this::isCompletedFirstAttunement));
        this.m_38897_(new WarpPlateAttunementSlot(container, 1, 80, 17, this::isCompletedFirstAttunement));
        this.m_38897_(new WarpPlateAttunementSlot(container, 2, 108, 45, this::isCompletedFirstAttunement));
        this.m_38897_(new WarpPlateAttunementSlot(container, 3, 80, 73, this::isCompletedFirstAttunement));
        this.m_38897_(new WarpPlateAttunementSlot(container, 4, 52, 45, this::isCompletedFirstAttunement));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.m_38897_(new Slot((Container)playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.m_38897_(new Slot((Container)playerInventory, j, 8 + j * 18, 162));
        }
        this.m_38884_(containerData);
    }

    private boolean isCompletedFirstAttunement() {
        return this.containerData.m_6413_(2) == 1;
    }

    public boolean m_6875_(Player player) {
        return this.container.m_6542_(player);
    }

    public float getAttunementProgress() {
        return (float)this.containerData.m_6413_(0) / (float)this.containerData.m_6413_(1);
    }

    public ItemStack m_7648_(Player player, int index) {
        ItemStack itemStack = ItemStack.f_41583_;
        Slot slot = (Slot)this.f_38839_.get(index);
        if (slot.m_6657_()) {
            ItemStack slotStack = slot.m_7993_();
            itemStack = slotStack.m_41777_();
            if (index < 5 ? !this.m_38903_(slotStack, 5, this.f_38839_.size(), true) : (!this.m_38853_(0).m_6657_() ? !this.m_38903_(slotStack.m_41620_(1), 0, 1, false) : !this.m_38903_(slotStack, 1, 5, false))) {
                return ItemStack.f_41583_;
            }
            if (slotStack.m_41619_()) {
                slot.m_5852_(ItemStack.f_41583_);
            } else {
                slot.m_6654_();
            }
        }
        return itemStack;
    }

    public IWaystone getWaystone() {
        return this.waystone;
    }
}

