package com.container.component;

import com.container.Container;

/**
 * References to components in the container
 */
public class ReferenceComponent implements Component
{
    private final Container container;
    private final String id;

    public ReferenceComponent(Container container, String id)
    {
        this.container = container;
        this.id = id;
    }

    @Override
    public Object create()
    {
        return container.getComponent(id);
    }
}
