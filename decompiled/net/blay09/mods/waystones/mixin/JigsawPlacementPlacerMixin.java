/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package net.blay09.mods.waystones.mixin;

import net.blay09.mods.waystones.config.WaystonesConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets={"net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement$Placer"})
public class JigsawPlacementPlacerMixin {
    @Final
    @Shadow
    private Registry<StructureTemplatePool> f_210314_;
    private boolean hasWaystone;

    private boolean shouldForceWaystone() {
        return WaystonesConfig.getActive().worldGen.forceSpawnInVillages;
    }

    @ModifyArg(method={"tryPlacingChildren(Lnet/minecraft/world/level/levelgen/structure/PoolElementStructurePiece;Lorg/apache/commons/lang3/mutable/MutableObject;IZLnet/minecraft/world/level/LevelHeightAccessor;Lnet/minecraft/world/level/levelgen/RandomState;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/core/Registry;getHolder(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;"))
    private ResourceKey<StructureTemplatePool> forceWaystonePool(ResourceKey<StructureTemplatePool> resourceKey) {
        ResourceLocation waystonePoolName;
        ResourceKey waystonePoolKey;
        if (!this.shouldForceWaystone() || this.hasWaystone) {
            return resourceKey;
        }
        String poolPath = resourceKey.m_135782_().m_135815_();
        if (poolPath.endsWith("/houses") && this.f_210314_.m_203636_(waystonePoolKey = ResourceKey.m_135785_((ResourceKey)Registries.f_256948_, (ResourceLocation)(waystonePoolName = new ResourceLocation("waystones", poolPath.replace("/houses", "/waystones"))))).isPresent()) {
            this.hasWaystone = true;
            return waystonePoolKey;
        }
        return resourceKey;
    }
}

