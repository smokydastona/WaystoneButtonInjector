/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.menu;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WaystoneSelectionMenu
extends AbstractContainerMenu {
    private final WarpMode warpMode;
    private final IWaystone fromWaystone;
    private final List<IWaystone> waystones;

    public WaystoneSelectionMenu(MenuType<WaystoneSelectionMenu> type, WarpMode warpMode, @Nullable IWaystone fromWaystone, int windowId, List<IWaystone> waystones) {
        super(type, windowId);
        this.warpMode = warpMode;
        this.fromWaystone = fromWaystone;
        this.waystones = waystones;
    }

    public ItemStack m_7648_(Player player, int i) {
        return ItemStack.f_41583_;
    }

    public boolean m_6875_(Player player) {
        if (this.fromWaystone != null) {
            BlockPos pos = this.fromWaystone.getPos();
            return player.m_20275_((double)pos.m_123341_() + 0.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5) <= 64.0;
        }
        return true;
    }

    @Nullable
    public IWaystone getWaystoneFrom() {
        return this.fromWaystone;
    }

    public WarpMode getWarpMode() {
        return this.warpMode;
    }

    public List<IWaystone> getWaystones() {
        return this.waystones;
    }

    public static WaystoneSelectionMenu createWaystoneSelection(int windowId, Player player, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        List<IWaystone> waystones = PlayerWaystoneManager.getWaystones(player);
        return new WaystoneSelectionMenu((MenuType<WaystoneSelectionMenu>)((MenuType)ModMenus.waystoneSelection.get()), warpMode, fromWaystone, windowId, waystones);
    }

    public static WaystoneSelectionMenu createSharestoneSelection(MinecraftServer server, int windowId, IWaystone fromWaystone, BlockState state) {
        SharestoneBlock block = (SharestoneBlock)state.m_60734_();
        ResourceLocation waystoneType = WaystoneTypes.getSharestone(block.getColor());
        List<IWaystone> waystones = WaystoneManager.get(server).getWaystonesByType(waystoneType).sorted(Comparator.comparing(IWaystone::getName)).collect(Collectors.toList());
        return new WaystoneSelectionMenu((MenuType<WaystoneSelectionMenu>)((MenuType)ModMenus.sharestoneSelection.get()), WarpMode.SHARESTONE_TO_SHARESTONE, fromWaystone, windowId, waystones);
    }
}

