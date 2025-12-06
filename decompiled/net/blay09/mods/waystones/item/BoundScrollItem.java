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
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.context.UseOnContext
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.item;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IAttunementItem;
import net.blay09.mods.waystones.api.IFOVOnUse;
import net.blay09.mods.waystones.api.IResetUseOnDamage;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.blay09.mods.waystones.compat.Compat;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.item.ScrollItemBase;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class BoundScrollItem
extends ScrollItemBase
implements IResetUseOnDamage,
IFOVOnUse,
IAttunementItem {
    public BoundScrollItem(Item.Properties properties) {
        super(properties);
    }

    public int m_8105_(ItemStack itemStack) {
        return WaystonesConfig.getActive().cooldowns.scrollUseTime;
    }

    @Deprecated
    public static void setBoundTo(ItemStack itemStack, @Nullable IWaystone entry) {
        WaystonesAPI.setBoundWaystone(itemStack, entry);
    }

    @Deprecated
    @Nullable
    protected IWaystone getBoundTo(Player player, ItemStack itemStack) {
        return this.getWaystoneAttunedTo(player.m_20194_(), itemStack);
    }

    @Override
    public void setWaystoneAttunedTo(ItemStack itemStack, @Nullable IWaystone waystone) {
        CompoundTag tagCompound = itemStack.m_41783_();
        if (tagCompound == null) {
            tagCompound = new CompoundTag();
            itemStack.m_41751_(tagCompound);
        }
        if (waystone != null) {
            tagCompound.m_128365_("WaystonesBoundTo", (Tag)NbtUtils.m_129226_((UUID)waystone.getWaystoneUid()));
        } else {
            tagCompound.m_128473_("WaystonesBoundTo");
        }
    }

    @Override
    @Nullable
    public IWaystone getWaystoneAttunedTo(@Nullable MinecraftServer server, ItemStack itemStack) {
        CompoundTag tagCompound = itemStack.m_41783_();
        if (tagCompound != null && tagCompound.m_128425_("WaystonesBoundTo", 11)) {
            return new WaystoneProxy(server, NbtUtils.m_129233_((Tag)Objects.requireNonNull(tagCompound.m_128423_("WaystonesBoundTo"))));
        }
        return null;
    }

    protected WarpMode getWarpMode() {
        return WarpMode.BOUND_SCROLL;
    }

    public InteractionResult m_6225_(UseOnContext context) {
        Player player = context.m_43723_();
        if (player == null) {
            return InteractionResult.PASS;
        }
        ItemStack heldItem = player.m_21120_(context.m_43724_());
        Level world = context.m_43725_();
        BlockEntity tileEntity = world.m_7702_(context.m_8083_());
        if (tileEntity instanceof WaystoneBlockEntity) {
            IWaystone waystone = ((WaystoneBlockEntity)tileEntity).getWaystone();
            if (!PlayerWaystoneManager.isWaystoneActivated(player, waystone)) {
                PlayerWaystoneManager.activateWaystone(player, waystone);
            }
            if (!world.f_46443_) {
                ItemStack boundItem = heldItem.m_41613_() == 1 ? heldItem : heldItem.m_41620_(1);
                WaystonesAPI.setBoundWaystone(boundItem, waystone);
                if (boundItem != heldItem && !player.m_36356_(boundItem)) {
                    player.m_36176_(boundItem, false);
                }
                MutableComponent chatComponent = Component.m_237110_((String)"chat.waystones.scroll_bound", (Object[])new Object[]{waystone.getName()});
                chatComponent.m_130940_(ChatFormatting.YELLOW);
                player.m_5661_((Component)chatComponent, true);
                world.m_5594_(null, context.m_8083_(), SoundEvents.f_12275_, SoundSource.BLOCKS, 0.2f, 2.0f);
            }
            return !world.f_46443_ ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }

    public ItemStack m_5922_(ItemStack stack, Level world, LivingEntity entity) {
        Player player;
        IWaystone boundTo;
        if (!world.f_46443_ && entity instanceof ServerPlayer && (boundTo = this.getBoundTo(player = (Player)entity, stack)) != null) {
            double distance = entity.m_20275_((double)boundTo.getPos().m_123341_(), (double)boundTo.getPos().m_123342_(), (double)boundTo.getPos().m_123343_());
            if (distance <= 3.0) {
                return stack;
            }
            PlayerWaystoneManager.tryTeleportToWaystone((Entity)player, boundTo, this.getWarpMode(), null);
        }
        return stack;
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        IWaystone boundTo = this.getBoundTo(player, itemStack);
        if (boundTo != null) {
            if (!player.m_6117_() && world.f_46443_) {
                world.m_6269_(null, (Entity)player, SoundEvents.f_12288_, SoundSource.PLAYERS, 0.1f, 2.0f);
            }
            if (this.m_8105_(itemStack) <= 0 || Compat.isVivecraftInstalled) {
                this.m_5922_(itemStack, world, (LivingEntity)player);
            } else {
                player.m_6672_(hand);
            }
            return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)itemStack);
        }
        MutableComponent chatComponent = Component.m_237115_((String)"chat.waystones.scroll_not_yet_bound");
        chatComponent.m_130940_(ChatFormatting.RED);
        player.m_5661_((Component)chatComponent, true);
        return new InteractionResultHolder(InteractionResult.FAIL, (Object)itemStack);
    }

    public void m_7373_(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        MutableComponent targetText;
        Player player = Balm.getProxy().getClientPlayer();
        if (player == null) {
            return;
        }
        IWaystone boundTo = this.getBoundTo(player, stack);
        MutableComponent mutableComponent = targetText = boundTo != null ? Component.m_237113_((String)boundTo.getName()) : Component.m_237115_((String)"tooltip.waystones.bound_to_none");
        if (boundTo != null) {
            targetText.m_130940_(ChatFormatting.AQUA);
        }
        MutableComponent boundToText = Component.m_237110_((String)"tooltip.waystones.bound_to", (Object[])new Object[]{targetText});
        boundToText.m_130940_(ChatFormatting.GRAY);
        list.add((Component)boundToText);
    }
}

