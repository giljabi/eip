package kr.giljabi.eip.dto.response;

import lombok.ToString;

@ToString
public enum ResponseCode {

    SUCCESS                     ("200","OK"),
    SYSTEM_ERROR                ("500","시스템 오류"),
    BAD_REQUEST_ERROR           ("400","부적절한 요청 오류"),
    UNAUTHORIZED_ERROR          ("401","인증 오류"),
    RESOURCE_NOT_FOUND          ("404", "해당 리소스가 없음"),
    UNKNOWN_ERROR               ("999","알 수 없는 오류");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
