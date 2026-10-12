package io.raditc.interview;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Case cannot be closed")
public class CaseConflictException extends RuntimeException {
 public CaseConflictException(String id) { super("Case cannot be closed: " + id); }
}
