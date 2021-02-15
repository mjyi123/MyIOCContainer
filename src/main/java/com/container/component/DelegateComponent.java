package com.container.component;

/**
 * Proxy component: forward the create method of the component to the create method of another component.
 * This component is used to delay settings for partial components when parsing the configuration file.
 */
public class DelegateComponent implements Component
{
    private Component component = Component.value(null);

    public void setComponent(Component component)
    {
        this.component = component;
    }

    @Override
    public Object create()
    {
        return component.create();
    }
}
