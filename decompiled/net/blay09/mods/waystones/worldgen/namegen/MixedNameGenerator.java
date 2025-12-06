/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.blay09.mods.waystones.worldgen.namegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.blay09.mods.waystones.worldgen.namegen.INameGenerator;
import net.minecraft.util.RandomSource;

public class MixedNameGenerator
implements INameGenerator {
    private final List<INameGenerator> nameGenerators;

    public MixedNameGenerator(INameGenerator ... nameGenerators) {
        this.nameGenerators = Arrays.asList(nameGenerators);
    }

    @Override
    public String randomName(RandomSource rand) {
        Collections.shuffle(this.nameGenerators);
        for (INameGenerator nameGenerator : this.nameGenerators) {
            String name = nameGenerator.randomName(rand);
            if (name == null) continue;
            return name;
        }
        return null;
    }
}

