/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.impl;

import me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.Animation;
import me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.Direction;

public class DecelerateAnimation extends Animation {

    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x) {
        double x1 = x / getDuration();
        return 1 - ((x1 - 1) * (x1 - 1));
    }
}