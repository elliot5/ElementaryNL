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

import com.elementarynl.event.ElementaryListener;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author      Elliot Bewey <elliotbewey@yahoo.co.uk>
 * @since       0.2
 */
public interface NetworkWorker {

    /**
     * Sets the listening port and schedules the listening loop.
     *
     * @param port The port to be listened on (must be between 0 and 65535)
     */
    void start(int port);

    /**
     * Sets the remote endpoint.
     *
     * @param address The remote endpoint.
     */
    void connect(InetSocketAddress address);

    /**
     * Sets the remote endpoint.
     *
     * @param address The remote endpoint's address
     * @param port The remote endpoint's port
     */
    void connect(InetAddress address, int port);

    /**
     * Allows for all connections to enter listener (server mode).
     */
    void bind();

    /**
     * Sets the listener to the given event listener,
     * it's used to pick up on receive packet events
     * amongst other things.
     *
     * @see ElementaryListener
     * @param listener The listener to be assigned
     */
    void setListener(final ElementaryListener listener);

    /**
     * Sends the given packet to the destination set within the DatagramPacket
     *
     * @see DatagramPacket
     * @param packet The DatagramPacket to be sent
     */
    void send(DatagramPacket packet);

    /**
     * Sends the given byte data to the socket address
     *
     * @see InetSocketAddress
     * @param data The byte data to be sent
     * @param address The address the data should be sent to
     */
    void send(byte[] data, InetSocketAddress address);

    /**
     * Sends the given string to the given socket address;
     *
     * @see InetSocketAddress
     * @param string The string data to be sent
     * @param address The address the string should be sent to
     */
    void send(String string, InetSocketAddress address);

    /**
     * Stops the NetworkWorker, ending the listening loop.
     * start(), must be called again to resume use.
     */
    void stop();
}
