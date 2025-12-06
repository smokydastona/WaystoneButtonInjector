/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.entity.BlockEntity
 */
package net.blay09.mods.waystones.compat;

import java.util.function.Consumer;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WaystonesWailaUtils {
    public static final ResourceLocation WAYSTONE_UID = new ResourceLocation("waystones", "waystone");

    public static void appendTooltip(BlockEntity blockEntity, Player player, Consumer<Component> tooltipConsumer) {
        if (blockEntity instanceof WarpPlateBlockEntity) {
            WarpPlateBlockEntity warpPlate = (WarpPlateBlockEntity)blockEntity;
            IWaystone waystone = warpPlate.getWaystone();
            tooltipConsumer.accept(WarpPlateBlock.getGalacticName(waystone));
        } else if (blockEntity instanceof WaystoneBlockEntityBase) {
            boolean isActivated;
            WaystoneBlockEntityBase waystoneBlockEntity = (WaystoneBlockEntityBase)blockEntity;
            IWaystone waystone = waystoneBlockEntity.getWaystone();
            boolean bl = isActivated = !waystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE) || PlayerWaystoneManager.isWaystoneActivated(player, waystone);
            if (isActivated && waystone.hasName() && waystone.isValid()) {
                tooltipConsumer.accept((Component)Component.m_237113_((String)waystone.getName()));
            } else {
                tooltipConsumer.accept((Component)Component.m_237115_((String)"tooltip.waystones.undiscovered"));
            }
        }
    }

    private WaystonesWailaUtils() {
    }
}

