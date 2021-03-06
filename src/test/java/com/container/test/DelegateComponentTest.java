package com.container.test;

import com.container.component.DelegateComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class DelegateComponentTest
{
    @Test
    public void test()
    {
        DelegateComponent c = new DelegateComponent();
        assertNull(c.create());
        c.setComponent(constructor(String.class, value("hello")));
        assertEquals("hello", c.create());
    }
}
