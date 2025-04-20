/*
 * Steam 'n' Rails
 * Copyright (c) 2025 The Railways Team
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

package com.railwayteam.railways.content.custom_bogeys.visual.standard.large;

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

public class LargeCreateStyled080Visual implements BogeyVisual {
    private final TransformedInstance[] shafts; // 横向轴
    private final TransformedInstance[] middleShafts; // 纵向轴
    private final TransformedInstance frame;
    private final TransformedInstance piston;
    private final TransformedInstance[] wheels;
    private final TransformedInstance[] innerWheels;
    private final TransformedInstance[] pins;

    public LargeCreateStyled080Visual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建横向轴实例
        var shaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.X)));
        shafts = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            shafts[i] = shaftInstancer.createInstance();
        }

        // 创建纵向轴实例
        var middleShaftInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z)));
        middleShafts = new TransformedInstance[4]; // -2, -1, 1, 2
        for (int i = 0; i < 4; i++) {
            middleShafts[i] = middleShaftInstancer.createInstance();
        }

        // 创建框架和活塞实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_8_0_FRAME))
                .createInstance();
        piston = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LARGE_CREATE_STYLED_0_8_0_PISTON))
                .createInstance();

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.LARGE_BOGEY_WHEELS));
        wheels = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            wheels[i] = wheelInstancer.createInstance();
        }

        // 创建内轮实例
        var innerWheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(LC_STYLE_SEMI_BLIND_WHEELS));
        innerWheels = new TransformedInstance[2];
        for (int i = 0; i < 2; i++) {
            innerWheels[i] = innerWheelInstancer.createInstance();
        }

        // 创建销钉实例
        var pinInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(AllPartialModels.BOGEY_PIN));
        pins = new TransformedInstance[4]; // 0, 1, 2, 3
        for (int i = 0; i < 4; i++) {
            pins[i] = pinInstancer.createInstance();
        }
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 更新横向轴
        for (int i = 0; i < 2; i++) {
            int side = i == 0 ? 1 : -1;
            shafts[i].setTransform(poseStack)
                    .translate(-.5f, .25f, -.5f + side * 3.617)
                    .center()
                    .rotateXDegrees(wheelAngle)
                    .uncenter()
                    .setChanged();
        }

        // 更新纵向轴
        int[] sides = {-2, -1, 1, 2};
        for (int i = 0; i < 4; i++) {
            middleShafts[i].setTransform(poseStack)
                    .translate(-.5f, .25f, -.5f + sides[i] * -1.6)
                    .center()
                    .rotateZDegrees(wheelAngle)
                    .uncenter()
                    .setChanged();
        }

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .setChanged();

        // 更新活塞
        piston.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .setChanged();

        // 更新车轮和内轮
        for (int i = 0; i < 2; i++) {
            int side = i == 0 ? 1 : -1;
            wheels[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, side * 2.62)
                    .rotateXDegrees(wheelAngle)
                    .setChanged();

            innerWheels[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, side * .8732)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, -1, 0)
                    .setChanged();
        }

        // 更新销钉
        for (int i = 0; i < 4; i++) {
            pins[i].setTransform(poseStack)
                    .scale(1 - 1 / 512f)
                    .translate(0, 1, -2.62f + i * 1.7467)
                    .rotateXDegrees(wheelAngle)
                    .translate(0, 1 / 4f, 0)
                    .rotateXDegrees(-wheelAngle)
                    .setChanged();
        }
    }

    @Override
    public void hide() {
        for (TransformedInstance shaft : shafts) {
            shaft.setZeroTransform().setChanged();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.setZeroTransform().setChanged();
        }
        frame.setZeroTransform().setChanged();
        piston.setZeroTransform().setChanged();
        for (TransformedInstance wheel : wheels) {
            wheel.setZeroTransform().setChanged();
        }
        for (TransformedInstance innerWheel : innerWheels) {
            innerWheel.setZeroTransform().setChanged();
        }
        for (TransformedInstance pin : pins) {
            pin.setZeroTransform().setChanged();
        }
    }

    @Override
    public void updateLight(int packedLight) {
        for (TransformedInstance shaft : shafts) {
            shaft.light(packedLight).setChanged();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.light(packedLight).setChanged();
        }
        frame.light(packedLight).setChanged();
        piston.light(packedLight).setChanged();
        for (TransformedInstance wheel : wheels) {
            wheel.light(packedLight).setChanged();
        }
        for (TransformedInstance innerWheel : innerWheels) {
            innerWheel.light(packedLight).setChanged();
        }
        for (TransformedInstance pin : pins) {
            pin.light(packedLight).setChanged();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (TransformedInstance shaft : shafts) {
            consumer.accept(shaft);
        }
        for (TransformedInstance middleShaft : middleShafts) {
            consumer.accept(middleShaft);
        }
        consumer.accept(frame);
        consumer.accept(piston);
        for (TransformedInstance wheel : wheels) {
            consumer.accept(wheel);
        }
        for (TransformedInstance innerWheel : innerWheels) {
            consumer.accept(innerWheel);
        }
        for (TransformedInstance pin : pins) {
            consumer.accept(pin);
        }
    }

    @Override
    public void delete() {
        for (TransformedInstance shaft : shafts) {
            shaft.delete();
        }
        for (TransformedInstance middleShaft : middleShafts) {
            middleShaft.delete();
        }
        frame.delete();
        piston.delete();
        for (TransformedInstance wheel : wheels) {
            wheel.delete();
        }
        for (TransformedInstance innerWheel : innerWheels) {
            innerWheel.delete();
        }
        for (TransformedInstance pin : pins) {
            pin.delete();
        }
    }
}