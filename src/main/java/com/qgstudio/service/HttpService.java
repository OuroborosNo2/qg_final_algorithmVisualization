package com.qgstudio.service;

import java.util.Map;
/**
 * @author OuroborosNo2
 * @since 2022-8-17
 */
public interface HttpService {
    String sendRequest(String url, Map<String, Object> params);
}
