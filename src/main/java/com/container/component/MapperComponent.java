package com.container.component;

/**
 * Convert the result created by a Component
 */
public class MapperComponent implements Component
{
    private final Component component;
    private final Mapper mapper;

    public MapperComponent(Component component, Mapper mapper)
    {
        this.component = component;
        this.mapper = mapper;
    }

    @Override
    public Object create()
    {
        return mapper.map(component.create());
    }
}
