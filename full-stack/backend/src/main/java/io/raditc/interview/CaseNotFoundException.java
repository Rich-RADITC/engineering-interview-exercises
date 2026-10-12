package io.raditc.interview;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Case not found")
public class CaseNotFoundException extends RuntimeException {
 public CaseNotFoundException(String id) { super("Case not found: " + id); }
}
