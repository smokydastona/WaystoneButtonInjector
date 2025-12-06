/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.feature.ConfiguredFeature
 *  net.minecraft.world.phys.BlockHitResult
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public interface BalmHooks {
    public boolean blockGrowFeature(Level var1, RandomSource var2, BlockPos var3, @Nullable Holder<ConfiguredFeature<?, ?>> var4);

    public boolean growCrop(ItemStack var1, Level var2, BlockPos var3, @Nullable Player var4);

    default public CompoundTag getPersistentData(Player player) {
        return this.getPersistentData((Entity)player);
    }

    public CompoundTag getPersistentData(Entity var1);

    public void curePotionEffects(LivingEntity var1, ItemStack var2);

    public boolean isFakePlayer(Player var1);

    public ItemStack getCraftingRemainingItem(ItemStack var1);

    public DyeColor getColor(ItemStack var1);

    public boolean canItemsStack(ItemStack var1, ItemStack var2);

    public int getBurnTime(ItemStack var1);

    public void setBurnTime(Item var1, int var2);

    public void firePlayerCraftingEvent(Player var1, ItemStack var2, Container var3);

    public boolean useFluidTank(BlockState var1, Level var2, BlockPos var3, Player var4, InteractionHand var5, BlockHitResult var6);

    public boolean isShield(ItemStack var1);

    public void setForcedPose(Player var1, Pose var2);

    public MinecraftServer getServer();

    default public double getBlockReachDistance(Player player) {
        return 4.5 + (player.m_7500_() ? 0.5 : 0.0);
    }

    public boolean isRepairable(ItemStack var1);
}

