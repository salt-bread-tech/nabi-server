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
    private int userUid;
    private LoginResponse(String token, int userUid) {
        super();
        this.token = token;
        this.expirationTime = 3600;
        this.userUid = userUid;
    }

    public static ResponseEntity<LoginResponse> success(String token, int userUid) {
        LoginResponse response = new LoginResponse(token, userUid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static ResponseEntity<ResponseDto> loginFail() {
        ResponseDto response = new ResponseDto(ResponseCode.SIGN_IN_FAIL
                , ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
