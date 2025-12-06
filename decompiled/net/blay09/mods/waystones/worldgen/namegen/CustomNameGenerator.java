/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.blay09.mods.waystones.worldgen.namegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.worldgen.namegen.INameGenerator;
import net.minecraft.util.RandomSource;

public class CustomNameGenerator
implements INameGenerator {
    private final boolean allowDuplicates;
    private final Set<String> usedNames;

    public CustomNameGenerator(boolean allowDuplicates, Set<String> usedNames) {
        this.allowDuplicates = allowDuplicates;
        this.usedNames = usedNames;
    }

    @Override
    public String randomName(RandomSource rand) {
        ArrayList<String> customNames = new ArrayList<String>(WaystonesConfig.getActive().worldGen.customWaystoneNames);
        Collections.shuffle(customNames);
        for (String customName : customNames) {
            if (!this.allowDuplicates && this.usedNames.contains(customName)) continue;
            return customName;
        }
        return null;
    }
}

