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
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 */
package net.blay09.mods.waystones.block.entity;

import java.util.Comparator;
import java.util.List;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.ModMenus;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.blay09.mods.waystones.menu.WaystoneSettingsMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SharestoneBlockEntity
extends WaystoneBlockEntityBase {
    public SharestoneBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType)ModBlockEntities.sharestone.get(), pos, state);
    }

    @Override
    protected ResourceLocation getWaystoneType() {
        return WaystoneTypes.getSharestone(((SharestoneBlock)this.m_58900_().m_60734_()).getColor());
    }

    @Override
    public MenuProvider getMenuProvider() {
        return new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.waystone_selection");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                return WaystoneSelectionMenu.createSharestoneSelection(playerEntity.m_20194_(), i, SharestoneBlockEntity.this.getWaystone(), SharestoneBlockEntity.this.m_58900_());
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                SharestoneBlock block = (SharestoneBlock)SharestoneBlockEntity.this.m_58900_().m_60734_();
                ResourceLocation waystoneType = WaystoneTypes.getSharestone(block.getColor());
                List<IWaystone> waystones = WaystoneManager.get(player.f_8924_).getWaystonesByType(waystoneType).sorted(Comparator.comparing(IWaystone::getName)).toList();
                Waystone.write(buf, SharestoneBlockEntity.this.getWaystone());
                buf.writeShort(waystones.size());
                for (IWaystone waystone : waystones) {
                    Waystone.write(buf, waystone);
                }
            }
        };
    }

    public BalmMenuProvider getSettingsMenuProvider() {
        return new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.waystone_settings");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
                return new WaystoneSettingsMenu((MenuType<WaystoneSettingsMenu>)((MenuType)ModMenus.waystoneSettings.get()), SharestoneBlockEntity.this.getWaystone(), i);
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                Waystone.write(buf, SharestoneBlockEntity.this.getWaystone());
            }
        };
    }
}

