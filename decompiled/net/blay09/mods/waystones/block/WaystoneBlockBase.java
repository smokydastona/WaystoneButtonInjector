/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Direction$Axis
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtUtils
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.context.BlockPlaceContext
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.BaseEntityBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.Mirror
 *  net.minecraft.world.level.block.ObserverBlock
 *  net.minecraft.world.level.block.RenderShape
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BlockStateProperties
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.DirectionProperty
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.FluidState
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.material.PushReaction
 *  net.minecraft.world.phys.BlockHitResult
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block;

import java.util.List;
import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class WaystoneBlockBase
extends BaseEntityBlock
implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.f_61374_;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.f_61401_;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.f_61362_;
    public static final EnumProperty<WaystoneOrigin> ORIGIN = EnumProperty.m_61587_((String)"origin", WaystoneOrigin.class);

    public WaystoneBlockBase(BlockBehaviour.Properties properties) {
        super(properties.m_278166_(PushReaction.BLOCK));
        this.m_49959_((BlockState)((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)FACING, (Comparable)Direction.NORTH)).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false))).m_61124_(ORIGIN, (Comparable)((Object)WaystoneOrigin.UNKNOWN)));
    }

    public BlockState m_7417_(BlockState state, Direction direction, BlockState directionState, LevelAccessor world, BlockPos pos, BlockPos directionPos) {
        if (((Boolean)state.m_61143_((Property)WATERLOGGED)).booleanValue()) {
            world.m_186469_(pos, (Fluid)Fluids.f_76193_, Fluids.f_76193_.m_6718_((LevelReader)world));
        }
        if (this.isDoubleBlock(state)) {
            DoubleBlockHalf half = (DoubleBlockHalf)state.m_61143_(HALF);
            if ((direction.m_122434_() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (direction == Direction.UP) || directionState.m_60734_() == this && directionState.m_61143_(HALF) != half) && (half != DoubleBlockHalf.LOWER || direction != Direction.DOWN || state.m_60710_((LevelReader)world, pos))) {
                return state;
            }
            return Blocks.f_50016_.m_49966_();
        }
        return state;
    }

    public void m_6240_(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (this.isDoubleBlock(state)) {
            super.m_6240_(world, player, pos, Blocks.f_50016_.m_49966_(), blockEntity, stack);
        } else {
            super.m_6240_(world, player, pos, state, blockEntity, stack);
        }
    }

    private boolean isDoubleBlock(BlockState state) {
        return state.m_61138_(HALF);
    }

    protected boolean canSilkTouch() {
        return false;
    }

    public void m_5707_(Level world, BlockPos pos, BlockState state, Player player) {
        BlockState offsetState;
        boolean hasSilkTouch;
        BlockEntity blockEntity = world.m_7702_(pos);
        boolean isDoubleBlock = this.isDoubleBlock(state);
        DoubleBlockHalf half = isDoubleBlock ? (DoubleBlockHalf)state.m_61143_(HALF) : null;
        BlockPos offset = half == DoubleBlockHalf.LOWER ? pos.m_7494_() : pos.m_7495_();
        BlockEntity offsetTileEntity = isDoubleBlock ? world.m_7702_(offset) : null;
        boolean bl = hasSilkTouch = EnchantmentHelper.m_44836_((Enchantment)Enchantments.f_44985_, (LivingEntity)player) > 0;
        if (hasSilkTouch && this.canSilkTouch()) {
            if (blockEntity instanceof WaystoneBlockEntityBase) {
                ((WaystoneBlockEntityBase)blockEntity).setSilkTouched(true);
            }
            if (isDoubleBlock && offsetTileEntity instanceof WaystoneBlockEntityBase) {
                ((WaystoneBlockEntityBase)offsetTileEntity).setSilkTouched(true);
            }
        }
        if (isDoubleBlock && (offsetState = world.m_8055_(offset)).m_60734_() == this && offsetState.m_61143_(HALF) != half) {
            world.m_46953_(half == DoubleBlockHalf.LOWER ? pos : offset, false, (Entity)player);
            if (!world.f_46443_ && !player.m_150110_().f_35937_) {
                WaystoneBlockBase.m_49881_((BlockState)state, (Level)world, (BlockPos)pos, (BlockEntity)blockEntity, (Entity)player, (ItemStack)player.m_21205_());
                WaystoneBlockBase.m_49881_((BlockState)offsetState, (Level)world, (BlockPos)offset, (BlockEntity)offsetTileEntity, (Entity)player, (ItemStack)player.m_21205_());
            }
        }
        super.m_5707_(world, pos, state, player);
    }

    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        builder.m_61104_(new Property[]{FACING, WATERLOGGED, ORIGIN});
    }

    public float m_5880_(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        if (!PlayerWaystoneManager.mayBreakWaystone(player, world, pos)) {
            return -1.0f;
        }
        return super.m_5880_(state, player, world, pos);
    }

    public boolean m_7898_(BlockState state, LevelReader world, BlockPos pos) {
        if (!this.isDoubleBlock(state)) {
            return true;
        }
        if (state.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
            return true;
        }
        BlockState below = world.m_8055_(pos.m_7495_());
        return below.m_60734_() == this && below.m_61143_(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    public BlockState m_5573_(BlockPlaceContext context) {
        if (!PlayerWaystoneManager.mayPlaceWaystone(context.m_43723_())) {
            return null;
        }
        Level world = context.m_43725_();
        BlockPos pos = context.m_8083_();
        FluidState fluidState = world.m_6425_(pos);
        if (pos.m_123342_() < world.m_141928_() - 1 && world.m_8055_(pos.m_7494_()).m_60629_(context)) {
            return (BlockState)((BlockState)((BlockState)this.m_49966_().m_61124_((Property)FACING, (Comparable)context.m_8125_().m_122424_())).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidState.m_76152_() == Fluids.f_76193_))).m_61124_(ORIGIN, (Comparable)((Object)WaystoneOrigin.PLAYER));
        }
        return null;
    }

    public FluidState m_5888_(BlockState state) {
        return (Boolean)state.m_61143_((Property)WATERLOGGED) != false ? Fluids.f_76193_.m_76068_(false) : super.m_5888_(state);
    }

    protected void notifyObserversOfAction(Level world, BlockPos pos) {
        if (!world.f_46443_) {
            for (Direction direction : Direction.values()) {
                BlockPos offset = pos.m_121945_(direction);
                BlockState neighbourState = world.m_8055_(offset);
                Block neighbourBlock = neighbourState.m_60734_();
                if (!(neighbourBlock instanceof ObserverBlock) || neighbourState.m_61143_((Property)ObserverBlock.f_52588_) != direction.m_122424_() || world.m_183326_().m_183582_(offset, (Object)neighbourBlock)) continue;
                world.m_186460_(offset, neighbourBlock, 2);
            }
        }
    }

    @Nullable
    protected InteractionResult handleEditActions(Level world, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (player.m_6144_()) {
            MenuProvider settingsContainerProvider;
            WaystoneEditPermissions result = PlayerWaystoneManager.mayEditWaystone(player, world, waystone);
            if (result != WaystoneEditPermissions.ALLOW) {
                if (result.getLangKey() != null) {
                    MutableComponent chatComponent = Component.m_237115_((String)result.getLangKey());
                    chatComponent.m_130940_(ChatFormatting.RED);
                    player.m_5661_((Component)chatComponent, true);
                }
                return InteractionResult.SUCCESS;
            }
            if (!world.f_46443_ && (settingsContainerProvider = tileEntity.getSettingsMenuProvider()) != null) {
                Balm.getNetworking().openGui(player, settingsContainerProvider);
            }
            return InteractionResult.SUCCESS;
        }
        return null;
    }

    @Nullable
    protected InteractionResult handleDebugActions(Level world, Player player, InteractionHand hand, WaystoneBlockEntityBase tileEntity) {
        if (player.m_150110_().f_35937_) {
            ItemStack heldItem = player.m_21120_(hand);
            if (heldItem.m_41720_() == Items.f_41911_) {
                if (!world.f_46443_) {
                    tileEntity.uninitializeWaystone();
                    player.m_5661_((Component)Component.m_237113_((String)"Waystone was successfully reset - it will re-initialize once it is next loaded."), false);
                }
                return InteractionResult.SUCCESS;
            }
            if (heldItem.m_41720_() == Items.f_42398_) {
                if (!world.f_46443_) {
                    player.m_5661_((Component)Component.m_237113_((String)("Server UUID: " + String.valueOf(tileEntity.getWaystone().getWaystoneUid()))), false);
                }
                if (world.f_46443_) {
                    player.m_5661_((Component)Component.m_237113_((String)("Client UUID: " + String.valueOf(tileEntity.getWaystone().getWaystoneUid()))), false);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }

    @Nullable
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        return null;
    }

    public void m_6810_(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity;
        if (!(state.m_60713_(newState.m_60734_()) || !((blockEntity = world.m_7702_(pos)) instanceof WaystoneBlockEntityBase) || this.canSilkTouch() && ((WaystoneBlockEntityBase)blockEntity).isSilkTouched())) {
            IWaystone waystone = ((WaystoneBlockEntityBase)blockEntity).getWaystone();
            WaystoneManager.get(world.m_7654_()).removeWaystone(waystone);
            PlayerWaystoneManager.removeKnownWaystone(world.m_7654_(), waystone);
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    public void m_5871_(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        WaystoneProxy waystone;
        super.m_5871_(stack, world, list, flag);
        CompoundTag tag = stack.m_41783_();
        if (tag != null && tag.m_128425_("UUID", 11) && (waystone = new WaystoneProxy(null, NbtUtils.m_129233_((Tag)Objects.requireNonNull(tag.m_128423_("UUID"))))).isValid()) {
            this.addWaystoneNameToTooltip(list, waystone);
        }
    }

    protected void addWaystoneNameToTooltip(List<Component> tooltip, WaystoneProxy waystone) {
        MutableComponent component = Component.m_237113_((String)waystone.getName());
        component.m_130940_(ChatFormatting.AQUA);
        tooltip.add((Component)component);
    }

    public InteractionResult m_6227_(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = world.m_7702_(pos);
        if (blockEntity instanceof WaystoneBlockEntityBase) {
            WaystoneBlockEntityBase waystoneTileEntity = (WaystoneBlockEntityBase)blockEntity;
            InteractionResult result = this.handleDebugActions(world, player, hand, waystoneTileEntity);
            if (result != null) {
                return result;
            }
            IWaystone waystone = waystoneTileEntity.getWaystone();
            result = this.handleEditActions(world, player, waystoneTileEntity, waystone);
            if (result != null) {
                return result;
            }
            result = this.handleActivation(world, pos, player, waystoneTileEntity, waystone);
            if (result != null) {
                return result;
            }
        }
        return InteractionResult.FAIL;
    }

    public void m_6402_(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = world.m_7702_(pos);
        BlockPos posAbove = pos.m_7494_();
        boolean isDoubleBlock = this.isDoubleBlock(state);
        if (isDoubleBlock) {
            FluidState fluidStateAbove = world.m_6425_(posAbove);
            world.m_46597_(posAbove, (BlockState)((BlockState)((BlockState)state.m_61124_(HALF, (Comparable)DoubleBlockHalf.UPPER)).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(fluidStateAbove.m_76152_() == Fluids.f_76193_))).m_61124_(ORIGIN, (Comparable)((Object)WaystoneOrigin.PLAYER)));
        }
        if (blockEntity instanceof WaystoneBlockEntityBase) {
            MenuProvider settingsContainerProvider;
            WaystoneBlockEntityBase waystoneTileEntity;
            ServerPlayer player;
            WaystoneEditPermissions result;
            if (!world.f_46443_) {
                BlockEntity waystoneEntityAbove;
                CompoundTag tag = stack.m_41783_();
                WaystoneProxy existingWaystone = null;
                if (tag != null && tag.m_128425_("UUID", 11)) {
                    existingWaystone = new WaystoneProxy(world.m_7654_(), NbtUtils.m_129233_((Tag)Objects.requireNonNull(tag.m_128423_("UUID"))));
                }
                if (existingWaystone != null && existingWaystone.isValid() && existingWaystone.getBackingWaystone() instanceof Waystone) {
                    ((WaystoneBlockEntityBase)blockEntity).initializeFromExisting((ServerLevelAccessor)world, (Waystone)existingWaystone.getBackingWaystone(), stack);
                } else {
                    ((WaystoneBlockEntityBase)blockEntity).initializeWaystone((ServerLevelAccessor)world, placer, WaystoneOrigin.PLAYER);
                }
                if (isDoubleBlock && (waystoneEntityAbove = world.m_7702_(posAbove)) instanceof WaystoneBlockEntityBase) {
                    ((WaystoneBlockEntityBase)waystoneEntityAbove).initializeFromBase((WaystoneBlockEntityBase)blockEntity);
                }
            }
            if (placer instanceof Player) {
                IWaystone waystone = ((WaystoneBlockEntityBase)blockEntity).getWaystone();
                PlayerWaystoneManager.activateWaystone((Player)placer, waystone);
                if (!world.f_46443_) {
                    WaystoneSyncManager.sendActivatedWaystones((Player)placer);
                }
            }
            if (!world.f_46443_ && placer instanceof ServerPlayer && (result = PlayerWaystoneManager.mayEditWaystone((Player)(player = (ServerPlayer)placer), world, (waystoneTileEntity = (WaystoneBlockEntityBase)blockEntity).getWaystone())) == WaystoneEditPermissions.ALLOW && (settingsContainerProvider = waystoneTileEntity.getSettingsMenuProvider()) != null) {
                Balm.getNetworking().openGui((Player)player, settingsContainerProvider);
            }
        }
    }

    public RenderShape m_7514_(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public BlockState m_6843_(BlockState state, Rotation rotation) {
        return (BlockState)state.m_61124_((Property)FACING, (Comparable)rotation.m_55954_((Direction)state.m_61143_((Property)FACING)));
    }

    public BlockState m_6943_(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.m_54846_((Direction)state.m_61143_((Property)FACING)));
    }
}

