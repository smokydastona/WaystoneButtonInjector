/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.item;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.item.AbstractAttunedShardItem;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CrumblingAttunedShardItem
extends AbstractAttunedShardItem {
    public CrumblingAttunedShardItem(Item.Properties properties) {
        super(properties.m_41487_(4));
    }

    @Override
    public void m_7373_(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        super.m_7373_(stack, world, list, flag);
        IWaystone attunedWarpPlate = this.getWaystoneAttunedTo(null, stack);
        if (attunedWarpPlate != null && attunedWarpPlate.isValid()) {
            AbstractContainerMenu abstractContainerMenu;
            MutableComponent textComponent = Component.m_237115_((String)"tooltip.waystones.attuned_shard.attunement_crumbling");
            textComponent.m_130940_(ChatFormatting.WHITE).m_130940_(ChatFormatting.ITALIC);
            Player player = Balm.getProxy().getClientPlayer();
            if (player != null && (abstractContainerMenu = player.f_36096_) instanceof WarpPlateContainer) {
                WarpPlateContainer wpc = (WarpPlateContainer)abstractContainerMenu;
                if (!attunedWarpPlate.getWaystoneUid().equals(wpc.getWaystone().getWaystoneUid())) {
                    list.add((Component)textComponent);
                }
            } else {
                list.add((Component)textComponent);
            }
        }
    }
}

