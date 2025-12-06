/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.energy.IEnergyStorage
 */
package net.blay09.mods.balm.forge.energy;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyStorage
implements IEnergyStorage {
    private final EnergyStorage energyStorage;

    public ForgeEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.energyStorage.fill(maxReceive, simulate);
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.energyStorage.drain(maxExtract, simulate);
    }

    public int getEnergyStored() {
        return this.energyStorage.getEnergy();
    }

    public int getMaxEnergyStored() {
        return this.energyStorage.getCapacity();
    }

    public boolean canExtract() {
        return this.energyStorage.canDrain();
    }

    public boolean canReceive() {
        return this.energyStorage.canFill();
    }
}

