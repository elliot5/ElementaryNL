package com.elementarynl;

import java.net.InetSocketAddress;

public interface NetworkWorker {
    void start(int port);
    void connect(InetSocketAddress address);
    void stop();
}
