package net.xiaoyu.target_lock.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LockTargetData {
    private Entity lockedTarget = null;
    private boolean isTargetLocked = false;
    private Vec3 lockedTargetPosition = null;

    public Entity getLockedTarget() {
        return lockedTarget;
    }

    public void setLockedTarget(Entity lockedTarget) {
        this.lockedTarget = lockedTarget;
    }

    public boolean isTargetLocked() {
        return isTargetLocked;
    }

    public void setTargetLocked(boolean targetLocked) {
        isTargetLocked = targetLocked;
    }

    public Vec3 getLockedTargetPosition() {
        return lockedTargetPosition;
    }

    public void setLockedTargetPosition(Vec3 lockedTargetPosition) {
        this.lockedTargetPosition = lockedTargetPosition;
    }

    public void reset() {
        this.lockedTarget = null;
        this.isTargetLocked = false;
        this.lockedTargetPosition = null;
    }

    public boolean isLockedTargetValid() {
        return lockedTarget != null && lockedTarget.isAlive();
    }
}