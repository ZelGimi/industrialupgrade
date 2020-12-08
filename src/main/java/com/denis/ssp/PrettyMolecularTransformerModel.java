package com.Denfop.ssp;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class PrettyMolecularTransformerModel extends ModelBase {
  ModelRenderer coreBottom;
  
  ModelRenderer coreWorkZone;
  
  ModelRenderer coreTopElectr;
  
  ModelRenderer coreTopPlate;
  
  ModelRenderer firstElTop;
  
  ModelRenderer firstElBottom;
  
  ModelRenderer secondElTop;
  
  ModelRenderer secondElBottom;
  
  ModelRenderer thirdElTop;
  
  ModelRenderer thirdElBottom;
  
  public PrettyMolecularTransformerModel() {
    this.textureWidth = 128;
    this.textureHeight = 64;
    this.coreBottom = new ModelRenderer(this, 0, 0);
    this.coreBottom.addBox(-5.0F, 4.0F, -5.0F, 10, 3, 10);
    this.coreBottom.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.coreBottom.setTextureSize(128, 64);
    this.coreBottom.mirror = true;
    setRotation(this.coreBottom, 0.0F, 0.0F, 0.0F);
    this.coreWorkZone = new ModelRenderer(this, 0, 44);
    this.coreWorkZone.addBox(-3.0F, -4.0F, -3.0F, 6, 9, 6);
    this.coreWorkZone.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.coreWorkZone.setTextureSize(128, 64);
    this.coreWorkZone.mirror = true;
    setRotation(this.coreWorkZone, 0.0F, 0.0F, 0.0F);
    this.coreTopElectr = new ModelRenderer(this, 25, 44);
    this.coreTopElectr.addBox(-2.0F, -8.0F, -1.466667F, 3, 2, 3);
    this.coreTopElectr.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.coreTopElectr.setTextureSize(128, 64);
    this.coreTopElectr.mirror = true;
    setRotation(this.coreTopElectr, 0.0F, 0.0F, 0.0F);
    this.coreTopPlate = new ModelRenderer(this, 0, 30);
    this.coreTopPlate.addBox(-5.0F, -7.0F, -4.5F, 9, 3, 9);
    this.coreTopPlate.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.coreTopPlate.setTextureSize(128, 64);
    this.coreTopPlate.mirror = true;
    setRotation(this.coreTopPlate, 0.0F, 0.0F, 0.0F);
    this.firstElTop = new ModelRenderer(this, 20, 16);
    this.firstElTop.addBox(3.0F, -8.0F, -5.0F, 4, 3, 10);
    this.firstElTop.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.firstElTop.setTextureSize(128, 64);
    this.firstElTop.mirror = true;
    setRotation(this.firstElTop, 0.0F, 0.0F, 0.0F);
    this.firstElBottom = new ModelRenderer(this, 49, 16);
    this.firstElBottom.addBox(4.0F, 3.0F, -3.0F, 3, 5, 6);
    this.firstElBottom.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.firstElBottom.setTextureSize(128, 64);
    this.firstElBottom.mirror = true;
    setRotation(this.firstElBottom, 0.0F, 0.0F, 0.0F);
    this.secondElTop = new ModelRenderer(this, 20, 16);
    this.secondElTop.addBox(3.0F, -8.0F, -5.0F, 4, 3, 10);
    this.secondElTop.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.secondElTop.setTextureSize(128, 64);
    this.secondElTop.mirror = true;
    setRotation(this.secondElTop, 0.0F, -2.094395F, 0.0F);
    this.secondElBottom = new ModelRenderer(this, 49, 16);
    this.secondElBottom.addBox(4.0F, 3.0F, -3.0F, 3, 5, 6);
    this.secondElBottom.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.secondElBottom.setTextureSize(128, 64);
    this.secondElBottom.mirror = true;
    setRotation(this.secondElBottom, 0.0F, -2.094395F, 0.0F);
    this.thirdElTop = new ModelRenderer(this, 20, 16);
    this.thirdElTop.addBox(3.0F, -8.0F, -5.0F, 4, 3, 10);
    this.thirdElTop.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.thirdElTop.setTextureSize(128, 64);
    this.thirdElTop.mirror = true;
    setRotation(this.thirdElTop, 0.0F, 2.094395F, 0.0F);
    this.thirdElBottom = new ModelRenderer(this, 49, 16);
    this.thirdElBottom.addBox(4.0F, 3.0F, -3.0F, 3, 5, 6);
    this.thirdElBottom.setRotationPoint(0.0F, 16.0F, 0.0F);
    this.thirdElBottom.setTextureSize(128, 64);
    this.thirdElBottom.mirror = true;
    setRotation(this.thirdElBottom, 0.0F, 2.094395F, 0.0F);
  }
  
  public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
    this.coreBottom.render(scale);
    this.firstElTop.render(scale);
    this.firstElBottom.render(scale);
    this.secondElTop.render(scale);
    this.secondElBottom.render(scale);
    this.thirdElTop.render(scale);
    this.thirdElBottom.render(scale);
    this.coreTopElectr.render(scale);
    this.coreTopPlate.render(scale);
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    this.coreWorkZone.render(scale);
    GlStateManager.popMatrix();
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z) {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
}
