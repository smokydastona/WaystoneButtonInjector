/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
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
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block;

import java.util.List;
import java.util.UUID;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.PortstoneBlockEntity;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PortstoneBlock
extends WaystoneBlockBase {
    private static final VoxelShape[] LOWER_SHAPES = new VoxelShape[]{Shapes.m_83124_((VoxelShape)PortstoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{PortstoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), PortstoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), PortstoneBlock.m_49796_((double)3.0, (double)9.0, (double)3.0, (double)13.0, (double)16.0, (double)7.0), PortstoneBlock.m_49796_((double)4.0, (double)9.0, (double)7.0, (double)12.0, (double)16.0, (double)10.0), PortstoneBlock.m_49796_((double)4.0, (double)9.0, (double)10.0, (double)12.0, (double)12.0, (double)12.0)}).m_83296_(), Shapes.m_83124_((VoxelShape)PortstoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{PortstoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), PortstoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), PortstoneBlock.m_49796_((double)9.0, (double)9.0, (double)3.0, (double)13.0, (double)16.0, (double)13.0), PortstoneBlock.m_49796_((double)6.0, (double)9.0, (double)4.0, (double)9.0, (double)16.0, (double)12.0), PortstoneBlock.m_49796_((double)4.0, (double)9.0, (double)4.0, (double)6.0, (double)12.0, (double)12.0)}).m_83296_(), Shapes.m_83124_((VoxelShape)PortstoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{PortstoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), PortstoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), PortstoneBlock.m_49796_((double)3.0, (double)9.0, (double)9.0, (double)13.0, (double)16.0, (double)13.0), PortstoneBlock.m_49796_((double)4.0, (double)9.0, (double)6.0, (double)12.0, (double)16.0, (double)9.0), PortstoneBlock.m_49796_((double)4.0, (double)9.0, (double)4.0, (double)12.0, (double)12.0, (double)6.0)}).m_83296_(), Shapes.m_83124_((VoxelShape)PortstoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{PortstoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), PortstoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), PortstoneBlock.m_49796_((double)3.0, (double)9.0, (double)3.0, (double)7.0, (double)16.0, (double)13.0), PortstoneBlock.m_49796_((double)7.0, (double)9.0, (double)4.0, (double)10.0, (double)16.0, (double)12.0), PortstoneBlock.m_49796_((double)10.0, (double)9.0, (double)4.0, (double)12.0, (double)12.0, (double)12.0)}).m_83296_()};
    private static final VoxelShape[] UPPER_SHAPES = new VoxelShape[]{Shapes.m_83110_((VoxelShape)PortstoneBlock.m_49796_((double)3.0, (double)0.0, (double)3.0, (double)13.0, (double)7.0, (double)7.0), (VoxelShape)PortstoneBlock.m_49796_((double)4.0, (double)0.0, (double)7.0, (double)12.0, (double)2.0, (double)9.0)).m_83296_(), Shapes.m_83110_((VoxelShape)PortstoneBlock.m_49796_((double)9.0, (double)0.0, (double)3.0, (double)13.0, (double)7.0, (double)13.0), (VoxelShape)PortstoneBlock.m_49796_((double)7.0, (double)0.0, (double)4.0, (double)9.0, (double)2.0, (double)12.0)).m_83296_(), Shapes.m_83110_((VoxelShape)PortstoneBlock.m_49796_((double)3.0, (double)0.0, (double)9.0, (double)13.0, (double)7.0, (double)13.0), (VoxelShape)PortstoneBlock.m_49796_((double)4.0, (double)0.0, (double)7.0, (double)12.0, (double)2.0, (double)9.0)).m_83296_(), Shapes.m_83110_((VoxelShape)PortstoneBlock.m_49796_((double)3.0, (double)0.0, (double)3.0, (double)7.0, (double)7.0, (double)13.0), (VoxelShape)PortstoneBlock.m_49796_((double)7.0, (double)0.0, (double)4.0, (double)9.0, (double)2.0, (double)12.0)).m_83296_()};

    public PortstoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    public VoxelShape m_5940_(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = (Direction)state.m_61143_((Property)FACING);
        return state.m_61143_((Property)HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPES[direction.m_122416_()] : LOWER_SHAPES[direction.m_122416_()];
    }

    @Nullable
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new PortstoneBlockEntity(pos, state);
    }

    @Override
    public InteractionResult m_6227_(BlockState state, final Level world, final BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!world.f_46443_) {
            Balm.getNetworking().openGui(player, (MenuProvider)new BalmMenuProvider(){

                public Component m_5446_() {
                    return Component.m_237115_((String)"block.waystones.portstone");
                }

                public AbstractContainerMenu m_7208_(int i, Inventory inventory, Player player) {
                    Waystone portstone = new Waystone(WaystoneTypes.PORTSTONE, UUID.randomUUID(), (ResourceKey<Level>)world.m_46472_(), pos, WaystoneOrigin.UNKNOWN, null);
                    return WaystoneSelectionMenu.createWaystoneSelection(i, player, WarpMode.PORTSTONE_TO_WAYSTONE, portstone);
                }

                public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                    buf.writeByte(WarpMode.PORTSTONE_TO_WAYSTONE.ordinal());
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.m_61104_(new Property[]{HALF});
    }

    public boolean m_7357_(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void m_5871_(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        MutableComponent component = Component.m_237115_((String)"tooltip.waystones.portstone");
        component.m_130940_(ChatFormatting.GRAY);
        list.add((Component)component);
        super.m_5871_(stack, world, list, flag);
    }
}

