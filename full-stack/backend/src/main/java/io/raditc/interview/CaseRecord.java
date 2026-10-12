package io.raditc.interview;
public class CaseRecord {
 private final String id;
 private final String reference;
 private CaseStatus status;
 public CaseRecord(String id, String reference, CaseStatus status) {
  this.id = id; this.reference = reference; this.status = status;
 }
 public String getId() { return id; }
 public String getReference() { return reference; }
 public CaseStatus getStatus() { return status; }
 public void setStatus(CaseStatus status) { this.status = status; }
 public CaseRecord copy() { return new CaseRecord(id, reference, status); }
}
