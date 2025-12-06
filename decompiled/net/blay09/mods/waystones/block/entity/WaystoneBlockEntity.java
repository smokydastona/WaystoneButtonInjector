/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package net.blay09.mods.waystones.block.entity;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WaystoneBlockEntity
extends WaystoneBlockEntityBase {
    public WaystoneBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType)ModBlockEntities.waystone.get(), blockPos, blockState);
    }

    @Override
    protected ResourceLocation getWaystoneType() {
        return WaystoneTypes.WAYSTONE;
    }

    public BalmMenuProvider getMenuProvider() {
        return new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.waystone_selection");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                return WaystoneSelectionMenu.createWaystoneSelection(i, playerEntity, WarpMode.WAYSTONE_TO_WAYSTONE, WaystoneBlockEntity.this.getWaystone());
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeByte(WarpMode.WAYSTONE_TO_WAYSTONE.ordinal());
                Waystone.write(buf, WaystoneBlockEntity.this.getWaystone());
            }
        };
    }

    public BalmMenuProvider getSettingsMenuProvider() {
        return new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.waystone_settings");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                return new WaystoneSettingsMenu((MenuType<WaystoneSettingsMenu>)((MenuType)ModMenus.waystoneSettings.get()), WaystoneBlockEntity.this.getWaystone(), i);
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                Waystone.write(buf, WaystoneBlockEntity.this.getWaystone());
            }
        };
    }
}

