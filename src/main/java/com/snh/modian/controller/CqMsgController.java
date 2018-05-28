package com.snh.modian.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snh.modian.task.PkBroadcastTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
public class CqMsgController {

    @Value("${GROUPID}")
    private long GROUPID;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PkBroadcastTask pkBroadcastTask;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String GetMsg(HttpServletRequest httpServletRequest) {
        try {
            String result = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            if (result != null) {
                JsonNode jsonNode = objectMapper.readTree(result);
                if (jsonNode.get("group_id") != null && jsonNode.get("group_id").asLong() == GROUPID) {
                    pkBroadcastTask.pkBroadcast();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}