package homestay.DTOs;

import com.google.gson.JsonElement;

public class BaseDTO {
    
    public record Request(
        String dir,
        String action,
        JsonElement data,
        String session
    ) {}

    public record Response(
        String dir,
        String action,
        int statusCode,
        String message,
        JsonElement data
    ) {}
}