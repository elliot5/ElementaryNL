package com.elementarynl;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseNetworkWorker extends Addressable implements NetworkWorker {

    private ExecutorService executorService;
    private DatagramSocket datagramSocket;
    private boolean running;
    private boolean listening;

    final private int RECEIVE_BUFFER_SIZE = 1024;

    @Override
    public void start(int port) {
        executorService = Executors.newSingleThreadExecutor();
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        setAddress(port);
        running = true;
    }

    @Override
    public void connect(InetSocketAddress address) {
        if(running) {
            executorService.submit(() -> {
                try {
                    datagramSocket.connect(address);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            });
        } else {
            throw new NotRunningException("Cannot connect while service is stopped.");
        }
        listen();
    }

    protected void listen() {
        if(running) {
            listening = true;
            byte[] receiveData = new byte[RECEIVE_BUFFER_SIZE];
            executorService.submit(() -> {
                 while (listening) {
                     DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                     try {
                         datagramSocket.receive(receivePacket);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     receive(receivePacket);
                 }
            });
        }
    }

    public void send(byte[] data, InetSocketAddress address) {
        DatagramPacket packet = new DatagramPacket(data, data.length, address);
        send(packet);
    }

    public void send(DatagramPacket packet) {
        if(running) {
            try {
                datagramSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new NotRunningException("Cannot send while service is stopped.");
        }
    }

    abstract protected void receive(DatagramPacket packet);

    @Override
    public void stop() {
        if(running || listening) {
            running = false;
            listening = false;
            datagramSocket.close();
        } else {
            throw new NotRunningException("Cannot stop while service is already stopped.");
        }
    }
}
