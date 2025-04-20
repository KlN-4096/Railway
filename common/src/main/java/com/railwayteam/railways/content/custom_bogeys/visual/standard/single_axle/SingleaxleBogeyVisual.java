/*
 * Steam 'n' Rails
 * Copyright (c) 2022-2024 The Railways Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.railwayteam.railways.content.custom_bogeys.visual.standard.single_axle;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.trains.bogey.BogeyVisual;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.railwayteam.railways.registry.CRBlockPartials.*;


import static com.railwayteam.railways.registry.CRBlockPartials.*;

public class SingleaxleBogeyVisual implements BogeyVisual {
    private final TransformedInstance frame;
    private final TransformedInstance wheel;

    public SingleaxleBogeyVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(SINGLEAXLE_FRAME))
                .createInstance();

        // 创建车轮实例
        wheel = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.SMALL_BOGEY_WHEELS))
                .createInstance();
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新车轮
        wheel.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 12 / 16f, 0)
                .rotateXDegrees(wheelAngle)
                .setChanged();
    }

    @Override
    public void hide() {
        frame.setZeroTransform().setChanged();
        wheel.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        frame.light(packedLight).setChanged();
        wheel.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(frame);
        consumer.accept(wheel);
    }

    @Override
    public void delete() {
        frame.delete();
        wheel.delete();
    }
}