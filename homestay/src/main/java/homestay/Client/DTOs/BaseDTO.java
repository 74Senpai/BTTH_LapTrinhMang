package homestay.Client.DTOs;

import com.google.gson.JsonElement;

public class BaseDTO {
    public static class Request{
        private String action;
        private JsonElement data;
        private String session;

        public Request(String action, JsonElement data, String session) {
            this.action = action;
            this.data = data;
            this.session = session;
        }
    }

    public static class Response{
        private String action;
        private int statusCode;
        private String message;
        private JsonElement data;

        public String getAction() { return this.action; }
        public int getStatusCode() { return this.statusCode; }
        public String getMessage() { return this.message; }
        public JsonElement getData() { return this.data; }
    }
}

