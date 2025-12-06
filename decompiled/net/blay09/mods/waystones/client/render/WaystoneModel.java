/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.geom.PartPose
 *  net.minecraft.client.model.geom.builders.CubeDeformation
 *  net.minecraft.client.model.geom.builders.CubeListBuilder
 *  net.minecraft.client.model.geom.builders.LayerDefinition
 *  net.minecraft.client.model.geom.builders.MeshDefinition
 *  net.minecraft.client.model.geom.builders.PartDefinition
 *  net.minecraft.client.renderer.RenderType
 */
package net.blay09.mods.waystones.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class WaystoneModel
extends Model {
    private final ModelPart root;

    public WaystoneModel(ModelPart root) {
        super(it -> RenderType.m_110463_());
        this.root = root;
    }

    public static LayerDefinition createLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.m_171576_();
        partDefinition.m_171599_("body", CubeListBuilder.m_171558_().m_171514_(144, 0).m_171488_(-10.0f, -48.0f, -10.0f, 20.0f, 28.0f, 20.0f, cubeDeformation), PartPose.f_171404_);
        return LayerDefinition.m_171565_((MeshDefinition)meshDefinition, (int)256, (int)256);
    }

    public void m_7695_(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root.m_104306_(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

