/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones;

import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IAttunementItem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.IWaystoneTeleportContext;
import net.blay09.mods.waystones.api.InternalMethods;
import net.blay09.mods.waystones.api.WaystoneStyle;
import net.blay09.mods.waystones.api.WaystoneTeleportError;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneTeleportContext;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class InternalMethodsImpl
implements InternalMethods {
    @Override
    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createDefaultTeleportContext(Entity entity, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        return WaystonesAPI.createCustomTeleportContext(entity, waystone).ifLeft(context -> {
            context.setWarpMode(warpMode);
            context.getLeashedEntities().addAll(PlayerWaystoneManager.findLeashedAnimals(entity));
            context.setFromWaystone(fromWaystone);
            context.setWarpItem(PlayerWaystoneManager.findWarpItem(entity, warpMode));
            context.setCooldown(PlayerWaystoneManager.getCooldownPeriod(warpMode, waystone));
            context.setXpCost(PlayerWaystoneManager.getExperienceLevelCost(entity, waystone, warpMode, context));
        });
    }

    @Override
    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createCustomTeleportContext(Entity entity, IWaystone waystone) {
        if (!waystone.isValid()) {
            return Either.right((Object)new WaystoneTeleportError.InvalidWaystone(waystone));
        }
        MinecraftServer server = entity.m_20194_();
        if (server == null) {
            return Either.right((Object)new WaystoneTeleportError.NotOnServer());
        }
        ServerLevel targetLevel = server.m_129880_(waystone.getDimension());
        if (targetLevel == null) {
            return Either.right((Object)new WaystoneTeleportError.InvalidDimension(waystone.getDimension()));
        }
        if (!waystone.isValidInLevel(targetLevel)) {
            return Either.right((Object)new WaystoneTeleportError.MissingWaystone(waystone));
        }
        return Either.left((Object)new WaystoneTeleportContext(entity, waystone, waystone.resolveDestination(targetLevel)));
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity entity, IWaystone waystone, WarpMode warpMode, IWaystone fromWaystone) {
        return PlayerWaystoneManager.tryTeleportToWaystone(entity, waystone, warpMode, fromWaystone);
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext context) {
        return PlayerWaystoneManager.tryTeleport(context);
    }

    @Override
    public Either<List<Entity>, WaystoneTeleportError> forceTeleportToWaystone(Entity entity, IWaystone waystone) {
        return this.createDefaultTeleportContext(entity, waystone, WarpMode.CUSTOM, null).mapLeft(this::forceTeleport);
    }

    @Override
    public List<Entity> forceTeleport(IWaystoneTeleportContext context) {
        return PlayerWaystoneManager.doTeleport(context);
    }

    @Override
    public Optional<IWaystone> getWaystoneAt(Level level, BlockPos pos) {
        return WaystoneManager.get(level.m_7654_()).getWaystoneAt((BlockGetter)level, pos);
    }

    @Override
    public Optional<IWaystone> getWaystoneAt(MinecraftServer server, BlockGetter level, BlockPos pos) {
        return WaystoneManager.get(server).getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> getWaystone(Level level, UUID uuid) {
        return WaystoneManager.get(level.m_7654_()).getWaystoneById(uuid);
    }

    @Override
    public Optional<IWaystone> getWaystone(MinecraftServer server, UUID uuid) {
        return WaystoneManager.get(server).getWaystoneById(uuid);
    }

    @Override
    public ItemStack createAttunedShard(IWaystone warpPlate) {
        ItemStack itemStack = new ItemStack((ItemLike)ModItems.attunedShard);
        this.setBoundWaystone(itemStack, warpPlate);
        return itemStack;
    }

    @Override
    public ItemStack createBoundScroll(IWaystone waystone) {
        ItemStack itemStack = new ItemStack((ItemLike)ModItems.boundScroll);
        this.setBoundWaystone(itemStack, waystone);
        return itemStack;
    }

    @Override
    public Optional<IWaystone> placeWaystone(Level level, BlockPos pos, WaystoneStyle style) {
        Block block = Balm.getRegistries().getBlock(style.getBlockRegistryName());
        level.m_7731_(pos, (BlockState)block.m_49966_().m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.LOWER), 3);
        level.m_7731_(pos.m_7494_(), (BlockState)block.m_49966_().m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> placeSharestone(Level level, BlockPos pos, @Nullable DyeColor color) {
        Block sharestone = color != null ? ModBlocks.scopedSharestones[color.ordinal()] : ModBlocks.sharestone;
        level.m_7731_(pos, (BlockState)sharestone.m_49966_().m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.LOWER), 3);
        level.m_7731_(pos.m_7494_(), (BlockState)sharestone.m_49966_().m_61124_((Property)WaystoneBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> placeWarpPlate(Level level, BlockPos pos) {
        level.m_7731_(pos, ModBlocks.warpPlate.m_49966_(), 3);
        return this.getWaystoneAt(level, pos);
    }

    @Override
    public Optional<IWaystone> getBoundWaystone(ItemStack itemStack) {
        Item item = itemStack.m_41720_();
        if (item instanceof IAttunementItem) {
            IAttunementItem attunementItem = (IAttunementItem)item;
            return Optional.ofNullable(attunementItem.getWaystoneAttunedTo(Balm.getHooks().getServer(), itemStack));
        }
        return Optional.empty();
    }

    @Override
    public void setBoundWaystone(ItemStack itemStack, @Nullable IWaystone waystone) {
        Item item = itemStack.m_41720_();
        if (item instanceof IAttunementItem) {
            IAttunementItem attunementItem = (IAttunementItem)item;
            attunementItem.setWaystoneAttunedTo(itemStack, waystone);
        }
    }

    @Override
    public boolean isWaystoneActivated(Player player, IWaystone waystone) {
        return PlayerWaystoneManager.isWaystoneActivated(player, waystone);
    }

    @Override
    public Collection<IWaystone> getActivatedWaystones(Player player) {
        return PlayerWaystoneManager.getWaystones(player);
    }

    @Override
    public Optional<IWaystone> getNearestWaystone(Player player) {
        return Optional.ofNullable(PlayerWaystoneManager.getNearestWaystone(player));
    }

    @Override
    public Stream<IWaystone> getAllWaystones(MinecraftServer server) {
        return WaystoneManager.get(server).getWaystones();
    }

    @Override
    public Stream<IWaystone> getWaystonesByType(MinecraftServer server, ResourceLocation type) {
        return WaystoneManager.get(server).getWaystonesByType(type);
    }

    @Override
    public void removeWaystoneFromDatabase(MinecraftServer server, IWaystone waystone) {
        WaystoneManager.get(server).removeWaystone(waystone);
        PlayerWaystoneManager.removeKnownWaystone(server, waystone);
    }
}

