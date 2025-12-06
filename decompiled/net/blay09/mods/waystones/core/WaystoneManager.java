/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.saveddata.SavedData
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.Waystone;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class WaystoneManager
extends SavedData {
    private static final String DATA_NAME = "waystones";
    private static final String TAG_WAYSTONES = "Waystones";
    private static final WaystoneManager clientStorageCopy = new WaystoneManager();
    private final Map<UUID, IWaystone> waystones = new HashMap<UUID, IWaystone>();

    public void addWaystone(IWaystone waystone) {
        this.waystones.put(waystone.getWaystoneUid(), waystone);
        this.m_77762_();
    }

    public void updateWaystone(IWaystone waystone) {
        Waystone mutableWaystone = (Waystone)this.waystones.getOrDefault(waystone.getWaystoneUid(), waystone);
        mutableWaystone.setName(waystone.getName());
        mutableWaystone.setGlobal(waystone.isGlobal());
        this.waystones.put(waystone.getWaystoneUid(), mutableWaystone);
        this.m_77762_();
    }

    public void removeWaystone(IWaystone waystone) {
        this.waystones.remove(waystone.getWaystoneUid());
        this.m_77762_();
    }

    public Optional<IWaystone> getWaystoneAt(BlockGetter world, BlockPos pos) {
        BlockEntity blockEntity = world.m_7702_(pos);
        if (blockEntity instanceof WaystoneBlockEntityBase) {
            return Optional.of(((WaystoneBlockEntityBase)blockEntity).getWaystone());
        }
        return Optional.empty();
    }

    public Optional<IWaystone> getWaystoneById(UUID waystoneUid) {
        return Optional.ofNullable(this.waystones.get(waystoneUid));
    }

    public Optional<IWaystone> findWaystoneByName(String name) {
        return this.waystones.values().stream().filter(it -> it.getName().equals(name)).findFirst();
    }

    public Stream<IWaystone> getWaystones() {
        return this.waystones.values().stream();
    }

    public Stream<IWaystone> getWaystonesByType(ResourceLocation type) {
        return this.getWaystones().filter(it -> it.getWaystoneType().equals((Object)type));
    }

    public List<IWaystone> getGlobalWaystones() {
        return this.waystones.values().stream().filter(IWaystone::isGlobal).collect(Collectors.toList());
    }

    public static WaystoneManager read(CompoundTag tagCompound) {
        WaystoneManager waystoneManager = new WaystoneManager();
        ListTag tagList = tagCompound.m_128437_(TAG_WAYSTONES, 10);
        for (Tag tag : tagList) {
            CompoundTag compound = (CompoundTag)tag;
            IWaystone waystone = Waystone.read(compound);
            waystoneManager.waystones.put(waystone.getWaystoneUid(), waystone);
        }
        return waystoneManager;
    }

    public CompoundTag m_7176_(CompoundTag tagCompound) {
        ListTag tagList = new ListTag();
        for (IWaystone waystone : this.waystones.values()) {
            tagList.add((Object)Waystone.write(waystone, new CompoundTag()));
        }
        tagCompound.m_128365_(TAG_WAYSTONES, (Tag)tagList);
        return tagCompound;
    }

    public static WaystoneManager get(@Nullable MinecraftServer server) {
        if (server != null) {
            ServerLevel overworld = server.m_129880_(Level.f_46428_);
            return (WaystoneManager)Objects.requireNonNull(overworld).m_8895_().m_164861_(WaystoneManager::read, WaystoneManager::new, DATA_NAME);
        }
        return clientStorageCopy;
    }
}

