package com.daniel.service.controller

import com.daniel.fetch.bean.PayerTransaction
import com.daniel.fetch.bean.PointsTransaction
import com.daniel.fetch.controller.PointsController
import com.daniel.fetch.service.PointsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import java.time.ZonedDateTime

class PointsControllerSpec extends Specification {

    PointsController pointsController
    PointsService pointsService = Mock(PointsService)

    def setup() {
        pointsController = new PointsController(
                pointsService: pointsService
        )
    }

    def 'Should_Return_200_When_Adding_Transaction_For_Payer'() {
        given:
        PayerTransaction payerTransaction = new PayerTransaction(payer:'DANNON', points:200, timestamp: ZonedDateTime.now())

        when:
        ResponseEntity response = pointsController.addTransactionForPayer(payerTransaction)

        then:
        1 * pointsService.addTransactionForPayer(_ as PayerTransaction)
        response.statusCode == HttpStatus.OK
    }

    def 'Should_Spend_Points_And_Return_Points_Spent_By_Payer'() {
        given:
        List<PayerTransaction> pointsSpend = [
                new PayerTransaction(payer:'DANNON', points:-500)
        ]

        when:
        ResponseEntity response = pointsController.spendPointsForUser(new PointsTransaction(points:500))

        then:
        1 * pointsService.spendPointsForUser(_ as PointsTransaction) >> pointsSpend
        response.statusCode == HttpStatus.OK
        response.body.get(0).getPoints() == -500
    }

    def 'Should_Return_Points_Balance_For_Each_Payer'() {
        given:
        Map<String, Integer> pointsForPayers = [:]
        pointsForPayers.put('DANNON', 1000)

        when:
        ResponseEntity response = pointsController.getPayerBalancesForUser()

        then:
        1 * pointsService.getPayerBalancesForUser() >> pointsForPayers
        response.statusCode == HttpStatus.OK
        response.body.get('DANNON') == 1000
    }
}
