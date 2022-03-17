package com.ajclaros.payoutsapp.payouts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payouts")
public class PayoutsController {

    private final PayoutsService payoutsService;

    public PayoutsController(PayoutsService payoutsService) {
        this.payoutsService = payoutsService;
    }

    @GetMapping("/test")
    public void getTestData() {
        payoutsService.executePayouts();
    }

}
