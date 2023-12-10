package com.primogemstudio.advancedui.render.filter;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;

import static com.primogemstudio.advancedui.render.Shaders.GAUSSIAN_BLUR;
import static net.minecraft.client.Minecraft.ON_OSX;

public class GaussianBlurFilter implements Filter {
    private static final TextureTarget target = new TextureTarget(1, 1, true, ON_OSX);
    private boolean enable;

    static {
        target.setClearColor(0, 0, 0, 0);
    }

    @Override
    public RenderTarget getTarget() {
        return target;
    }

    @Override
    public void render(float partialTicks) {
        if (enable) {
            GAUSSIAN_BLUR.setSamplerUniform("InputSampler", target);
            GAUSSIAN_BLUR.render(partialTicks);
        }
    }

    @Override
    public void enable() {
        enable = true;
    }

    @Override
    public void reset() {
        enable = false;
    }
}