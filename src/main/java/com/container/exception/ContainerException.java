package com.container.exception;

/**
 * Container exception base class
 */
public class ContainerException extends RuntimeException
{
    public ContainerException(String msg)
    {
        super(msg);
    }

    public ContainerException(String msg, Exception e)
    {
        super(msg, e);
    }
}
