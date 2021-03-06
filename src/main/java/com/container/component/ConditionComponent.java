package com.container.component;

/**
 * Condition component: return c1 when predicate is true, otherwise return c2
 */
public class ConditionComponent implements Component
{
    private final Component predicate;
    private final Component c1;
    private final Component c2;

    public ConditionComponent(Component predicate, Component c1, Component c2)
    {
        this.predicate = predicate;
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public Object create()
    {
        Object p = predicate.create();
        if (p instanceof Boolean && (boolean) p) return c1.create();
        return c2.create();
    }
}
