package com.container;

import com.container.component.Component;

/**
 * IOC container: manages all the components in the system and the dependencies between them.
 * When the application is initialized, the dependencies between components should be defined first, and then all components should be added to the IOC container.
 * When an application needs a component, it can be obtained directly from the container.
 */
public interface Container
{
    /**
     * Add components to the container
     * @param id the key that uniquely identifies this component
     * @param component
     */
    void addComponent(String id, Component component);

    /**
     * Get components
     * @param id component id
     * @param <T> return value type
     * @return specifies the id component
     */
    <T> T getComponent(String id);
}
