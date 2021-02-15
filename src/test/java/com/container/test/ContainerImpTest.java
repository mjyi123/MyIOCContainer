package com.container.test;

import com.container.ContainerImp;
import com.container.Container;
import com.container.exception.ContainerException;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static com.container.component.Component.*;

public class ContainerImpTest
{
    @Test
    public void test()
    {
        Container container = new ContainerImp();
        container.addComponent("c1", constructor(String.class, value("hello")));
        container.addComponent("c2", staticFactory(List.class, "of", value(1), value(2), value(3)));
        assertEquals("hello", container.getComponent("c1"));
        assertEquals(List.of(1, 2, 3), container.getComponent("c2"));

        assertThrows(ContainerException.class,
                () -> container.addComponent("c3", null));
        assertThrows(ContainerException.class,
                () -> container.getComponent("c3"));
    }
}
