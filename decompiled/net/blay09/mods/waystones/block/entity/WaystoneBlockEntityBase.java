/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.block.entity.CustomRenderBoundingBox
 *  net.blay09.mods.balm.api.block.entity.OnLoadHandler
 *  net.blay09.mods.balm.common.BalmBlockEntity
 *  net.minecraft.core.BlockPos
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block.entity;

import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.balm.api.block.entity.CustomRenderBoundingBox;
import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.core.InvalidWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public abstract class WaystoneBlockEntityBase
extends BalmBlockEntity
implements OnLoadHandler,
CustomRenderBoundingBox {
    private IWaystone waystone = InvalidWaystone.INSTANCE;
    private UUID waystoneUid;
    private boolean shouldNotInitialize;
    private boolean silkTouched;

    public WaystoneBlockEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    protected void m_183515_(CompoundTag tag) {
        super.m_183515_(tag);
        if (this.waystone.isValid()) {
            tag.m_128365_("UUID", (Tag)NbtUtils.m_129226_((UUID)this.waystone.getWaystoneUid()));
        } else if (this.waystoneUid != null) {
            tag.m_128365_("UUID", (Tag)NbtUtils.m_129226_((UUID)this.waystoneUid));
        }
    }

    public void m_142466_(CompoundTag compound) {
        super.m_142466_(compound);
        if (compound.m_128425_("UUID", 11)) {
            this.waystoneUid = NbtUtils.m_129233_((Tag)Objects.requireNonNull(compound.m_128423_("UUID")));
        }
        if (compound.m_128425_("Waystone", 10)) {
            IWaystone syncedWaystone = Waystone.read(compound.m_128469_("Waystone"));
            WaystoneManager.get(null).updateWaystone(syncedWaystone);
            this.waystone = new WaystoneProxy(null, syncedWaystone.getWaystoneUid());
        }
    }

    public void writeUpdateTag(CompoundTag tag) {
        tag.m_128365_("Waystone", (Tag)Waystone.write(this.getWaystone(), new CompoundTag()));
    }

    public void onLoad() {
        IWaystone backingWaystone = this.waystone;
        if (this.waystone instanceof WaystoneProxy) {
            backingWaystone = ((WaystoneProxy)this.waystone).getBackingWaystone();
        }
        if (backingWaystone instanceof Waystone && this.f_58857_ != null) {
            ((Waystone)backingWaystone).setDimension((ResourceKey<Level>)this.f_58857_.m_46472_());
            ((Waystone)backingWaystone).setPos(this.f_58858_);
        }
        this.sync();
    }

    public AABB getRenderBoundingBox() {
        return new AABB((double)this.f_58858_.m_123341_(), (double)this.f_58858_.m_123342_(), (double)this.f_58858_.m_123343_(), (double)(this.f_58858_.m_123341_() + 1), (double)(this.f_58858_.m_123342_() + 2), (double)(this.f_58858_.m_123343_() + 1));
    }

    public IWaystone getWaystone() {
        if (!(this.waystone.isValid() || this.f_58857_ == null || this.f_58857_.f_46443_ || this.shouldNotInitialize)) {
            BlockState state;
            if (this.waystoneUid != null) {
                this.waystone = new WaystoneProxy(this.f_58857_.m_7654_(), this.waystoneUid);
            }
            if (!this.waystone.isValid() && (state = this.m_58900_()).m_60734_() instanceof WaystoneBlockBase) {
                BlockEntity blockEntity;
                WaystoneOrigin origin;
                DoubleBlockHalf half = state.m_61138_(WaystoneBlockBase.HALF) ? (DoubleBlockHalf)state.m_61143_(WaystoneBlockBase.HALF) : DoubleBlockHalf.LOWER;
                WaystoneOrigin waystoneOrigin = origin = state.m_61138_(WaystoneBlockBase.ORIGIN) ? (WaystoneOrigin)((Object)state.m_61143_(WaystoneBlockBase.ORIGIN)) : WaystoneOrigin.UNKNOWN;
                if (half == DoubleBlockHalf.LOWER) {
                    this.initializeWaystone((ServerLevelAccessor)Objects.requireNonNull(this.f_58857_), null, origin);
                } else if (half == DoubleBlockHalf.UPPER && (blockEntity = this.f_58857_.m_7702_(this.f_58858_.m_7495_())) instanceof WaystoneBlockEntityBase) {
                    this.initializeFromBase((WaystoneBlockEntityBase)blockEntity);
                }
            }
            if (this.waystone.isValid()) {
                this.waystoneUid = this.waystone.getWaystoneUid();
                this.sync();
            }
        }
        return this.waystone;
    }

    protected abstract ResourceLocation getWaystoneType();

    public void initializeWaystone(ServerLevelAccessor world, @Nullable LivingEntity player, WaystoneOrigin origin) {
        Waystone waystone = new Waystone(this.getWaystoneType(), UUID.randomUUID(), (ResourceKey<Level>)world.m_6018_().m_46472_(), this.f_58858_, origin, player != null ? player.m_20148_() : null);
        WaystoneManager.get(world.m_7654_()).addWaystone(waystone);
        this.waystone = waystone;
        this.m_6596_();
        this.sync();
    }

    public void initializeFromExisting(ServerLevelAccessor world, Waystone existingWaystone, ItemStack itemStack) {
        this.waystone = existingWaystone;
        existingWaystone.setDimension((ResourceKey<Level>)world.m_6018_().m_46472_());
        existingWaystone.setPos(this.f_58858_);
        this.m_6596_();
        this.sync();
    }

    public void initializeFromBase(WaystoneBlockEntityBase tileEntity) {
        this.waystone = tileEntity.getWaystone();
        this.m_6596_();
        this.sync();
    }

    public void uninitializeWaystone() {
        if (this.waystone.isValid()) {
            WaystoneManager.get(this.f_58857_.m_7654_()).removeWaystone(this.waystone);
            PlayerWaystoneManager.removeKnownWaystone(this.f_58857_.m_7654_(), this.waystone);
        }
        this.waystone = InvalidWaystone.INSTANCE;
        this.shouldNotInitialize = true;
        DoubleBlockHalf half = (DoubleBlockHalf)this.m_58900_().m_61143_((Property)WaystoneBlock.HALF);
        BlockPos otherPos = half == DoubleBlockHalf.UPPER ? this.f_58858_.m_7495_() : this.f_58858_.m_7494_();
        BlockEntity blockEntity = Objects.requireNonNull(this.f_58857_).m_7702_(otherPos);
        if (blockEntity instanceof WaystoneBlockEntityBase) {
            WaystoneBlockEntityBase waystoneTile = (WaystoneBlockEntityBase)blockEntity;
            waystoneTile.waystone = InvalidWaystone.INSTANCE;
            waystoneTile.shouldNotInitialize = true;
        }
        this.m_6596_();
        this.sync();
    }

    public void setSilkTouched(boolean silkTouched) {
        this.silkTouched = silkTouched;
    }

    public boolean isSilkTouched() {
        return this.silkTouched;
    }

    public abstract MenuProvider getMenuProvider();

    @Nullable
    public abstract MenuProvider getSettingsMenuProvider();
}

