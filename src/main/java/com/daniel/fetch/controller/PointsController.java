package com.daniel.fetch.controller;

import com.daniel.fetch.bean.PayerTransaction;
import com.daniel.fetch.bean.PointsTransaction;
import com.daniel.fetch.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
public class PointsController {

    @Autowired
    private PointsService pointsService;

    @RequestMapping(value = "/v1/points/transactions", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTransactionForPayer(@RequestBody PayerTransaction payerTransaction) {
        pointsService.addTransactionForPayer(payerTransaction);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/v1/points/spend", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PayerTransaction>> spendPointsForUser(@RequestBody PointsTransaction pointsTransaction) {
        List<PayerTransaction> spentPoints = pointsService.spendPointsForUser(pointsTransaction);
        return ResponseEntity.ok().body(spentPoints);
    }


    @RequestMapping(value = "/v1/points/balance", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> getPayerBalancesForUser() {
        return ResponseEntity.ok().body(pointsService.getPayerBalancesForUser());
    }
}
