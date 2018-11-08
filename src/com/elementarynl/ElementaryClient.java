package com.elementarynl;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class ElementaryClient extends BaseNetworkWorker {

    public ElementaryClient(InetSocketAddress address) {
        start(address.getPort());
    }

    @Override
    protected void receive(DatagramPacket packet) {
        String a = new String(packet.getData());
        System.out.println(a);
    }
}
