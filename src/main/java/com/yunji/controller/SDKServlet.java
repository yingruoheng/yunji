package com.yunji.controller;

import point.SDKPointCollection;
import util.SysConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SDKServlet extends HttpServlet {
    private static String host;
    private static int port;

    static {
        host = SysConfig.getString("waggle.host");
        port = SysConfig.getInteger("waggle.port");
        SDKPointCollection pointCollection = new SDKPointCollection(host, port);
        pointCollection.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer buffer = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            buffer = new StringBuffer();
            String msg = null;
            while((msg=in.readLine()) != null){
                buffer.append(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        SDKPointCollection.pointCollection(buffer.toString(),"1001000000013");
    }

}
