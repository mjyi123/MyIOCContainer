package com.container.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulate the creation results of multiple components into a list
 */
public class ListComponent implements Component
{
    private final Component[] components;

    public ListComponent(Component... components)
    {
        this.components = components;
    }

    @Override
    public Object create()
    {
        List<Object> list = new ArrayList<>();
        for (Component c : components)
        {
            list.add(c.create());
        }
        return list;
    }
}
