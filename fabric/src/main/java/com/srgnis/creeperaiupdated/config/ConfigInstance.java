package com.srgnis.creeperaiupdated.config;

public class ConfigInstance {

    public boolean fireExplosions;
    public boolean canBreach;
    public boolean canLeap;
    public int followingRadius;
    public int breachingRadius;
    public int maxY;
    public int minY;
    public double poweredChance;

    public ConfigInstance() {
        fireExplosions=true;
        canBreach=true;
        canLeap=true;
        followingRadius=32;
        breachingRadius=30;
        maxY=255;
        minY=0;
        poweredChance=0.3;
    }

}
