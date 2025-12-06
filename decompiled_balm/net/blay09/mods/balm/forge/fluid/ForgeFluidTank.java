/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fluids.FluidStack
 *  net.minecraftforge.fluids.capability.IFluidHandler
 *  net.minecraftforge.fluids.capability.IFluidHandler$FluidAction
 *  org.jetbrains.annotations.NotNull
 */
package net.blay09.mods.balm.forge.fluid;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class ForgeFluidTank
implements IFluidHandler {
    private final FluidTank fluidTank;

    public ForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    public int getTanks() {
        return 1;
    }

    @NotNull
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(this.fluidTank.getFluid(), this.fluidTank.getAmount());
    }

    public int getTankCapacity(int tank) {
        return this.fluidTank.getCapacity();
    }

    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.fluidTank.canFill(stack.getFluid());
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.fluidTank.fill(resource.getFluid(), resource.getAmount(), action.simulate());
    }

    @NotNull
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        int drained = this.fluidTank.drain(resource.getFluid(), resource.getAmount(), action.simulate());
        return new FluidStack(this.fluidTank.getFluid(), drained);
    }

    @NotNull
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        int drained = this.fluidTank.drain(this.fluidTank.getFluid(), maxDrain, action.simulate());
        return new FluidStack(this.fluidTank.getFluid(), drained);
    }
}

