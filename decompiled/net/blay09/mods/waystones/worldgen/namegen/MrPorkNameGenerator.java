/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.blay09.mods.waystones.worldgen.namegen;

import net.blay09.mods.waystones.worldgen.namegen.INameGenerator;
import net.minecraft.util.RandomSource;

public class MrPorkNameGenerator
implements INameGenerator {
    private static final String[] random1 = new String[]{"Kr", "Ca", "Ra", "Rei", "Mar", "Luk", "Cro", "Cru", "Ray", "Bre", "Zed", "Mor", "Jag", "Mer", "Jar", "Mad", "Cry", "Zur", "Mjol", "Zork", "Creo", "Azak", "Azur", "Mrok", "Drak"};
    private static final String[] random2 = new String[]{"ir", "mi", "air", "sor", "mee", "clo", "red", "cra", "ark", "arc", "mur", "zer", "miri", "lori", "cres", "zoir", "urak", "marac", "slamar", "salmar"};
    private static final String[] random3 = new String[]{"d", "ed", "es", "er", "ark", "arc", "der", "med", "ure", "zur", "mur", "tron", "cred"};

    @Override
    public String randomName(RandomSource rand) {
        return random1[rand.m_188503_(random1.length)] + random2[rand.m_188503_(random2.length)] + random3[rand.m_188503_(random3.length)];
    }
}

