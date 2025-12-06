/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 */
package net.blay09.mods.balm.api.fluid;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraft.core.Direction;

public interface BalmFluidTankProvider {
    public FluidTank getFluidTank();

    default public FluidTank getFluidTank(Direction side) {
        return this.getFluidTank();
    }
}

