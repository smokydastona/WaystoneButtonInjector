/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.tag.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Waystone
implements IWaystone,
IMutableWaystone {
    private final ResourceLocation waystoneType;
    private final UUID waystoneUid;
    private final WaystoneOrigin origin;
    private ResourceKey<Level> dimension;
    private BlockPos pos;
    private String name = "";
    private boolean isGlobal;
    private UUID ownerUid;

    public Waystone(ResourceLocation waystoneType, UUID waystoneUid, ResourceKey<Level> dimension, BlockPos pos, WaystoneOrigin origin, @Nullable UUID ownerUid) {
        this.waystoneType = waystoneType;
        this.waystoneUid = waystoneUid;
        this.dimension = dimension;
        this.pos = pos;
        this.origin = origin;
        this.ownerUid = ownerUid;
    }

    @Override
    public UUID getWaystoneUid() {
        return this.waystoneUid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    @Override
    public WaystoneOrigin getOrigin() {
        return this.origin;
    }

    @Override
    public boolean isGlobal() {
        return this.isGlobal;
    }

    @Override
    public void setGlobal(boolean global) {
        this.isGlobal = global;
    }

    @Override
    public boolean isOwner(Player player) {
        return this.ownerUid == null || player.m_36316_().getId().equals(this.ownerUid) || player.m_150110_().f_35937_;
    }

    @Override
    public void setOwnerUid(@Nullable UUID ownerUid) {
        this.ownerUid = ownerUid;
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public UUID getOwnerUid() {
        return this.ownerUid;
    }

    @Override
    public void setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
    }

    @Override
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public ResourceLocation getWaystoneType() {
        return this.waystoneType;
    }

    @Override
    public boolean isValidInLevel(ServerLevel level) {
        BlockState state = level.m_8055_(this.pos);
        return state.m_204336_(ModBlockTags.IS_TELEPORT_TARGET);
    }

    @Override
    public TeleportDestination resolveDestination(ServerLevel level) {
        BlockState state = level.m_8055_(this.pos);
        Direction direction = (Direction)state.m_61143_((Property)WaystoneBlock.FACING);
        ArrayList directionCandidates = Lists.newArrayList((Object[])new Direction[]{direction, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH});
        for (Direction candidate : directionCandidates) {
            BlockPos offsetPos = this.pos.m_121945_(candidate);
            BlockPos offsetPosUp = offsetPos.m_7494_();
            if (level.m_8055_(offsetPos).m_60828_((BlockGetter)level, offsetPos) || level.m_8055_(offsetPosUp).m_60828_((BlockGetter)level, offsetPosUp)) continue;
            direction = candidate;
            break;
        }
        BlockPos targetPos = this.getWaystoneType().equals((Object)WaystoneTypes.WARP_PLATE) ? this.getPos() : this.getPos().m_121945_(direction);
        Vec3 location = new Vec3((double)targetPos.m_123341_() + 0.5, (double)targetPos.m_123342_() + 0.5, (double)targetPos.m_123343_() + 0.5);
        return new TeleportDestination(level, location, direction);
    }

    public static IWaystone read(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.m_130259_();
        ResourceLocation waystoneType = buf.m_130281_();
        String name = buf.m_130277_();
        boolean isGlobal = buf.readBoolean();
        ResourceKey dimension = ResourceKey.m_135785_((ResourceKey)Registries.f_256858_, (ResourceLocation)new ResourceLocation(buf.m_130136_(250)));
        BlockPos pos = buf.m_130135_();
        WaystoneOrigin origin = (WaystoneOrigin)buf.m_130066_(WaystoneOrigin.class);
        Waystone waystone = new Waystone(waystoneType, waystoneUid, (ResourceKey<Level>)dimension, pos, origin, null);
        waystone.setName(name);
        waystone.setGlobal(isGlobal);
        return waystone;
    }

    public static IWaystone read(CompoundTag compound) {
        WaystoneOrigin origin;
        UUID waystoneUid = NbtUtils.m_129233_((Tag)Objects.requireNonNull(compound.m_128423_("WaystoneUid")));
        String name = compound.m_128461_("Name");
        ResourceKey dimensionType = ResourceKey.m_135785_((ResourceKey)Registries.f_256858_, (ResourceLocation)new ResourceLocation(compound.m_128461_("World")));
        BlockPos pos = NbtUtils.m_129239_((CompoundTag)compound.m_128469_("BlockPos"));
        boolean wasGenerated = compound.m_128471_("WasGenerated");
        WaystoneOrigin waystoneOrigin = origin = wasGenerated ? WaystoneOrigin.WILDERNESS : WaystoneOrigin.UNKNOWN;
        if (compound.m_128441_("Origin")) {
            try {
                origin = WaystoneOrigin.valueOf(compound.m_128461_("Origin"));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                // empty catch block
            }
        }
        UUID ownerUid = compound.m_128441_("OwnerUid") ? NbtUtils.m_129233_((Tag)Objects.requireNonNull(compound.m_128423_("OwnerUid"))) : null;
        ResourceLocation waystoneType = compound.m_128441_("Type") ? new ResourceLocation(compound.m_128461_("Type")) : WaystoneTypes.WAYSTONE;
        Waystone waystone = new Waystone(waystoneType, waystoneUid, (ResourceKey<Level>)dimensionType, pos, origin, ownerUid);
        waystone.setName(name);
        waystone.setGlobal(compound.m_128471_("IsGlobal"));
        return waystone;
    }

    public static void write(FriendlyByteBuf buf, IWaystone waystone) {
        buf.m_130077_(waystone.getWaystoneUid());
        buf.m_130085_(waystone.getWaystoneType());
        buf.m_130070_(waystone.getName());
        buf.writeBoolean(waystone.isGlobal());
        buf.m_130085_(waystone.getDimension().m_135782_());
        buf.m_130064_(waystone.getPos());
        buf.m_130068_((Enum)waystone.getOrigin());
    }

    public static CompoundTag write(IWaystone waystone, CompoundTag compound) {
        compound.m_128365_("WaystoneUid", (Tag)NbtUtils.m_129226_((UUID)waystone.getWaystoneUid()));
        compound.m_128359_("Type", waystone.getWaystoneType().toString());
        compound.m_128359_("Name", waystone.getName());
        compound.m_128359_("World", waystone.getDimension().m_135782_().toString());
        compound.m_128365_("BlockPos", (Tag)NbtUtils.m_129224_((BlockPos)waystone.getPos()));
        compound.m_128359_("Origin", waystone.getOrigin().name());
        if (waystone.getOwnerUid() != null) {
            compound.m_128365_("OwnerUid", (Tag)NbtUtils.m_129226_((UUID)waystone.getOwnerUid()));
        }
        compound.m_128379_("IsGlobal", waystone.isGlobal());
        return compound;
    }
}

