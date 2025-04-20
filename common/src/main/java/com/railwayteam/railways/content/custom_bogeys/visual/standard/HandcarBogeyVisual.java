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

package com.railwayteam.railways.content.custom_bogeys.visual.standard;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.railwayteam.railways.content.handcar.ik.DoubleArmIK;
import com.simibubi.create.content.trains.bogey.BogeyVisual;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.railwayteam.railways.registry.CRBlockPartials.*;


import static com.railwayteam.railways.registry.CRBlockPartials.*;

public class HandcarBogeyVisual implements BogeyVisual {
    private final TransformedInstance frame;
    private final TransformedInstance handle;
    private final TransformedInstance handleFirstPerson;
    private final TransformedInstance coupling;
    private final TransformedInstance largeCog;
    private final TransformedInstance smallCog;
    private final TransformedInstance wheel1;
    private final TransformedInstance wheel2;

    private CarriageBogey carriageBogey;

    public HandcarBogeyVisual(VisualizationContext ctx, float partialTick, boolean inContraption) {
        // 创建框架实例
        frame = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_FRAME))
                .createInstance();

        // 创建手柄实例
        handle = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_HANDLE))
                .createInstance();

        // 创建第一人称手柄实例
        handleFirstPerson = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_HANDLE_FIRST_PERSON))
                .createInstance();

        // 创建连接器实例
        coupling = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_COUPLING))
                .createInstance();

        // 创建齿轮实例
        largeCog = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_LARGE_COG))
                .createInstance();

        smallCog = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_SMALL_COG))
                .createInstance();

        // 创建车轮实例
        var wheelInstancer = ctx.instancerProvider()
                .instancer(InstanceTypes.TRANSFORMED, Models.partial(HANDCAR_WHEELS));
        wheel1 = wheelInstancer.createInstance();
        wheel2 = wheelInstancer.createInstance();
    }

    private boolean isFirstPerson() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (!mc.options.getCameraType().isFirstPerson()) {
            return false;
        }
        if (player != null && player.getRootVehicle() instanceof CarriageContraptionEntity cce) {
            if (carriageBogey == null)
                return true;
            return cce.trainId.equals(carriageBogey.carriage.train.id)
                    && cce.carriageIndex == carriageBogey.carriage.train.carriages.indexOf(carriageBogey.carriage);
        }
        return false;
    }

    @Override
    public void update(CompoundTag bogeyData, float wheelAngle, PoseStack poseStack) {
        // 翻倍轮子角度
        wheelAngle *= 2;

        // 更新框架
        frame.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 5 / 16f, 0)
                .setChanged();

        // 计算连接位置和角度
        Vec3 coupling_pos;
        {
            final double couple_r = (3 / 16.) * Mth.SQRT_OF_TWO;
            final double couple_degrees = (-wheelAngle / 2) - 22.5;
            float couple_radians = (float) (couple_degrees * Mth.DEG_TO_RAD);
            double couple_x = couple_r * Mth.sin(couple_radians);
            double couple_y = couple_r * Mth.cos(couple_radians);
            coupling_pos = new Vec3(1.75 / 16., (12 / 16.) + couple_y, (-3.5 / 16.) + couple_x);
        }

        Vec2 upperVec2 = new Vec2(0, 39 / 16f);
        Vec2 couplingVec2 = new Vec2((float)coupling_pos.z, (float)coupling_pos.y);

        //                                                                             upper         lower
        Vec2 hingeOffset = DoubleArmIK.calculateJointOffset(upperVec2, couplingVec2, 14 / 16., 18 / 16.);
        Vec2 hingePos2 = hingeOffset.add(couplingVec2);

        double couplingAngle;
        double handleAngle;

        {
            couplingAngle = Mth.atan2((hingeOffset.y), (hingeOffset.x));

            Vec2 handle_offset = hingePos2.add(upperVec2.negated());
            handleAngle = Mth.atan2(handle_offset.y, handle_offset.x);
        }

        boolean firstPerson = isFirstPerson();

        // 更新手柄
        handle.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translateY((float) (39 / 16.))
                .rotateZDegrees(180)
                .rotateX((float)(handleAngle - Math.toRadians(90-32.5)))
                .translateY((float) (-34 / 16.))
                .scale(firstPerson ? 0 : 1)
                .setChanged();

        // 更新第一人称手柄
        handleFirstPerson.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translateY((float) (39 / 16.))
                .rotateZDegrees(180)
                .rotateX((float)(handleAngle - Math.toRadians(90-32.5)))
                .translateY((float) (-34 / 16.))
                .scale(firstPerson ? 1 : 0)
                .setChanged();

        // 更新连接器
        coupling.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(coupling_pos)
                .rotateX((float)(-(couplingAngle - Mth.HALF_PI)))
                .setChanged();

        // 更新大齿轮
        largeCog.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(-8 / 16f, 12 / 16f, -3.5 / 16f)
                .rotateXDegrees((float) ((-wheelAngle / 2) + 22.5))
                .rotateZDegrees(90)
                .translate(0, -7 / 16f, 0)
                .setChanged();

        // 更新小齿轮
        smallCog.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(-8 / 16f, 12 / 16f, -1)
                .rotateXDegrees(wheelAngle)
                .rotateZDegrees(90)
                .translate(0, -7 / 16f, 0)
                .setChanged();

        // 更新车轮
        wheel1.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 12 / 16f, 1)
                .rotateXDegrees(wheelAngle)
                .translate(0, -12 / 16f, 0)
                .setChanged();

        wheel2.setTransform(poseStack)
                .scale(1 - 1 / 512f)
                .translate(0, 12 / 16f, -1)
                .rotateXDegrees(wheelAngle)
                .translate(0, -12 / 16f, 0)
                .setChanged();
    }

    @Override
    public void hide() {
        frame.setZeroTransform().setChanged();
        handle.setZeroTransform().setChanged();
        handleFirstPerson.setZeroTransform().setChanged();
        coupling.setZeroTransform().setChanged();
        largeCog.setZeroTransform().setChanged();
        smallCog.setZeroTransform().setChanged();
        wheel1.setZeroTransform().setChanged();
        wheel2.setZeroTransform().setChanged();
    }

    @Override
    public void updateLight(int packedLight) {
        frame.light(packedLight).setChanged();
        handle.light(packedLight).setChanged();
        handleFirstPerson.light(packedLight).setChanged();
        coupling.light(packedLight).setChanged();
        largeCog.light(packedLight).setChanged();
        smallCog.light(packedLight).setChanged();
        wheel1.light(packedLight).setChanged();
        wheel2.light(packedLight).setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(frame);
        consumer.accept(handle);
        consumer.accept(handleFirstPerson);
        consumer.accept(coupling);
        consumer.accept(largeCog);
        consumer.accept(smallCog);
        consumer.accept(wheel1);
        consumer.accept(wheel2);
    }

    @Override
    public void delete() {
        frame.delete();
        handle.delete();
        handleFirstPerson.delete();
        coupling.delete();
        largeCog.delete();
        smallCog.delete();
        wheel1.delete();
        wheel2.delete();
    }
}