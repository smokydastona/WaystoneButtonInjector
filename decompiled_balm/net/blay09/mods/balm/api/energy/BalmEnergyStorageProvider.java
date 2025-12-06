/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Direction
 */
package net.blay09.mods.balm.api.energy;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.minecraft.core.Direction;

public interface BalmEnergyStorageProvider {
    public EnergyStorage getEnergyStorage();

    default public EnergyStorage getEnergyStorage(Direction side) {
        return this.getEnergyStorage();
    }
}

