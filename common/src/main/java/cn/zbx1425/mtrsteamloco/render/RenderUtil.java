package cn.zbx1425.mtrsteamloco.render;

import cn.zbx1425.mtrsteamloco.MainClient;
import cn.zbx1425.sowcerext.multipart.MultipartContainer;
import cn.zbx1425.sowcerext.multipart.MultipartUpdateProp;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;

public class RenderUtil {

    public static final int LEVEL_SOWCER = 2;
    public static final int LEVEL_BLAZE = 1;
    public static final int LEVEL_NONE = 0;

    public static int railRenderLevel = LEVEL_SOWCER;

    public static int trainRenderLevel = LEVEL_SOWCER;

    public static void updateAndEnqueueAll(MultipartContainer container, MultipartUpdateProp prop,
                                           Matrix4f basePose, int light, MultiBufferSource vertexConsumers) {
        if (trainRenderLevel == LEVEL_SOWCER) {
            container.updateAndEnqueueAll(prop, MainClient.batchManager, basePose, light);
        } else if (trainRenderLevel == LEVEL_BLAZE) {
            container.updateAndEnqueueAll(prop, vertexConsumers, basePose, light);
        }
    }
}