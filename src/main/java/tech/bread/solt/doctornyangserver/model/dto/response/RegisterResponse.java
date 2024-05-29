package tech.bread.solt.doctornyangserver.model.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.bread.solt.doctornyangserver.security.ResponseCode;
import tech.bread.solt.doctornyangserver.security.ResponseMessage;

@Getter
public class RegisterResponse extends ResponseDto {
    private RegisterResponse() {
        super();
    }

    public static ResponseEntity<RegisterResponse> success() {
        RegisterResponse response = new RegisterResponse();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static ResponseEntity<ResponseDto> nicknameFormatError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto(ResponseCode.NICKNAME_VALIDATION_FAIL, ResponseMessage.NICKNAME_VALIDATION_FAIL));
    }

    public static ResponseEntity<ResponseDto> idFormatError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto(ResponseCode.ID_VALIDATION_FAIL, ResponseMessage.ID_VALIDATION_FAIL));
    }

    public static ResponseEntity<ResponseDto> passwordFormatError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto(ResponseCode.PW_VALIDATION_FAIL, ResponseMessage.PW_VALIDATION_FAIL));
    }

    public static ResponseEntity<ResponseDto> heightFormatError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto(ResponseCode.HEIGHT_VALIDATION_FAIL, ResponseMessage.HEIGHT_VALIDATION_FAIL));
    }

    public static ResponseEntity<ResponseDto> weightFormatError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto(ResponseCode.WEIGHT_VALIDATION_FAIL, ResponseMessage.WEIGHT_VALIDATION_FAIL));
    }

    public static ResponseEntity<ResponseDto> duplicateId() {
        ResponseDto response = new ResponseDto(ResponseCode.DUPLICATE_ID
                , ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto response = new ResponseDto(ResponseCode.CERTIFICATION_FAIL
                , ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
