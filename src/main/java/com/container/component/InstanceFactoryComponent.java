package com.container.component;

import com.container.exception.ContainerException;
import com.container.exception.Message;
import com.container.util.ReflectUtils;

import java.util.Arrays;

/**
 * Use the instance factory to create objects
 */
public class InstanceFactoryComponent implements Component
{
    private final Component instance;
    private final String method;
    private final Component[] params;

    public InstanceFactoryComponent(Component instance, String method, Component[] params)
    {
        this.instance = instance;
        this.method = method;
        this.params = params;
    }

    @Override
    public Object create()
    {
        Object i = instance.create();
        if (i == null) throw new ContainerException("Instance is null.");
        Object[] p = Arrays.stream(params).map(Component::create).toArray();

        try
        {
            return ReflectUtils.call(i, method, p);
        }
        catch (Exception e)
        {
            throw new ContainerException(Message.instanceFactoryNotFound(i.getClass(), method, p), e);
        }
    }
}
