/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.ai.village.poi.PoiType
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraft.world.level.levelgen.GenerationStep$Decoration
 *  net.minecraft.world.level.levelgen.feature.Feature
 *  net.minecraft.world.level.levelgen.placement.PlacedFeature
 *  net.minecraft.world.level.levelgen.placement.PlacementModifierType
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.world.BiomeModifier$Phase
 *  net.minecraftforge.common.world.ModifiableBiomeInfo$BiomeInfo$Builder
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.ForgeRegistries$Keys
 *  net.minecraftforge.registries.RegistryObject
 *  net.minecraftforge.server.ServerLifecycleHooks
 */
package net.blay09.mods.balm.forge.world;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.blay09.mods.balm.forge.world.BalmBiomeModifier;
import net.blay09.mods.balm.forge.world.BiomeModification;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ForgeBalmWorldGen
implements BalmWorldGen {
    public static final Codec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = Codec.unit((Object)BalmBiomeModifier.INSTANCE);
    private static final List<BiomeModification> biomeModifications = new ArrayList<BiomeModification>();

    public ForgeBalmWorldGen() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public static void initializeBalmBiomeModifiers(IEventBus modEventBus) {
        DeferredRegister registry = DeferredRegister.create((ResourceKey)ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, (String)"balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(modEventBus);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        String namespace = identifier.m_135827_();
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.FEATURES, namespace);
        RegistryObject registryObject = register.register(identifier.m_135815_(), supplier);
        return new DeferredObject(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        String namespace = identifier.m_135827_();
        DeferredRegister register = DeferredRegisters.get(Registries.f_256843_, namespace);
        RegistryObject registryObject = register.register(identifier.m_135815_(), supplier);
        return new DeferredObject(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation identifier, Supplier<T> supplier) {
        String namespace = identifier.m_135827_();
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.POI_TYPES, namespace);
        RegistryObject registryObject = register.register(identifier.m_135815_(), supplier);
        return new DeferredObject(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        ResourceKey resourceKey = ResourceKey.m_135785_((ResourceKey)Registries.f_256988_, (ResourceLocation)placedFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, (ResourceKey<PlacedFeature>)resourceKey));
    }

    public void modifyBiome(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            for (BiomeModification biomeModification : biomeModifications) {
                ResourceLocation location = biome.m_203543_().map(ResourceKey::m_135782_).orElse(null);
                if (location == null || !biomeModification.getBiomePredicate().test(location, biome)) continue;
                Registry placedFeatures = ServerLifecycleHooks.getCurrentServer().m_206579_().m_175515_(Registries.f_256988_);
                placedFeatures.m_203636_(biomeModification.getConfiguredFeatureKey()).ifPresent(placedFeature -> builder.getGenerationSettings().m_255419_(biomeModification.getStep(), (Holder)placedFeature));
            }
        }
    }
}

