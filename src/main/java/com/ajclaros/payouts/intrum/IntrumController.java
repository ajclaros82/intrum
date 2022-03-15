package com.ajclaros.payouts.intrum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class IntrumController {

    private final IntrumService intrumService;

    public IntrumController(IntrumService intrumService) {
        this.intrumService = intrumService;
    }

    @GetMapping
    public void getTestData() {
        intrumService.executePayouts();
    }

}
