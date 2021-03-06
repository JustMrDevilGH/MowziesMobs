package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelBarako;
import com.bobmowzie.mowziesmobs.server.entity.barakoa.EntityBarako;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderBarako extends MobRenderer<EntityBarako, ModelBarako<EntityBarako>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MowziesMobs.MODID, "textures/entity/barako.png");
    private static final double BURST_RADIUS = 3.5;
    private static final int BURST_FRAME_COUNT = 10;
    private static final int BURST_START_FRAME = 12;

    public RenderBarako(EntityRendererManager mgr) {
        super(mgr, new ModelBarako<>(), 1.0F);
    }

    @Override
    protected float getDeathMaxRotation(EntityBarako entity) {
        return 0;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBarako entity) {
        return RenderBarako.TEXTURE;
    }

    @Override
    public void doRender(EntityBarako barako, double x, double y, double z, float yaw, float delta) {
        if (barako.getAnimation() == EntityBarako.ATTACK_ANIMATION && barako.getAnimationTick() > BURST_START_FRAME && barako.getAnimationTick() < BURST_START_FRAME + BURST_FRAME_COUNT - 1) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(x, y + 1.1, z);
            setupGL();
            bindTexture(RenderSunstrike.TEXTURE);
            GlStateManager.rotatef(-renderManager.playerViewY, 0, 1, 0);
            GlStateManager.rotatef(renderManager.playerViewX, 1, 0, 0);
            GlStateManager.disableDepthTest();
            drawBurst(barako.getAnimationTick() - BURST_START_FRAME + delta);
            GlStateManager.enableDepthTest();
            revertGL();
            GlStateManager.popMatrix();
        }
        super.doRender(barako, x, y, z, yaw, delta);
    }

    private void drawBurst(float tick) {
        int dissapateFrame = 6;
        float firstSpeed = 2f;
        float secondSpeed = 1f;
        int frame = ((int) (tick * firstSpeed) <= dissapateFrame) ? (int) (tick * firstSpeed) : (int) (dissapateFrame + (tick - dissapateFrame / firstSpeed) * secondSpeed);
        if (frame > BURST_FRAME_COUNT) {
            frame = BURST_FRAME_COUNT;
        }
        double minU = 0.0625 * frame;
        double maxU = minU + 0.0625;
        double minV = 0.5;
        double maxV = minV + 0.5;
        double offset = 0.219 * (frame % 2);
        Tessellator t = Tessellator.getInstance();
        BufferBuilder buf = t.getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        float opacity = (tick < 8) ? 0.8f : 0.4f;
        buf.pos(-BURST_RADIUS + offset, -BURST_RADIUS + offset, 0).tex(minU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(-BURST_RADIUS + offset, BURST_RADIUS + offset, 0).tex(minU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(BURST_RADIUS + offset, BURST_RADIUS + offset, 0).tex(maxU, maxV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        buf.pos(BURST_RADIUS + offset, -BURST_RADIUS + offset, 0).tex(maxU, minV).lightmap(0, 240).color(1, 1, 1, opacity).endVertex();
        GlStateManager.disableDepthTest();
        t.draw();
        GlStateManager.enableDepthTest();
    }

    private void setupGL() {
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0);
    }

    private void revertGL() {
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
    }
}
