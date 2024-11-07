package kr.giljabi.eip.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Response<T> {

    private String code;

    private String message;

    private T data;

    public Response(ResponseCode responseCode){
        this(responseCode, null, null);
    }

    public Response(ResponseCode responseCode, String responseMessage, T data) {
        this.code = responseCode.getCode();
        this.message = (responseMessage == null ? responseCode.getMessage() : responseMessage);
        this.data = data;
    }

}
