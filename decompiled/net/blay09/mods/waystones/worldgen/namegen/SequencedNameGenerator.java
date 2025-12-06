/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.blay09.mods.waystones.worldgen.namegen;

import net.blay09.mods.waystones.worldgen.namegen.INameGenerator;
import net.minecraft.util.RandomSource;

public class SequencedNameGenerator
implements INameGenerator {
    private final INameGenerator[] nameGenerators;

    public SequencedNameGenerator(INameGenerator ... nameGenerators) {
        this.nameGenerators = nameGenerators;
    }

    @Override
    public String randomName(RandomSource rand) {
        for (INameGenerator nameGenerator : this.nameGenerators) {
            String name = nameGenerator.randomName(rand);
            if (name == null) continue;
            return name;
        }
        return null;
    }
}

