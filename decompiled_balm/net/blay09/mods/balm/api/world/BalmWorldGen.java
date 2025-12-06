/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.village.poi.PoiType
 *  net.minecraft.world.level.levelgen.GenerationStep$Decoration
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.placement.PlacementModifierType
 */
package net.blay09.mods.balm.api.world;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public interface BalmWorldGen {
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation var1, Supplier<T> var2);

    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation var1, Supplier<T> var2);

    public <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation var1, Supplier<T> var2);

    public void addFeatureToBiomes(BiomePredicate var1, GenerationStep.Decoration var2, ResourceLocation var3);
}

