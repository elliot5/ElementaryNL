package com.elementarynl;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public abstract class Addressable {

    private InetSocketAddress address = null;

    protected void setAddress(int port) {
        InetAddress address = InetAddress.getLoopbackAddress();
        setAddress(new InetSocketAddress(address, port));
    }

    protected void setAddress(String addressName, int port) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(addressName);
        setAddress(new InetSocketAddress(address, port));
    }

    protected void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    protected boolean isAddressDefined() {
        return address != null;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getAddressPort() {
        return address.getPort();
    }

    public String getAddressName() {
        return address.getHostString();
    }

}
