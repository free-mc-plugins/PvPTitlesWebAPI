package com.ericlam.mc.webhandler;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ResponseHandler extends AbstractHandler {
    private String datalist;
    private RefreshScheduler refreshScheduler;

    public ResponseHandler(){
        refreshScheduler =  RefreshScheduler.getInstance();
        datalist = refreshScheduler.getDatalist();
    }

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter writer = response.getWriter();

        switch (s){
            case "/refresh":
                HashMap<String,Boolean> result = new HashMap<>();
                result.put("success",refreshScheduler.refreshData());
                datalist = refreshScheduler.getDatalist();
                writer.println(JSONObject.toJSONString(result));
                break;
            case "/list":
                writer.println(datalist);
                break;
            default:
                writer.println(new JSONObject().toJSONString());
                break;
        }

        baseRequest.setHandled(true);
    }
}
