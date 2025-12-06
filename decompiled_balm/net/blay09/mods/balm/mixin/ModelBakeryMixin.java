/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.resources.model.Material
 *  net.minecraft.client.resources.model.ModelBakery
 *  net.minecraft.resources.ResourceLocation
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.blay09.mods.balm.mixin;

import java.util.function.BiFunction;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ModelBakery.class})
public class ModelBakeryMixin {
    @Inject(method={"bakeModels(Ljava/util/function/BiFunction;)V"}, at={@At(value="RETURN")})
    private void apply(BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction, CallbackInfo callbackInfo) {
        ForgeBalmModels.onBakeModels((ModelBakery)this, spriteBiFunction);
    }
}

