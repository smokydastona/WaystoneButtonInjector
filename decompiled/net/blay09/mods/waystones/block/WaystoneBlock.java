/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.DoubleBlockHalf
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.pathfinder.PathComputationType
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block;

import java.util.Objects;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaystoneBlock
extends WaystoneBlockBase {
    private static final VoxelShape LOWER_SHAPE = Shapes.m_83124_((VoxelShape)WaystoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{WaystoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), WaystoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), WaystoneBlock.m_49796_((double)3.0, (double)9.0, (double)3.0, (double)13.0, (double)16.0, (double)13.0)}).m_83296_();
    private static final VoxelShape UPPER_SHAPE = Shapes.m_83124_((VoxelShape)WaystoneBlock.m_49796_((double)3.0, (double)0.0, (double)3.0, (double)13.0, (double)8.0, (double)13.0), (VoxelShape[])new VoxelShape[]{WaystoneBlock.m_49796_((double)2.0, (double)8.0, (double)2.0, (double)14.0, (double)10.0, (double)14.0), WaystoneBlock.m_49796_((double)1.0, (double)10.0, (double)1.0, (double)15.0, (double)12.0, (double)15.0), WaystoneBlock.m_49796_((double)3.0, (double)12.0, (double)3.0, (double)13.0, (double)14.0, (double)13.0), WaystoneBlock.m_49796_((double)4.0, (double)14.0, (double)4.0, (double)12.0, (double)16.0, (double)12.0)}).m_83296_();

    public WaystoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)FACING, (Comparable)Direction.NORTH)).m_61124_((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).m_61124_((Property)ORIGIN, (Comparable)((Object)WaystoneOrigin.UNKNOWN))).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    @Override
    protected boolean canSilkTouch() {
        return true;
    }

    public VoxelShape m_5940_(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return state.m_61143_((Property)HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Nullable
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new WaystoneBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (player.m_21205_().m_204117_(ModItemTags.BOUND_SCROLLS)) {
            return InteractionResult.PASS;
        }
        boolean isActivated = PlayerWaystoneManager.isWaystoneActivated(player, waystone);
        if (isActivated) {
            if (!world.f_46443_) {
                if (WaystonesConfig.getActive().restrictions.allowWaystoneToWaystoneTeleport) {
                    Balm.getNetworking().openGui(player, tileEntity.getMenuProvider());
                } else {
                    player.m_5661_((Component)Component.m_237115_((String)"chat.waystones.waystone_to_waystone_disabled"), true);
                }
            }
        } else {
            PlayerWaystoneManager.activateWaystone(player, waystone);
            if (!world.f_46443_) {
                MutableComponent nameComponent = Component.m_237113_((String)waystone.getName());
                nameComponent.m_130940_(ChatFormatting.WHITE);
                MutableComponent chatComponent = Component.m_237110_((String)"chat.waystones.waystone_activated", (Object[])new Object[]{nameComponent});
                chatComponent.m_130940_(ChatFormatting.YELLOW);
                player.m_213846_((Component)chatComponent);
                WaystoneSyncManager.sendActivatedWaystones(player);
                world.m_5594_(null, pos, SoundEvents.f_12275_, SoundSource.BLOCKS, 0.2f, 1.0f);
            }
            this.notifyObserversOfAction(world, pos);
            if (world.f_46443_) {
                for (int i = 0; i < 32; ++i) {
                    world.m_7106_((ParticleOptions)ParticleTypes.f_123809_, (double)pos.m_123341_() + 0.5 + (world.f_46441_.m_188500_() - 0.5) * 2.0, (double)(pos.m_123342_() + 3), (double)pos.m_123343_() + 0.5 + (world.f_46441_.m_188500_() - 0.5) * 2.0, 0.0, -5.0, 0.0);
                    world.m_7106_((ParticleOptions)ParticleTypes.f_123809_, (double)pos.m_123341_() + 0.5 + (world.f_46441_.m_188500_() - 0.5) * 2.0, (double)(pos.m_123342_() + 4), (double)pos.m_123343_() + 0.5 + (world.f_46441_.m_188500_() - 0.5) * 2.0, 0.0, -5.0, 0.0);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    public void m_214162_(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.m_188501_() < 0.75f) {
            BlockEntity blockEntity = world.m_7702_(pos);
            LocalPlayer player = Minecraft.m_91087_().f_91074_;
            if (blockEntity instanceof WaystoneBlockEntity && PlayerWaystoneManager.isWaystoneActivated((Player)Objects.requireNonNull(player), ((WaystoneBlockEntity)blockEntity).getWaystone())) {
                world.m_7106_((ParticleOptions)ParticleTypes.f_123760_, (double)pos.m_123341_() + 0.5 + (random.m_188500_() - 0.5) * 1.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5 + (random.m_188500_() - 0.5) * 1.5, 0.0, 0.0, 0.0);
                world.m_7106_((ParticleOptions)ParticleTypes.f_123809_, (double)pos.m_123341_() + 0.5 + (random.m_188500_() - 0.5) * 1.5, (double)pos.m_123342_() + 0.5, (double)pos.m_123343_() + 0.5 + (random.m_188500_() - 0.5) * 1.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.m_61104_(new Property[]{HALF});
    }

    public boolean m_7357_(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
}

