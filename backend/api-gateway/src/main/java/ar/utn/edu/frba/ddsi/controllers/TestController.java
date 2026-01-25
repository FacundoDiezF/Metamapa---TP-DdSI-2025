package ar.utn.edu.frba.ddsi.controllers;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
public class TestController {

  // @RequestMapping atiende GET, POST, PUT, DELETE, etc.
  @RequestMapping("/test/dump-headers")
  public Mono<Map<String, String>> dumpHeaders(@RequestHeader Map<String, String> headers) {
    return Mono.just(headers);
  }
}