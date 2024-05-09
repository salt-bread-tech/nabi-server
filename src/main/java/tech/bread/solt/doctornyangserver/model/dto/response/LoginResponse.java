package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.bread.solt.doctornyangserver.security.ResponseCode;
import tech.bread.solt.doctornyangserver.security.ResponseMessage;

@Getter
public class LoginResponse extends ResponseDto{
    private String token;
    private int expirationTime;
    private LoginResponse(String token) {
        super();
        this.token = token;
        this.expirationTime = 3600;
    }

    public static ResponseEntity<LoginResponse> success(String token) {
        LoginResponse response = new LoginResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static ResponseEntity<ResponseDto> loginFail() {
        ResponseDto response = new ResponseDto(ResponseCode.SIGN_IN_FAIL
                , ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
