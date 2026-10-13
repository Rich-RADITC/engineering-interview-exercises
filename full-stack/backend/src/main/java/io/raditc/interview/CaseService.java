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
    CaseRecord record = repository.findById(id).orElseThrow(() -> new CaseNotFoundException(id));
    if(record.getStatus() == CaseStatus.CLOSED) {
      throw new CaseConflictException(id);
    }
    record.setStatus(CaseStatus.CLOSED);
    return repository.save(record);

  // TODO: implement the contract in full-stack/BRIEF.md.
  // This intentional 501 distinguishes unfinished work from a setup failure.
  //throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Interview task not implemented");
 }
}
