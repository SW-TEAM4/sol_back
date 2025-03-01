package org.team4.sol_server.domain.test;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {
  @PostMapping("/test")
  public ResponseEntity<String> test() {
      return ResponseEntity.ok().body("test ok");
  }
}
