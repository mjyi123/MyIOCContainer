package com.container.test;

import com.container.ContainerImp;
import com.container.component.Component;
import com.container.Container;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class ReferenceComponentTest
{
    @Test
    public void test()
    {
        Container container = new ContainerImp();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", value(1234));
        Component c1 = reference(container, "c1");
        assertEquals("hello", c1.create());
        Component c2 = reference(container, "c2");
        assertEquals(1234, c2.create());
    }
}
