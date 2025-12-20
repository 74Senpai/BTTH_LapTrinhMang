package homestay.Server.DTOs;

import com.google.gson.JsonElement;

public class BaseDTO {
    public static class Request{
        private String action;
        private JsonElement data;
        private String session;

        public String getAction(){ return this.action; }
        public JsonElement getData() { return this.data; }
        public String getSession() { return this.session; }
    }

    public static record Response(
        String action,
        int statusCode,
        String message,
        JsonElement data
    ) {}
}
