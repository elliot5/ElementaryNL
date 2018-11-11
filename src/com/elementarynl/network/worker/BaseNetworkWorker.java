/*
 *
 * MIT License
 *
 * Copyright (c) 2018 Elliot Bewey
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.elementarynl.network.worker;

import com.elementarynl.network.Addressable;
import com.elementarynl.event.NotRunningException;
import com.elementarynl.event.OutcomeState;
import com.elementarynl.event.ElementaryListener;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseNetworkWorker extends Addressable implements NetworkWorker {

    private DatagramSocket datagramSocket;
    private boolean running;
    private boolean listening;

    // DatagramChannel datagramChannel;

    final private int RECEIVE_BUFFER_SIZE = 1024;

    private ElementaryListener listener;

    protected BaseNetworkWorker(final ElementaryListener listener) {
        setListener(listener);
    }

    protected BaseNetworkWorker() { }

    @Override
    public void setListener(ElementaryListener listener) {
        this.listener = listener;
    }

    @Override
    public void start(int port) {
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        setAddress(port);
        running = true;
        listen();
    }

    @Override
    public void connect(InetAddress address, int port) {
        connect(new InetSocketAddress(address, port));
    }

    @Override
    public void connect(InetSocketAddress address) {
        if(running) {
            CompletableFuture
                    .supplyAsync(() -> connectAsync(address))
                    .thenAcceptAsync(state -> listener.onConnect(this, state));
        } else {
            throw new NotRunningException("Cannot connect while service is stopped.");
        }
    }

    private synchronized OutcomeState connectAsync(InetSocketAddress address) {
            try {
                datagramSocket.connect(address);
            } catch (SocketException e) {
                e.printStackTrace();
                return OutcomeState.FAILURE;
            }
            return OutcomeState.SUCCESS;
    }

    @Override
    public void bind() {
        if(running) {
            CompletableFuture
                    .supplyAsync(this::bindAsync)
                    .thenAcceptAsync(state -> listener.onBind(this, state));
        } else {
            throw new NotRunningException("Cannot connect while service is stopped.");
        }
    }

    private OutcomeState bindAsync() {
        try {
            datagramSocket.bind(getSocketAddress());
        } catch (SocketException e) {
            e.printStackTrace();
            return OutcomeState.FAILURE;
        }
        return OutcomeState.SUCCESS;
    }

    protected void listen() {
        if(running) {
            listening = true;
            CompletableFuture
                    .supplyAsync(this::listenAsync);
        }
    }

    private OutcomeState listenAsync() {
        while(listening) {
            byte[] receiveData = new byte[RECEIVE_BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                datagramSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                return OutcomeState.FAILURE;
            }
            listener.onReceive(this, receivePacket);
        }
        return OutcomeState.SUCCESS;
    }

    public void send(String string, InetSocketAddress address) {
        DatagramPacket packet = new DatagramPacket(string.getBytes(), string.getBytes().length, address);
        send(packet);
    }

    public void send(byte[] data, InetSocketAddress address)  {
        DatagramPacket packet = new DatagramPacket(data, data.length, address);
        send(packet);
    }

    public void send(DatagramPacket packet) {
        if(running) {
                    CompletableFuture
                    .supplyAsync(() -> sendAsync(packet))
                    .thenAcceptAsync((state) -> listener.onSent(this, state));
        } else {
            throw new NotRunningException("Cannot send while service is stopped.");
        }
    }

    private synchronized OutcomeState sendAsync(DatagramPacket packet) {
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return OutcomeState.FAILURE;
        }
        return OutcomeState.SUCCESS;
    }

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
