package io.raditc.interview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class SetupTest {
 @Autowired MockMvc api;
 @Test void healthCheck() throws Exception { api.perform(get("/api/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok")); }
 @Test void seededCaseIsReadable() throws Exception { api.perform(get("/api/cases/case-001")).andExpect(status().isOk()).andExpect(jsonPath("$.reference").value("TEST-001")); }
 @Test void missingCaseIsNotFound() throws Exception { api.perform(get("/api/cases/missing")).andExpect(status().isNotFound()); }
}
