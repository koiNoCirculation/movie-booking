package dev.youtiao.movie_booking.dto;

public class Response {
    private boolean succeed;
    private String message;
    private Object data;

    private Response() {};

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public static class ResponseBuilder {
        boolean succeed;
        String message;
        Object data;
        public ResponseBuilder setSucceed(boolean succeed) {
            this.succeed = succeed;
            return this;
        }

        public ResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder setData(Object data) {
            this.data = data;
            return this;
        }
        public Response build() {
            Response response = new Response();
            response.message = message;
            response.succeed = succeed;
            response.data = data;
            return response;
        }
    }
}
