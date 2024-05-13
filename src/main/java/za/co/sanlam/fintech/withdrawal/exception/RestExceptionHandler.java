package za.co.sanlam.fintech.withdrawal.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import za.co.sanlam.fintech.withdrawal.dto.ErrorResponse;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> defaultErrorHandler(WebRequest req, Exception e) {
		log.error("Request to url {} resulted in an error", req.getDescription(false), e);
		var errorResponse = ErrorResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).details(e.getLocalizedMessage()).build();
		return ResponseEntity.internalServerError().body(errorResponse);
	}
}
