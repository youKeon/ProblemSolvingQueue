package com.psq.backend.global.error;

import com.psq.backend.bookmark.exception.DuplicatedBookmarkException;
import com.psq.backend.bookmark.exception.NoSuchBookmarkException;
import com.psq.backend.member.exception.DuplicatedEmailException;
import com.psq.backend.member.exception.InvalidEmailFormatException;
import com.psq.backend.member.exception.NoSuchMemberException;
import com.psq.backend.member.exception.UnauthorizedMemberException;
import com.psq.backend.problem.exception.InvalidProblemException;
import com.psq.backend.problem.exception.NoSuchProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "%s 필드는 %s (전달된 값: %s)";


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError firstFieldError = ex.getFieldErrors().get(0);
        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT, firstFieldError.getField(),
                firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 데이터 타입입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            NoSuchProblemException.class,
            NoSuchBookmarkException.class,
            NoSuchMemberException.class,
    })
    public ResponseEntity<ErrorResponse> handleNoSuchData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({
            InvalidProblemException.class,
            InvalidEmailFormatException.class,
            DuplicatedBookmarkException.class,
            DuplicatedEmailException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedMemberException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception e,
                                                                   final HttpServletRequest request) {
        ErrorReportRequest errorReport = new ErrorReportRequest(request, e);
        log.error(errorReport.getLogMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("예상하지 못한 서버 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
