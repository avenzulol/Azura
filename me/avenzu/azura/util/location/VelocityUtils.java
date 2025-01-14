package me.avenzu.azura.util.location;

import lombok.Getter;

@Getter
public class VelocityUtils {

    private final double horizontal, vertical;

    private long creationTime;

    public VelocityUtils(double horizontal, double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.creationTime = System.currentTimeMillis();
    }
}
