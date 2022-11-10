package cn.zbx1425.mtrsteamloco.mixin;

import cn.zbx1425.mtrsteamloco.ClientConfig;
import cn.zbx1425.mtrsteamloco.MainClient;
import cn.zbx1425.mtrsteamloco.render.RenderUtil;
import cn.zbx1425.sowcer.util.GLStateCapture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import mtr.data.Rail;
import mtr.data.RailType;
import mtr.data.TransportMode;
import mtr.entity.EntitySeat;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderTrains.class)
public class RenderTrainsMixin {

    private static final GLStateCapture glState = new GLStateCapture();

    @Inject(at = @At("TAIL"),
            method = "render(Lmtr/entity/EntitySeat;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V")
    private static void render(EntitySeat entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        if (ClientConfig.getRailRenderLevel() < RenderUtil.LEVEL_SOWCER && ClientConfig.getTrainRenderLevel() < RenderUtil.LEVEL_SOWCER) return;
        if (MainClient.shaderManager.isReady()) {
            glState.capture();
            Matrix4f viewMatrix = matrices.last().pose();
            if (ClientConfig.getRailRenderLevel() == RenderUtil.LEVEL_SOWCER) {
                MainClient.railRenderDispatcher.updateAndEnqueueAll(Minecraft.getInstance().level, MainClient.batchManager, viewMatrix);
            }
            MainClient.batchManager.drawAll(MainClient.shaderManager);
            glState.restore();
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true,
            method = "renderRailStandard(Lnet/minecraft/world/level/Level;Lmtr/data/Rail;FZFLjava/lang/String;FFFF)V")
    private static void renderRailStandard(Level world, Rail rail, float yOffset, boolean renderColors, float railWidth, String texture, float u1, float v1, float u2, float v2, CallbackInfo ci) {
        if (ClientConfig.getRailRenderLevel() == RenderUtil.LEVEL_NONE) {
            ci.cancel();
            return;
        }
        if (ClientConfig.getRailRenderLevel() == RenderUtil.LEVEL_SOWCER) {
            if (rail.transportMode == TransportMode.TRAIN && rail.railType != RailType.NONE) {
                MainClient.railRenderDispatcher.registerRail(rail);
                ci.cancel();
            }
        }
    }

}
