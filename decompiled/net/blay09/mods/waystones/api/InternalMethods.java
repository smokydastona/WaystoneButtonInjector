/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.minecraft.core.BlockPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.api;

import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.IWaystoneTeleportContext;
import net.blay09.mods.waystones.api.WaystoneStyle;
import net.blay09.mods.waystones.api.WaystoneTeleportError;
import net.blay09.mods.waystones.core.WarpMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface InternalMethods {
    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createDefaultTeleportContext(Entity var1, IWaystone var2, WarpMode var3, @Nullable IWaystone var4);

    public Either<IWaystoneTeleportContext, WaystoneTeleportError> createCustomTeleportContext(Entity var1, IWaystone var2);

    public Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity var1, IWaystone var2, WarpMode var3, IWaystone var4);

    public Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext var1);

    public Either<List<Entity>, WaystoneTeleportError> forceTeleportToWaystone(Entity var1, IWaystone var2);

    public List<Entity> forceTeleport(IWaystoneTeleportContext var1);

    public Optional<IWaystone> getWaystoneAt(Level var1, BlockPos var2);

    public Optional<IWaystone> getWaystoneAt(MinecraftServer var1, BlockGetter var2, BlockPos var3);

    public Optional<IWaystone> getWaystone(Level var1, UUID var2);

    public Optional<IWaystone> getWaystone(MinecraftServer var1, UUID var2);

    public ItemStack createAttunedShard(IWaystone var1);

    public ItemStack createBoundScroll(IWaystone var1);

    public Optional<IWaystone> placeWaystone(Level var1, BlockPos var2, WaystoneStyle var3);

    public Optional<IWaystone> placeSharestone(Level var1, BlockPos var2, DyeColor var3);

    public Optional<IWaystone> placeWarpPlate(Level var1, BlockPos var2);

    public Optional<IWaystone> getBoundWaystone(ItemStack var1);

    public void setBoundWaystone(ItemStack var1, @Nullable IWaystone var2);

    public boolean isWaystoneActivated(Player var1, IWaystone var2);

    public Collection<IWaystone> getActivatedWaystones(Player var1);

    public Optional<IWaystone> getNearestWaystone(Player var1);

    public Stream<IWaystone> getAllWaystones(MinecraftServer var1);

    public Stream<IWaystone> getWaystonesByType(MinecraftServer var1, ResourceLocation var2);

    public void removeWaystoneFromDatabase(MinecraftServer var1, IWaystone var2);
}

