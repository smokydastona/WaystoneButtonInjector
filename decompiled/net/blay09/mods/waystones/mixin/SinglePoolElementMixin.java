/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.StructureManager
 *  net.minecraft.world.level.WorldGenLevel
 *  net.minecraft.world.level.block.Rotation
 *  net.minecraft.world.level.chunk.ChunkGenerator
 *  net.minecraft.world.level.levelgen.structure.BoundingBox
 *  net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
 *  net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.blay09.mods.waystones.mixin;

import com.mojang.datafixers.util.Either;
import java.util.HashSet;
import java.util.Set;
import net.blay09.mods.waystones.worldgen.WaystoneStructurePoolElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SinglePoolElement.class})
public abstract class SinglePoolElementMixin
implements WaystoneStructurePoolElement {
    @Unique
    private static final Set<BlockPos> waystones$generatedWaystones = new HashSet<BlockPos>();
    @Unique
    private Boolean waystones$isWaystone;

    @Accessor
    public abstract Either<ResourceLocation, StructureTemplate> getTemplate();

    @Override
    public boolean waystones$isWaystone() {
        if (this.waystones$isWaystone == null) {
            Either<ResourceLocation, StructureTemplate> template = this.getTemplate();
            template.ifLeft(resourceLocation -> {
                this.waystones$isWaystone = resourceLocation.m_135815_().startsWith("village/") && resourceLocation.m_135815_().endsWith("/waystone");
            });
            template.ifRight(structureTemplate -> {
                this.waystones$isWaystone = false;
            });
        }
        return this.waystones$isWaystone;
    }

    @Override
    public void waystones$setIsWaystone(boolean isWaystone) {
        this.waystones$isWaystone = isWaystone;
    }

    @Inject(method={"place(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/util/RandomSource;Z)Z"}, at={@At(value="HEAD")}, cancellable=true)
    public void place(StructureTemplateManager structureTemplateManager, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos blockPos2, Rotation rotation, BoundingBox boundingBox, RandomSource randomSource, boolean bl, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.waystones$isWaystone()) {
            for (BlockPos existingPos : waystones$generatedWaystones) {
                if (pos == existingPos || !(existingPos.m_123331_((Vec3i)pos) < 10000.0)) continue;
                callbackInfo.setReturnValue((Object)false);
                return;
            }
            waystones$generatedWaystones.add(pos);
        }
    }
}

