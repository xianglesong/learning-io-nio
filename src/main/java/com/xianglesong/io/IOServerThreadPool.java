/**
 * @CopyRight all rights reserved
 */

package com.xianglesong.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOServerThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOServerThreadPool.class);
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(9876));
        } catch (IOException ex) {
            LOGGER.error("Listen failed", ex);
            return;
        }
        try{
            while(true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    try{
                        InputStream inputstream = socket.getInputStream();
                        LOGGER.info("Received message {}", IOUtils.toString(new InputStreamReader(inputstream)));
                    } catch (IOException ex) {
                        LOGGER.error("Read message failed", ex);
                    }
                });
            }
        } catch(IOException ex) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
            LOGGER.error("Accept connection failed", ex);
        }
    }
}
