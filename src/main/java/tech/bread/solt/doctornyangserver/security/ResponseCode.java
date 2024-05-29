package tech.bread.solt.doctornyangserver.security;

public interface ResponseCode {
    String SUCCESS = "SU";
    String VALIDATION_FAIL = "VF";
    String DUPLICATE_ID = "DI";
    String SIGN_IN_FAIL = "SF";
    String CERTIFICATION_FAIL = "CF";
    String DATABASE_ERROR = "DBE";

    String ID_VALIDATION_FAIL = "IVF";
    String PW_VALIDATION_FAIL = "PVF";
    String HEIGHT_VALIDATION_FAIL = "HVF";
    String WEIGHT_VALIDATION_FAIL = "WVF";
    String NICKNAME_VALIDATION_FAIL = "NVF";
}
