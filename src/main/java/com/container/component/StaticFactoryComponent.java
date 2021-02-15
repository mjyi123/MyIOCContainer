package com.container.component;

import com.container.exception.ContainerException;
import com.container.exception.Message;
import com.container.util.ReflectUtils;

import java.util.Arrays;

/**
 * Use static factory to create objects
 */
public class StaticFactoryComponent implements Component
{
    private final Class<?> type;
    private final String method;
    private final Component[] params;

    public StaticFactoryComponent(Class<?> type, String method, Component[] params)
    {
        this.type = type;
        this.method = method;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.call(type, method, p);
        }
        catch (Exception e)
        {
            throw new ContainerException(Message.staticFactoryNotFound(type, method, p), e);
        }
    }
}
