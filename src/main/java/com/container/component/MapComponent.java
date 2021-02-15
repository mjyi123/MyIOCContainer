package com.container.component;

import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulate the creation results of multiple components into a map
 */
public class MapComponent implements Component
{
    private final Map<Component, Component> componentMap;

    public MapComponent(Map<Component, Component> componentMap)
    {
        this.componentMap = componentMap;
    }

    @Override
    public Object create()
    {
        Map<Object, Object> map = new HashMap<>();
        for (Component k : componentMap.keySet())
        {
            map.put(k.create(), componentMap.get(k).create());
        }
        return map;
    }
}
