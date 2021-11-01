package com.fundraisey.backend.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseTemplate {
    public Map<String, Object> success(Object data) {
        Map<String, Object> map = new HashMap();
        try {
            map.put("data", data);
            map.put("status", 200);
            map.put("message", "success");
        } catch (Exception e) {
            map.put("status", 500);
            map.put("message", e);
        }
        return map;
    }

    public Map notFound(String message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 404);
        map.put("message", message);
        return map;
    }

    public Map isRequired(String message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 400);
        map.put("message", message);
        return map;
    }

    public Map internalServerError(Object message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 500);
        map.put("message", message);
        return map;
    }
}
