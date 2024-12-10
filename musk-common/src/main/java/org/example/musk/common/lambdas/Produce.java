package org.example.musk.common.lambdas;


import java.io.IOException;

@FunctionalInterface
public interface Produce {
    void produce() throws IOException;
}
