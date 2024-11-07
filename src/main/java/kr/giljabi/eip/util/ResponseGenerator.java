package kr.giljabi.eip.util;

import kr.giljabi.eip.dto.response.Response;
import kr.giljabi.eip.dto.response.ResponseCode;

public class ResponseGenerator {

    public static <D> Response<D> success(D data) {
        return new Response<>(ResponseCode.SUCCESS, null, data);
    }

    public static Response<Void> success() {
        return new Response<>(ResponseCode.SUCCESS);
    }

    public static Response<Void> fail(ResponseCode code, String msg){
        return new Response<>(code, msg, null);
    }

    public static Response<Void> fail(ResponseCode code){
        return new Response<>(code, null, null);
    }

}
