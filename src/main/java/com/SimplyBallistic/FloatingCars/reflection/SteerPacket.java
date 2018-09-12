package com.SimplyBallistic.FloatingCars.reflection;

import net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle;

/**
 * Created by SimplyBallistic on 12/09/2018
 *
 * @author SimplyBallistic
 **/
public class SteerPacket {
    private boolean space, shift;
    private float forward, side;

    public SteerPacket(PacketPlayInSteerVehicle packet) {
        shift = packet.e();
        space = packet.d();
        forward = packet.c();
        side = packet.b();
    }

    public boolean isSpacePressed() {
        return space;
    }

    public boolean isShiftPressed() {
        return shift;
    }

    public float getForwardValue() {
        return forward;
    }

    public float getSideValue() {
        return side;
    }
}
