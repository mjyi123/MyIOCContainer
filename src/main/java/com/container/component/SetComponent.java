package com.container.component;

import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulate the creation results of multiple components into a collection
 */
public class SetComponent implements Component
{
    private final Component[] components;

    public SetComponent(Component[] components)
    {
        this.components = components;
    }

    @Override
    public Object create()
    {
        Set<Object> set = new HashSet<>();
        for (Component c : components)
        {
            set.add(c.create());
        }
        return set;
    }
}
