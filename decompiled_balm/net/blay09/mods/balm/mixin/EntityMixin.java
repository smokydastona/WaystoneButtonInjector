/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.world.entity.Entity
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.entity.BalmEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Entity.class})
public class EntityMixin
implements BalmEntity {
    private CompoundTag fabricBalmData = new CompoundTag();

    @Inject(method={"load(Lnet/minecraft/nbt/CompoundTag;)V"}, at={@At(value="HEAD")})
    private void load(CompoundTag compound, CallbackInfo callbackInfo) {
        if (compound.m_128441_("BalmData")) {
            this.fabricBalmData = compound.m_128469_("BalmData");
        }
    }

    @Inject(method={"saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"}, at={@At(value="HEAD")})
    private void saveWithoutId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> callbackInfo) {
        compound.m_128365_("BalmData", (Tag)this.fabricBalmData);
    }

    @Override
    public CompoundTag getFabricBalmData() {
        return this.fabricBalmData;
    }

    @Override
    public void setFabricBalmData(CompoundTag tag) {
        this.fabricBalmData = tag;
    }
}

