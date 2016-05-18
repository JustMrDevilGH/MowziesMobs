package com.bobmowzie.mowziesmobs.server.property;

import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class MowziePlayerProperties extends EntityProperties<EntityPlayer> {
    public static final int SWING_HIT_TICK = 10;
    private static final int SWING_LENGTH = 20;
    @NBTProperty
    public int untilSunstrike = 0;
    @NBTProperty
    private int prevTime;
    @NBTProperty
    private int time;

    public static float fnc1(float x) {
        return x * ((45 - 27 * x) * x - 18);
    }

    public static float fnc2(float x) {
        return MathHelper.sin(x * (float) Math.PI);
    }

    public static float fnc3(float x, float incline, float decline, float steepness) {
        return (float) (1 / (1 + Math.exp(-steepness * (x - incline))) - (1 / (1 + Math.exp(-steepness * (x - decline)))));
    }

    @Override
    public void init() {

    }

    @Override
    public String getID() {
        return "mm:player";
    }

    @Override
    public Class<EntityPlayer> getEntityClass() {
        return EntityPlayer.class;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.saveNBTData(this, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
    }

    public void update() {
        prevTime = time;
    }

    public void swing() {
        time = SWING_LENGTH;
    }

    public int getTick() {
        return time;
    }

    public float getSwingPercentage(float partialRenderTicks) {
        return (prevTime + (time - prevTime) * partialRenderTicks) / SWING_LENGTH;
    }

    public void decrementTime() {
        time--;
    }

    @Override
    public int getTrackingTime() {
        return 0;
    }
}
