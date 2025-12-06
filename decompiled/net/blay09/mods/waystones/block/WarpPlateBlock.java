/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.util.StringRepresentable
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityTicker
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.StateDefinition$Builder
 *  net.minecraft.world.level.block.state.properties.BooleanProperty
 *  net.minecraft.world.level.block.state.properties.EnumProperty
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.apache.commons.lang3.StringUtils
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.block;

import java.util.List;
import java.util.Locale;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.waystones.api.IAttunementItem;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class WarpPlateBlock
extends WaystoneBlockBase {
    private static final Style GALACTIC_STYLE = Style.f_131099_.m_131150_(new ResourceLocation("minecraft", "alt"));
    private static final VoxelShape SHAPE = Shapes.m_83110_((VoxelShape)WarpPlateBlock.m_49796_((double)0.0, (double)0.0, (double)0.0, (double)16.0, (double)1.0, (double)16.0), (VoxelShape)WarpPlateBlock.m_49796_((double)3.0, (double)1.0, (double)3.0, (double)13.0, (double)2.0, (double)13.0)).m_83296_();
    @Deprecated
    public static final BooleanProperty ACTIVE = BooleanProperty.m_61465_((String)"active");
    public static final EnumProperty<WarpPlateStatus> STATUS = EnumProperty.m_61587_((String)"status", WarpPlateStatus.class);

    public WarpPlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState)((BlockState)((BlockState)((BlockState)this.f_49792_.m_61090_()).m_61124_((Property)WATERLOGGED, (Comparable)Boolean.valueOf(false))).m_61124_((Property)ACTIVE, (Comparable)Boolean.valueOf(false))).m_61124_(STATUS, (Comparable)((Object)WarpPlateStatus.IDLE)));
    }

    @Override
    protected boolean canSilkTouch() {
        return true;
    }

    @Override
    public void m_5707_(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.m_7702_(pos);
        if (blockEntity instanceof WarpPlateBlockEntity && !player.m_150110_().f_35937_) {
            boolean isSilkTouch = EnchantmentHelper.m_44836_((Enchantment)Enchantments.f_44985_, (LivingEntity)player) > 0;
            WarpPlateBlockEntity warpPlate = (WarpPlateBlockEntity)blockEntity;
            if (warpPlate.isCompletedFirstAttunement()) {
                for (int i = 0; i < warpPlate.m_6643_(); ++i) {
                    IWaystone waystoneAttunedTo;
                    ItemStack itemStack = warpPlate.m_8020_(i);
                    if (!isSilkTouch && itemStack.m_41720_() == ModItems.attunedShard && (waystoneAttunedTo = ((IAttunementItem)ModItems.attunedShard).getWaystoneAttunedTo(world.m_7654_(), itemStack)) != null && waystoneAttunedTo.getWaystoneUid().equals(warpPlate.getWaystone().getWaystoneUid())) continue;
                    WarpPlateBlock.m_49840_((Level)world, (BlockPos)pos, (ItemStack)itemStack);
                }
            }
        }
        super.m_5707_(world, pos, state, player);
    }

    public VoxelShape m_5940_(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected void m_7926_(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.m_61104_(new Property[]{ACTIVE});
        builder.m_61104_(new Property[]{STATUS});
    }

    public void m_7892_(BlockState blockState, Level world, BlockPos pos, Entity entity) {
        BlockEntity tileEntity;
        if (entity.m_20185_() >= (double)pos.m_123341_() && entity.m_20185_() < (double)(pos.m_123341_() + 1) && entity.m_20186_() >= (double)pos.m_123342_() && entity.m_20186_() < (double)(pos.m_123342_() + 1) && entity.m_20189_() >= (double)pos.m_123343_() && entity.m_20189_() < (double)(pos.m_123343_() + 1) && !world.f_46443_ && (tileEntity = world.m_7702_(pos)) instanceof WarpPlateBlockEntity) {
            ((WarpPlateBlockEntity)tileEntity).onEntityCollision(entity);
        }
    }

    public void m_214162_(BlockState state, Level world, BlockPos pos, RandomSource random) {
        block3: {
            block2: {
                if (state.m_61143_(STATUS) != WarpPlateStatus.ACTIVE) break block2;
                for (int i = 0; i < 50; ++i) {
                    world.m_7106_((ParticleOptions)ParticleTypes.f_123784_, (double)pos.m_123341_() + Math.random(), (double)pos.m_123342_() + Math.random() * 2.0, (double)pos.m_123343_() + Math.random(), 0.0, 0.0, 0.0);
                    world.m_7106_((ParticleOptions)ParticleTypes.f_123760_, (double)pos.m_123341_() + Math.random(), (double)pos.m_123342_() + Math.random() * 2.0, (double)pos.m_123343_() + Math.random(), 0.0, 0.0, 0.0);
                }
                break block3;
            }
            if (state.m_61143_(STATUS) != WarpPlateStatus.INVALID) break block3;
            for (int i = 0; i < 10; ++i) {
                world.m_7106_((ParticleOptions)ParticleTypes.f_123762_, (double)pos.m_123341_() + Math.random(), (double)pos.m_123342_(), (double)pos.m_123343_() + Math.random(), 0.0, (double)0.01f, 0.0);
            }
        }
    }

    @Nullable
    public BlockEntity m_142194_(BlockPos pos, BlockState state) {
        return new WarpPlateBlockEntity(pos, state);
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
    protected void addWaystoneNameToTooltip(List<Component> tooltip, WaystoneProxy waystone) {
        tooltip.add(WarpPlateBlock.getGalacticName(waystone));
    }

    public static ChatFormatting getColorForName(String name) {
        int colorIndex = Math.abs(name.hashCode()) % 15;
        ChatFormatting textFormatting = ChatFormatting.m_126647_((int)colorIndex);
        if (textFormatting == ChatFormatting.GRAY) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (textFormatting == ChatFormatting.DARK_GRAY) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (textFormatting == ChatFormatting.BLACK) {
            return ChatFormatting.GOLD;
        }
        return textFormatting != null ? textFormatting : ChatFormatting.GRAY;
    }

    public static Component getGalacticName(IWaystone waystone) {
        String name = StringUtils.substringBeforeLast((String)waystone.getName(), (String)" ");
        MutableComponent galacticName = Component.m_237113_((String)name);
        galacticName.m_130940_(WarpPlateBlock.getColorForName(name));
        galacticName.m_130948_(GALACTIC_STYLE);
        return galacticName;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> m_142354_(Level world, BlockState state, BlockEntityType<T> type) {
        return world.f_46443_ ? null : WarpPlateBlock.m_152132_(type, (BlockEntityType)((BlockEntityType)ModBlockEntities.warpPlate.get()), (level, pos, state2, blockEntity) -> blockEntity.serverTick());
    }

    public static enum WarpPlateStatus implements StringRepresentable
    {
        IDLE,
        ACTIVE,
        INVALID;


        public String m_7912_() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}

