package io.raditc.interview;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Tag("exercise")
class CloseCaseTest {
 @Test void closesAnOpenCase() {
  CaseRepository repository = mock(CaseRepository.class);
  when(repository.findById("case-001")).thenReturn(Optional.of(new CaseRecord("case-001", "TEST-001", CaseStatus.OPEN)));
  when(repository.save(any(CaseRecord.class))).thenAnswer(call -> call.getArgument(0));
  CaseRecord updated = new CaseService(repository).closeCase("case-001");
  assertEquals(CaseStatus.CLOSED, updated.getStatus());
  verify(repository).save(any(CaseRecord.class));
 }
 // TODO: add focused tests for the other required behaviours.
}
