package com.lathika.lathikamart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lathika.lathikamart.dto.ApiResponseDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility for JSON serialization and writing standardized API responses.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private JsonUtil() {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static void sendSuccess(HttpServletResponse response, int statusCode, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        ApiResponseDTO apiResponse = ApiResponseDTO.success(data);
        PrintWriter writer = response.getWriter();
        writer.print(GSON.toJson(apiResponse));
        writer.flush();
    }

    public static void sendError(HttpServletResponse response, int statusCode, String errorCode, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        ApiResponseDTO apiResponse = ApiResponseDTO.error(errorCode, message);
        PrintWriter writer = response.getWriter();
        writer.print(GSON.toJson(apiResponse));
        writer.flush();
    }
}
