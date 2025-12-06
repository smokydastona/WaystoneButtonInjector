/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.DyeColor
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
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.SharestoneBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SharestoneBlock
extends WaystoneBlockBase {
    private static final VoxelShape LOWER_SHAPE = Shapes.m_83124_((VoxelShape)SharestoneBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)3.0, (double)16.0), (VoxelShape[])new VoxelShape[]{SharestoneBlock.m_49796_((double)1.0, (double)3.0, (double)1.0, (double)15.0, (double)7.0, (double)15.0), SharestoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), SharestoneBlock.m_49796_((double)3.0, (double)9.0, (double)3.0, (double)13.0, (double)16.0, (double)13.0)}).m_83296_();
    private static final VoxelShape UPPER_SHAPE = Shapes.m_83124_((VoxelShape)SharestoneBlock.m_49796_((double)3.0, (double)0.0, (double)3.0, (double)13.0, (double)7.0, (double)13.0), (VoxelShape[])new VoxelShape[]{SharestoneBlock.m_49796_((double)2.0, (double)7.0, (double)2.0, (double)14.0, (double)9.0, (double)14.0), SharestoneBlock.m_49796_((double)1.0, (double)9.0, (double)1.0, (double)15.0, (double)13.0, (double)15.0), SharestoneBlock.m_49796_((double)0.0, (double)13.0, (double)0.0, (double)16.0, (double)16.0, (double)16.0)}).m_83296_();
    @Nullable
    private final DyeColor color;

    public SharestoneBlock(BlockBehaviour.Properties properties, @Nullable DyeColor color) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false)));
    }

    @Nullable
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new SharestoneBlockEntity(pos, state);
    }

    public VoxelShape m_5940_(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.m_61143_((Property)HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    protected InteractionResult handleActivation(Level world, BlockPos pos, Player player, WaystoneBlockEntityBase tileEntity, IWaystone waystone) {
        if (!world.f_46443_) {
            Balm.getNetworking().openGui(player, tileEntity.getMenuProvider());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void m_5871_(ItemStack stack, @Nullable BlockGetter world, List<Component> list, TooltipFlag flag) {
        MutableComponent component = Component.m_237115_((String)(this.color != null ? "tooltip.waystones." + this.color.m_7912_() + "_sharestone" : "tooltip.waystones.sharestone"));
        component.m_130940_(ChatFormatting.GRAY);
        list.add((Component)component);
        super.m_5871_(stack, world, list, flag);
    }

    @Override
    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.m_61104_(new Property[]{HALF});
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }
}

