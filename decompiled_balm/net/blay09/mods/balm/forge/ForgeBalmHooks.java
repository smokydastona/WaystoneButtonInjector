/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Holder
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BoneMealItem
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.levelgen.feature.ConfiguredFeature
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraftforge.common.ForgeHooks
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.ToolActions
 *  net.minecraftforge.common.util.FakePlayer
 *  net.minecraftforge.event.ForgeEventFactory
 *  net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent
 *  net.minecraftforge.eventbus.api.Event$Result
 *  net.minecraftforge.fluids.FluidUtil
 *  net.minecraftforge.items.ItemHandlerHelper
 *  net.minecraftforge.server.ServerLifecycleHooks
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge;

import java.util.HashMap;
import java.util.Map;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.entity.BalmEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmHooks
implements BalmHooks {
    public final Map<Item, Integer> burnTimes = new HashMap<Item, Integer>();

    public ForgeBalmHooks() {
        MinecraftForge.EVENT_BUS.addListener(this::furnaceFuelBurnTime);
    }

    private void furnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        Integer found = this.burnTimes.get(event.getItemStack().m_41720_());
        if (found != null) {
            event.setBurnTime(found.intValue());
        }
    }

    @Override
    public boolean blockGrowFeature(Level level, RandomSource random, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder) {
        return !ForgeEventFactory.blockGrowFeature((LevelAccessor)level, (RandomSource)random, (BlockPos)pos, holder).getResult().equals((Object)Event.Result.DENY);
    }

    @Override
    public boolean growCrop(ItemStack itemStack, Level level, BlockPos pos, Player player) {
        if (player != null) {
            return BoneMealItem.applyBonemeal((ItemStack)itemStack, (Level)level, (BlockPos)pos, (Player)player);
        }
        return BoneMealItem.m_40627_((ItemStack)itemStack, (Level)level, (BlockPos)pos);
    }

    @Override
    public CompoundTag getPersistentData(Entity entity) {
        CompoundTag balmData;
        CompoundTag persistentData = entity.getPersistentData();
        if (entity instanceof Player) {
            CompoundTag persistedTag = persistentData.m_128469_("PlayerPersisted");
            persistentData.m_128365_("PlayerPersisted", (Tag)persistedTag);
            persistentData = persistedTag;
        }
        if ((balmData = persistentData.m_128469_("BalmData")).m_128440_() == 0) {
            balmData = ((BalmEntity)entity).getFabricBalmData();
        }
        persistentData.m_128365_("BalmData", (Tag)balmData);
        return balmData;
    }

    @Override
    public void curePotionEffects(LivingEntity entity, ItemStack curativeItem) {
        entity.curePotionEffects(curativeItem);
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.getCraftingRemainingItem();
    }

    @Override
    public DyeColor getColor(ItemStack itemStack) {
        return DyeColor.getColor((ItemStack)itemStack);
    }

    @Override
    public boolean canItemsStack(ItemStack first, ItemStack second) {
        return ItemHandlerHelper.canItemStacksStack((ItemStack)first, (ItemStack)second);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return ForgeHooks.getBurnTime((ItemStack)itemStack, (RecipeType)RecipeType.f_44108_);
    }

    @Override
    public void setBurnTime(Item item, int burnTime) {
        this.burnTimes.put(item, burnTime);
    }

    @Override
    public void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        ForgeEventFactory.firePlayerCraftingEvent((Player)player, (ItemStack)crafted, (Container)craftMatrix);
    }

    @Override
    public boolean useFluidTank(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return FluidUtil.interactWithFluidHandler((Player)player, (InteractionHand)hand, (Level)level, (BlockPos)pos, (Direction)hitResult.m_82434_());
    }

    @Override
    public boolean isShield(ItemStack itemStack) {
        return itemStack.m_41720_().canPerformAction(itemStack, ToolActions.SHIELD_BLOCK);
    }

    @Override
    public boolean isRepairable(ItemStack itemStack) {
        return itemStack.isRepairable();
    }

    @Override
    public void setForcedPose(Player player, Pose pose) {
        player.setForcedPose(pose);
    }

    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public double getBlockReachDistance(Player player) {
        return player.getBlockReach();
    }
}

