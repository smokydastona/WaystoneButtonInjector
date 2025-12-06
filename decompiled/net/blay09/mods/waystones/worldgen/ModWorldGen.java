/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.DeferredObject
 *  net.blay09.mods.balm.api.event.ConfigLoadedEvent
 *  net.blay09.mods.balm.api.event.server.ServerStartingEvent
 *  net.blay09.mods.balm.api.world.BalmWorldGen
 *  net.blay09.mods.balm.api.world.BiomePredicate
 *  net.minecraft.core.Holder
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.ai.village.poi.PoiType
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.level.levelgen.GenerationStep$Decoration
 *  net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
 *  net.minecraft.world.level.levelgen.placement.PlacementModifierType
 *  net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement
 *  net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool$Projection
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList
 */
package net.blay09.mods.waystones.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.event.ConfigLoadedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartingEvent;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.config.WorldGenStyle;
import net.blay09.mods.waystones.mixin.StructureTemplatePoolAccessor;
import net.blay09.mods.waystones.tag.ModBiomeTags;
import net.blay09.mods.waystones.worldgen.WaystoneFeature;
import net.blay09.mods.waystones.worldgen.WaystonePlacement;
import net.blay09.mods.waystones.worldgen.WaystoneStructurePoolElement;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class ModWorldGen {
    private static final ResourceLocation waystone = new ResourceLocation("waystones", "waystone");
    private static final ResourceLocation mossyWaystone = new ResourceLocation("waystones", "mossy_waystone");
    private static final ResourceLocation sandyWaystone = new ResourceLocation("waystones", "sandy_waystone");
    private static final ResourceLocation blackstoneWaystone = new ResourceLocation("waystones", "blackstone_waystone");
    private static final ResourceLocation deepslateWaystone = new ResourceLocation("waystones", "deepslate_waystone");
    private static final ResourceLocation endStoneWaystone = new ResourceLocation("waystones", "end_stone_waystone");
    private static final ResourceLocation villageWaystoneStructure = new ResourceLocation("waystones", "village/common/waystone");
    private static final ResourceLocation desertVillageWaystoneStructure = new ResourceLocation("waystones", "village/desert/waystone");
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.m_135785_((ResourceKey)Registries.f_257011_, (ResourceLocation)new ResourceLocation("minecraft", "empty"));
    public static DeferredObject<PlacementModifierType<WaystonePlacement>> waystonePlacement;

    public static void initialize(BalmWorldGen worldGen) {
        worldGen.registerFeature(waystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.waystone.m_49966_()));
        worldGen.registerFeature(mossyWaystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.mossyWaystone.m_49966_()));
        worldGen.registerFeature(sandyWaystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.sandyWaystone.m_49966_()));
        worldGen.registerFeature(blackstoneWaystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.blackstoneWaystone.m_49966_()));
        worldGen.registerFeature(deepslateWaystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.deepslateWaystone.m_49966_()));
        worldGen.registerFeature(endStoneWaystone, () -> new WaystoneFeature((Codec<NoneFeatureConfiguration>)NoneFeatureConfiguration.f_67815_, ModBlocks.endStoneWaystone.m_49966_()));
        waystonePlacement = worldGen.registerPlacementModifier(ModWorldGen.id("waystone"), () -> () -> WaystonePlacement.CODEC);
        ResourceLocation waystonesCommonConfig = new ResourceLocation("waystones", "common");
        Runnable configLoadHandler = () -> {
            worldGen.addFeatureToBiomes(ModWorldGen.matchesTag(ModBiomeTags.HAS_STRUCTURE_SANDY_WAYSTONE), GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldGen.getWaystoneFeature(WorldGenStyle.SANDY));
            worldGen.addFeatureToBiomes(ModWorldGen.matchesTag(ModBiomeTags.HAS_STRUCTURE_MOSSY_WAYSTONE), GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldGen.getWaystoneFeature(WorldGenStyle.MOSSY));
            worldGen.addFeatureToBiomes(ModWorldGen.matchesTag(ModBiomeTags.HAS_STRUCTURE_BLACKSTONE_WAYSTONE), GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldGen.getWaystoneFeature(WorldGenStyle.BLACKSTONE));
            worldGen.addFeatureToBiomes(ModWorldGen.matchesTag(ModBiomeTags.HAS_STRUCTURE_END_STONE_WAYSTONE), GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldGen.getWaystoneFeature(WorldGenStyle.END_STONE));
            worldGen.addFeatureToBiomes(ModWorldGen.matchesTag(ModBiomeTags.HAS_STRUCTURE_WAYSTONE), GenerationStep.Decoration.VEGETAL_DECORATION, ModWorldGen.getWaystoneFeature(WorldGenStyle.DEFAULT));
        };
        Balm.getEvents().onEvent(ConfigLoadedEvent.class, event -> {
            if (event.getSchema().identifier().equals((Object)waystonesCommonConfig)) {
                configLoadHandler.run();
            }
        });
        if (Balm.getConfig().getActiveConfig(new ResourceLocation("waystones", "common")) != null) {
            configLoadHandler.run();
        }
        worldGen.registerPoiType(ModWorldGen.id("wild_waystone"), () -> new PoiType(ModWorldGen.gatherWaystonesOfOrigin(WaystoneOrigin.WILDERNESS), 1, 1));
        worldGen.registerPoiType(ModWorldGen.id("village_waystone"), () -> new PoiType(ModWorldGen.gatherWaystonesOfOrigin(WaystoneOrigin.VILLAGE), 1, 1));
        Balm.getEvents().onEvent(ServerStartingEvent.class, event -> ModWorldGen.setupDynamicRegistries((RegistryAccess)event.getServer().m_206579_()));
    }

    private static Set<BlockState> gatherWaystonesOfOrigin(WaystoneOrigin origin) {
        List<Block> sourceBlocks = List.of(ModBlocks.waystone, ModBlocks.sandyWaystone, ModBlocks.mossyWaystone, ModBlocks.blackstoneWaystone, ModBlocks.endStoneWaystone);
        return sourceBlocks.stream().flatMap(it -> it.m_49965_().m_61056_().stream()).filter(it -> it.m_61143_((Property)WaystoneBlock.ORIGIN) == origin).collect(Collectors.toSet());
    }

    private static BiomePredicate matchesTag(TagKey<Biome> tag) {
        return (resourceLocation, biome) -> biome.m_203656_(tag);
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("waystones", name);
    }

    private static ResourceLocation getWaystoneFeature(WorldGenStyle biomeWorldGenStyle) {
        WorldGenStyle worldGenStyle = WaystonesConfig.getActive().worldGen.worldGenStyle;
        return switch (worldGenStyle) {
            case WorldGenStyle.MOSSY -> mossyWaystone;
            case WorldGenStyle.SANDY -> sandyWaystone;
            case WorldGenStyle.BLACKSTONE -> blackstoneWaystone;
            case WorldGenStyle.DEEPSLATE -> deepslateWaystone;
            case WorldGenStyle.END_STONE -> endStoneWaystone;
            case WorldGenStyle.BIOME -> {
                switch (biomeWorldGenStyle) {
                    case SANDY: {
                        yield sandyWaystone;
                    }
                    case MOSSY: {
                        yield mossyWaystone;
                    }
                    case BLACKSTONE: {
                        yield blackstoneWaystone;
                    }
                    case DEEPSLATE: {
                        yield deepslateWaystone;
                    }
                    case END_STONE: {
                        yield endStoneWaystone;
                    }
                }
                yield waystone;
            }
            default -> waystone;
        };
    }

    public static void setupDynamicRegistries(RegistryAccess registryAccess) {
        if (WaystonesConfig.getActive().worldGen.spawnInVillages || WaystonesConfig.getActive().worldGen.forceSpawnInVillages) {
            ModWorldGen.addWaystoneStructureToVillageConfig(registryAccess, "village/plains/houses", villageWaystoneStructure, 1);
            ModWorldGen.addWaystoneStructureToVillageConfig(registryAccess, "village/snowy/houses", villageWaystoneStructure, 1);
            ModWorldGen.addWaystoneStructureToVillageConfig(registryAccess, "village/savanna/houses", villageWaystoneStructure, 1);
            ModWorldGen.addWaystoneStructureToVillageConfig(registryAccess, "village/desert/houses", desertVillageWaystoneStructure, 1);
            ModWorldGen.addWaystoneStructureToVillageConfig(registryAccess, "village/taiga/houses", villageWaystoneStructure, 1);
        }
    }

    private static void addWaystoneStructureToVillageConfig(RegistryAccess registryAccess, String villagePiece, ResourceLocation waystoneStructure, int weight) {
        StructureTemplatePool pool;
        Holder.Reference emptyProcessorList = registryAccess.m_175515_(Registries.f_257011_).m_246971_(EMPTY_PROCESSOR_LIST_KEY);
        LegacySinglePoolElement piece = (LegacySinglePoolElement)StructurePoolElement.m_210512_((String)waystoneStructure.toString(), (Holder)emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);
        if (piece instanceof WaystoneStructurePoolElement) {
            WaystoneStructurePoolElement element = (WaystoneStructurePoolElement)piece;
            element.waystones$setIsWaystone(true);
        }
        if ((pool = (StructureTemplatePool)registryAccess.m_175515_(Registries.f_256948_).m_6612_(new ResourceLocation(villagePiece)).orElse(null)) != null) {
            StructureTemplatePoolAccessor poolAccessor = (StructureTemplatePoolAccessor)pool;
            ObjectArrayList listOfPieces = new ObjectArrayList(poolAccessor.getTemplates());
            for (int i = 0; i < weight; ++i) {
                listOfPieces.add((Object)piece);
            }
            poolAccessor.setTemplates((ObjectArrayList<StructurePoolElement>)listOfPieces);
            ArrayList<Pair<StructurePoolElement, Integer>> listOfWeightedPieces = new ArrayList<Pair<StructurePoolElement, Integer>>(poolAccessor.getRawTemplates());
            listOfWeightedPieces.add((Pair<StructurePoolElement, Integer>)new Pair((Object)piece, (Object)weight));
            poolAccessor.setRawTemplates(listOfWeightedPieces);
        }
    }
}

