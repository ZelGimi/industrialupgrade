package com.denfop.utils;

public class Triple<A, B,C>
{
    private A a;
    private B b;
    private C c;

    public Triple(A aIn, B bIn, C cIn)
    {
        this.a = aIn;
        this.b = bIn;
        this.c = cIn;
    }

    public A getFirst()
    {
        return this.a;
    }

    public B getSecond()
    {
        return this.b;
    }

    public C getThird()
    {
        return this.c;
    }
}
