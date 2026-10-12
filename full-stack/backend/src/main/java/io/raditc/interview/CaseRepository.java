package io.raditc.interview;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;
@Repository
public class CaseRepository {
 private final ConcurrentHashMap<String, CaseRecord> cases = new ConcurrentHashMap<>();
 public CaseRepository() {
  cases.put("case-001", new CaseRecord("case-001", "TEST-001", CaseStatus.OPEN));
  cases.put("case-002", new CaseRecord("case-002", "TEST-002", CaseStatus.CLOSED));
 }
 public Optional<CaseRecord> findById(String id) {
  return Optional.ofNullable(cases.get(id)).map(CaseRecord::copy);
 }
 public CaseRecord save(CaseRecord record) {
  cases.put(record.getId(), record.copy());
  return record.copy();
 }
}
