/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.block.entity.CustomRenderBoundingBox
 *  net.blay09.mods.balm.common.BalmBlockEntity
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 */
package net.blay09.mods.waystones.block.entity;

import net.blay09.mods.balm.api.block.entity.CustomRenderBoundingBox;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PortstoneBlockEntity
extends BalmBlockEntity
implements CustomRenderBoundingBox {
    public PortstoneBlockEntity(BlockPos worldPosition, BlockState state) {
        super((BlockEntityType)ModBlockEntities.portstone.get(), worldPosition, state);
    }

    public AABB getRenderBoundingBox() {
        return new AABB((double)this.f_58858_.m_123341_(), (double)this.f_58858_.m_123342_(), (double)this.f_58858_.m_123343_(), (double)(this.f_58858_.m_123341_() + 1), (double)(this.f_58858_.m_123342_() + 2), (double)(this.f_58858_.m_123343_() + 1));
    }
}

