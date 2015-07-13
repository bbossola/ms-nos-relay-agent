package com.workshare.msnos.relay_agent.ui;

import java.io.BufferedReader;
import java.io.PrintStream;

public interface Console {

    public BufferedReader in()
    ;

    public PrintStream out()
    ;

    public PrintStream err()
    ;

}