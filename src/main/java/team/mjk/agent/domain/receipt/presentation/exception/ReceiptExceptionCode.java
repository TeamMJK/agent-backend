package team.mjk.agent.domain.receipt.presentation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import team.mjk.agent.global.exception.ExceptionCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ReceiptExceptionCode implements ExceptionCode {
    EMPTY_FILE_EXCEPTION(BAD_REQUEST, "업로드된 파일이 비어 있습니다."),
    INVALID_FILE_EXTENSION(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    NO_FILE_EXTENSION(BAD_REQUEST, "파일에 확장자가 존재하지 않습니다."),
    ON_IMAGE_UPLOAD(INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다."),
    ON_IMAGE_DELETE(INTERNAL_SERVER_ERROR, "이미지 삭제 중 오류가 발생했습니다."),
    PUT_OBJECT_EXCEPTION(INTERNAL_SERVER_ERROR, "S3에 객체 업로드를 실패했습니다."),
    DELETE_NOT_FORBIDDEN(FORBIDDEN, "삭제 권한이 없습니다."),
    URL_NOT_FOUND(NOT_FOUND, "url을 찾을 수 없습니다."),
    NOT_FOUND_RECEIPT(NOT_FOUND, "영수증을 찾을 수 없습니다."),
    UPDATE_NOT_FORBIDDEN(FORBIDDEN, "수정 권한이 없습니다."),
    ON_IMAGE_DOWNLOAD(INTERNAL_SERVER_ERROR, "S3 이미지 다운로드 중 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }

}
