package homestay.Server.DTOs;

import com.google.gson.JsonElement;

public class BaseDTO {
    public static class Request{
        private String action;
        private JsonElement data;

        public String getAction(){ return this.action; }
        public JsonElement getData() { return this.data; }
    }

    public static record Response(
        int statusCode,
        String message,
        JsonElement data
    ) {}
}
