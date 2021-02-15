package com.container;

import com.container.component.Component;
import com.container.exception.ContainerException;
import com.container.exception.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC container implementation class
 */
public class ContainerImp implements Container
{
    private final Map<String, Component> components = new ConcurrentHashMap<>();

    @Override
    public void addComponent(String id, Component component)
    {
        if (component == null)
            throw new ContainerException(Message.parameterNotNull("component"));
        components.put(id, component);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getComponent(String id)
    {
        if (!components.containsKey(id))
            throw new ContainerException(Message.componentNotFound(id));
        return (T) components.get(id).create();
    }
}
