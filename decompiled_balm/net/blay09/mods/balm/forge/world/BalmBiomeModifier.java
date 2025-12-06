/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.Holder
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraftforge.common.world.BiomeModifier
 *  net.minecraftforge.common.world.BiomeModifier$Phase
 *  net.minecraftforge.common.world.ModifiableBiomeInfo$BiomeInfo$Builder
 */
package net.blay09.mods.balm.forge.world;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class BalmBiomeModifier
implements BiomeModifier {
    public static final BalmBiomeModifier INSTANCE = new BalmBiomeModifier();

    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        ForgeBalmWorldGen worldGen = (ForgeBalmWorldGen)Balm.getWorldGen();
        worldGen.modifyBiome(biome, phase, builder);
    }

    public Codec<? extends BiomeModifier> codec() {
        return ForgeBalmWorldGen.BALM_BIOME_MODIFIER_CODEC;
    }
}

