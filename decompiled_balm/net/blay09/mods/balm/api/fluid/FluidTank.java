/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 */
package net.blay09.mods.balm.api.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTank {
    private final int capacity;
    private final int maxFill;
    private final int maxDrain;
    private Fluid fluid = Fluids.f_76191_;
    private int amount;

    public FluidTank(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public FluidTank(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public FluidTank(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public FluidTank(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(Fluid fluid, int maxFill, boolean simulate) {
        if (!this.canFill(fluid)) {
            return 0;
        }
        if (this.fluid.m_6212_(Fluids.f_76191_)) {
            this.fluid = fluid;
        }
        int filled = Math.min(this.capacity - this.amount, Math.min(this.maxFill, maxFill));
        if (!simulate) {
            this.amount += filled;
            this.setChanged();
        }
        return filled;
    }

    public int drain(Fluid fluid, int maxDrain, boolean simulate) {
        if (!this.canDrain(fluid)) {
            return 0;
        }
        int drained = Math.min(this.amount, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            this.amount -= drained;
            this.setChanged();
        }
        return drained;
    }

    public Fluid getFluid() {
        return this.amount >= 0 ? this.fluid : Fluids.f_76191_;
    }

    public void setFluid(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
        this.setChanged();
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean canDrain(Fluid fluid) {
        return (this.fluid.m_6212_(fluid) || this.fluid.m_6212_(Fluids.f_76191_)) && this.maxDrain > 0;
    }

    public boolean canFill(Fluid fluid) {
        return (this.fluid.m_6212_(fluid) || this.fluid.m_6212_(Fluids.f_76191_)) && this.maxFill > 0;
    }

    public boolean isEmpty() {
        return this.amount <= 0 || this.fluid.m_6212_(Fluids.f_76191_);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.m_128359_("Fluid", BuiltInRegistries.f_257020_.m_7981_((Object)this.fluid).toString());
        tag.m_128405_("Amount", this.amount);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.fluid = (Fluid)BuiltInRegistries.f_257020_.m_7745_(ResourceLocation.m_135820_((String)tag.m_128461_("Fluid")));
        this.amount = tag.m_128451_("Amount");
    }

    public void setChanged() {
    }
}

