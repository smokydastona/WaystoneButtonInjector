/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.item;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IAttunementItem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAttunedShardItem
extends Item
implements IAttunementItem {
    public AbstractAttunedShardItem(Item.Properties properties) {
        super(properties);
    }

    public boolean m_5812_(ItemStack itemStack) {
        IWaystone waystoneAttunedTo = this.getWaystoneAttunedTo(null, itemStack);
        return waystoneAttunedTo != null && waystoneAttunedTo.isValid();
    }

    public void m_7373_(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        super.m_7373_(stack, world, list, flag);
        IWaystone attunedWarpPlate = this.getWaystoneAttunedTo(null, stack);
        if (attunedWarpPlate == null || !attunedWarpPlate.isValid()) {
            MutableComponent textComponent = Component.m_237115_((String)"tooltip.waystones.attuned_shard.attunement_lost");
            textComponent.m_130940_(ChatFormatting.GRAY);
            list.add((Component)textComponent);
            return;
        }
        list.add(WarpPlateBlock.getGalacticName(attunedWarpPlate));
        Player player = Balm.getProxy().getClientPlayer();
        if (player != null && player.f_36096_ instanceof WarpPlateContainer) {
            IWaystone currentWarpPlate = ((WarpPlateContainer)player.f_36096_).getWaystone();
            if (attunedWarpPlate.getWaystoneUid().equals(currentWarpPlate.getWaystoneUid())) {
                list.add((Component)Component.m_237115_((String)"tooltip.waystones.attuned_shard.move_to_other_warp_plate"));
            } else {
                list.add((Component)Component.m_237115_((String)"tooltip.waystones.attuned_shard.plug_into_warp_plate"));
            }
        } else {
            list.add((Component)Component.m_237115_((String)"tooltip.waystones.attuned_shard.plug_into_warp_plate"));
        }
    }

    @Override
    @Nullable
    public IWaystone getWaystoneAttunedTo(MinecraftServer server, ItemStack itemStack) {
        CompoundTag compound = itemStack.m_41783_();
        if (compound != null && compound.m_128425_("AttunedToWaystone", 11)) {
            return new WaystoneProxy(server, NbtUtils.m_129233_((Tag)Objects.requireNonNull(compound.m_128423_("AttunedToWaystone"))));
        }
        return null;
    }

    @Override
    public void setWaystoneAttunedTo(ItemStack itemStack, @Nullable IWaystone waystone) {
        CompoundTag tagCompound = itemStack.m_41783_();
        if (tagCompound == null) {
            tagCompound = new CompoundTag();
            itemStack.m_41751_(tagCompound);
        }
        if (waystone != null) {
            tagCompound.m_128365_("AttunedToWaystone", (Tag)NbtUtils.m_129226_((UUID)waystone.getWaystoneUid()));
        } else {
            tagCompound.m_128473_("AttunedToWaystone");
        }
    }
}

