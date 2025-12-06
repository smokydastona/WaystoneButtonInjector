/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.waystones.item;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IResetUseOnDamage;
import net.blay09.mods.waystones.compat.Compat;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.item.ScrollItemBase;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WarpScrollItem
extends ScrollItemBase
implements IResetUseOnDamage {
    private static final BalmMenuProvider containerProvider = new BalmMenuProvider(){

        public Component m_5446_() {
            return Component.m_237115_((String)"container.waystones.waystone_selection");
        }

        public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
            return WaystoneSelectionMenu.createWaystoneSelection(i, playerEntity, WarpMode.WARP_SCROLL, null);
        }

        public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            buf.writeByte(WarpMode.WARP_SCROLL.ordinal());
        }
    };

    public WarpScrollItem(Item.Properties properties) {
        super(properties);
    }

    public int m_8105_(ItemStack itemStack) {
        return WaystonesConfig.getActive().cooldowns.scrollUseTime;
    }

    public ItemStack m_5922_(ItemStack itemStack, Level world, LivingEntity entity) {
        if (!world.f_46443_ && entity instanceof ServerPlayer) {
            Balm.getNetworking().openGui((Player)((ServerPlayer)entity), (MenuProvider)containerProvider);
        }
        return itemStack;
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!player.m_6117_() && !world.f_46443_) {
            world.m_6269_(null, (Entity)player, SoundEvents.f_12288_, SoundSource.PLAYERS, 0.1f, 2.0f);
        }
        if (this.m_8105_(itemStack) <= 0 || Compat.isVivecraftInstalled) {
            this.m_5922_(itemStack, world, (LivingEntity)player);
        } else {
            player.m_6672_(hand);
        }
        return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)itemStack);
    }

    public boolean m_5812_(ItemStack itemStack) {
        return true;
    }
}

