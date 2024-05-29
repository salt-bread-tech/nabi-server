package tech.bread.solt.doctornyangserver.security;

public interface ResponseMessage {
    String SUCCESS = "Success.";
    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATE_ID = "Duplicate Id.";
    String SIGN_IN_FAIL = "Login information mismatch.";
    String CERTIFICATION_FAIL = "Certification failed.";
    String DATABASE_ERROR = "Database error.";

    String ID_VALIDATION_FAIL = "ID Validation failed.";
    String PW_VALIDATION_FAIL = "Password Validation failed.";
    String HEIGHT_VALIDATION_FAIL = "Height Validation failed.";
    String WEIGHT_VALIDATION_FAIL = "Weight Validation failed.";
    String NICKNAME_VALIDATION_FAIL = "Nickname Validation failed.";
}
