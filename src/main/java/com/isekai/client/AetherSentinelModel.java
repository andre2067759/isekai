package com.isekai.client;

import com.isekai.IsekaiMod;
import com.isekai.entity.AetherSentinel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AetherSentinelModel extends HierarchicalModel<AetherSentinel> {
    public static final ModelLayerLocation LAYER_LOCATION =
        new ModelLayerLocation(new ResourceLocation(IsekaiMod.MOD_ID, "aether_sentinel"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public AetherSentinelModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-4F, -8F, -4F, 8F, 8F, 8F)
            .texOffs(32, 0).addBox(-2F, -3F, -5F, 4F, 2F, 1F),
            PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("body", CubeListBuilder.create()
            .texOffs(0, 18).addBox(-5F, 0F, -3F, 10F, 12F, 6F),
            PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
            .texOffs(32, 18).addBox(-1F, -2F, -2F, 4F, 12F, 4F),
            PartPose.offset(6F, 1F, 0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
            .texOffs(32, 18).addBox(-3F, -2F, -2F, 4F, 12F, 4F),
            PartPose.offset(-6F, 1F, 0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
            .texOffs(48, 18).addBox(-2F, 0F, -2F, 4F, 10F, 4F),
            PartPose.offset(3F, 11F, 0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
            .texOffs(48, 18).addBox(-2F, 0F, -2F, 4F, 10F, 4F),
            PartPose.offset(-3F, 11F, 0F));
        return LayerDefinition.create(mesh, 64, 48);
    }

    @Override public ModelPart root() { return root; }

    @Override
    public void setupAnim(AetherSentinel entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        head.xRot = headPitch * ((float)Math.PI / 180F);
        rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
    }
}
