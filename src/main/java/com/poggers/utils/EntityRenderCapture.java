package com.poggers.utils;

import net.minecraft.entity.Entity;

public class EntityRenderCapture {
    public static final ThreadLocal<Entity> CURRENT_RENDERED_ENTITY = new ThreadLocal<>();
}
