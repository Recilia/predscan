/*
 * PredScan Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderMC/PredScan/
 */
package me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.impl;

import me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.Animation;
import me.rein.predscan.ui.client.clickgui.style.styles.predscandropdown.utils.animations.Direction;

public class EaseInOutQuad extends Animation {

    public EaseInOutQuad(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    protected double getEquation(double x1) {
        double x = x1 / getDuration();
        return x < 0.5 ? 2 * Math.pow(x, 2) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

}