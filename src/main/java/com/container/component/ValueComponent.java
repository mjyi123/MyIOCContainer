package com.container.component;

/**
 * Wrap a constant component
 */
public class ValueComponent implements Component
{
    private final Object value;

    public ValueComponent(Object value)
    {
        this.value = value;
    }

    @Override
    public Object create()
    {
        return value;
    }
}
