package io.raditc.interview;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController
public class CaseController {
 private final CaseService service;
 public CaseController(CaseService service) { this.service = service; }
 @GetMapping("/api/health") public Map<String, String> health() { return Map.of("status", "ok"); }
 @GetMapping("/api/cases/{id}") public CaseRecord get(@PathVariable String id) { return service.getCase(id); }
 @PostMapping("/api/cases/{id}/close") public CaseRecord close(@PathVariable String id) { return service.closeCase(id); }
}
