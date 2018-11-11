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

import com.elementarynl.event.ElementaryListener;
import com.elementarynl.event.OutcomeState;
import com.elementarynl.network.ElementaryClient;
import com.elementarynl.network.ElementaryServer;
import com.elementarynl.network.worker.NetworkWorker;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class Main {


    public static void main(String [] args) {

        ElementaryListener listener = new ElementaryListener() {
            @Override
            public synchronized void onReceive(NetworkWorker worker, DatagramPacket packet) {
            }
        };

        ElementaryClient client = new ElementaryClient(21926, listener);
        ElementaryServer server = new ElementaryServer(21925, listener);

        client.connect(server.getSocketAddress());
        client.send("Hello", server.getSocketAddress());




    }
}
