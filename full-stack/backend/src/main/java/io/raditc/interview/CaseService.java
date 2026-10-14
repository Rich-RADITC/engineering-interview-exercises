package io.raditc.interview;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Service
public class CaseService {
 private final CaseRepository repository;
 public CaseService(CaseRepository repository) { this.repository = repository; }
 public CaseRecord getCase(String id) {
  return repository.findById(id).orElseThrow(() -> new CaseNotFoundException(id));
 }
 public CaseRecord closeCase(String id) {
  CaseRecord record = getCase(id);
  if (record.getStatus() != CaseStatus.OPEN) {
   throw new CaseConflictException(id);
  }
  record.setStatus(CaseStatus.CLOSED);
  return repository.save(record);
 }
}
