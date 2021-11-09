package com.fundraisey.backend.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseTemplate {
    public Map<String, Object> success(Object data) {
        Map<String, Object> map = new HashMap();
        try {
            if (data != null) {
                map.put("data", data);
            }
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

    public Map alreadyExist(String message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 403);
        map.put("message", message);
        return map;
    }

    public Map notAllowed(String message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 403);
        map.put("message", message);
        return map;
    }

    public Map internalServerError(Object message) {
        Map<String, Object> map = new HashMap();
        map.put("status", 500);
        map.put("message", message);
        return map;
    }

    public ResponseEntity<Map> controllerHttpRestResponse(Map response) {
        if ((Integer) response.get("status") == 200) {
            return new ResponseEntity<Map>(response, HttpStatus.OK);
        } else if ((Integer) response.get("status") == 400) {
            return new ResponseEntity<Map>(response, HttpStatus.BAD_REQUEST);
        } else if ((Integer) response.get("status") == 403) {
            return new ResponseEntity<Map>(response, HttpStatus.NOT_FOUND);
        } else if ((Integer) response.get("status") == 404) {
            return new ResponseEntity<Map>(response, HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<Map>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
