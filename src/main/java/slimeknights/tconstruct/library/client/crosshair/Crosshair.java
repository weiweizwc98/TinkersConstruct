package slimeknights.tconstruct.library.client.crosshair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import slimeknights.tconstruct.library.Util;

public class Crosshair implements ICrosshair {

  private final ResourceLocation texture;
  private final int size;

  public Crosshair(ResourceLocation texture) {
    this(texture, 16);
  }

  public Crosshair(ResourceLocation texture, int size) {
    this.texture = texture;
    this.size = size;
  }

  @Override
  public void render(float charge, float width, float height, float partialTicks) {
    Minecraft mc = Minecraft.getMinecraft();

    mc.getTextureManager().bindTexture(texture);
    GlStateManager.enableBlend();
    OpenGlHelper.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);

    float spread = (1.0f - charge) * 25f;

    drawCrosshair(spread, width, height, partialTicks);

    GlStateManager.disableBlend();
    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
  }

  protected void drawCrosshair(float spread, float width, float height, float partialTicks) {
    drawSquareCrosshairPart(width / 2f - spread, height / 2f - spread, 0);
    drawSquareCrosshairPart(width / 2f + spread, height / 2f - spread, 1);
    drawSquareCrosshairPart(width / 2f - spread, height / 2f + spread, 2);
    drawSquareCrosshairPart(width / 2f + spread, height / 2f + spread, 3);
  }

  protected void drawSquareCrosshairPart(double x, double y, int part) {
    int s = size/4;

    double z = -90;

    double u1 = 0;
    double v1 = 0;

    switch(part)
    {
      // top left
      case 0:
        x -= s;
        y -= s;
        break;
      case 1:
        u1 = 0.5;
        x += s;
        y -= s;
        break;
      case 2:
        v1 = 0.5;
        x -= s;
        y += s;
        break;
      case 3:
        u1 = 0.5;
        v1 = 0.5;
        x += s;
        y += s;
        break;
    }

    double u2 = u1 + 0.5;
    double v2 = v1 + 0.5;

    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vb = tessellator.getBuffer();
    vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    vb.pos(x - s, y - s, z).tex(u1, v1).endVertex();
    vb.pos(x - s, y + s, z).tex(u1, v2).endVertex();
    vb.pos(x + s, y + s, z).tex(u2, v2).endVertex();
    vb.pos(x + s, y - s, z).tex(u2, v1).endVertex();
    tessellator.draw();
  }
}