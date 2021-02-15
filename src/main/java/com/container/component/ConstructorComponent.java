package com.container.component;

import com.container.exception.ContainerException;
import com.container.exception.Message;
import com.container.util.ReflectUtils;

import java.util.Arrays;

/**
 * Use the constructor to create objects
 */
public class ConstructorComponent implements Component
{
    private final Class<?> type;
    private final Component[] params;

    public ConstructorComponent(Class<?> type, Component... params)
    {
        this.type = type;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.create(type, p);
        }
        catch (Exception e)
        {
            throw new ContainerException(Message.constructorNotFound(type, p), e);
        }
    }
}
