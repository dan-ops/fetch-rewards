package com.daniel.service

import com.daniel.fetch.bean.PayerTransaction
import com.daniel.fetch.bean.PointsTransaction
import com.daniel.fetch.service.PointsService
import spock.lang.Specification

import java.time.ZonedDateTime

class PointsServiceSpec extends Specification {

    PointsService pointsService
    List<PayerTransaction> payerTransactions = []

    def setup() {
        pointsService = new PointsService(
                payerTransactions: payerTransactions
        )
    }

    def 'Should_Add_New_Transaction_For_Payer'() {
        given:
        PayerTransaction payerTransaction = new PayerTransaction(payer:'DANNON', points:200, timestamp: ZonedDateTime.now())

        when:
        pointsService.addTransactionForPayer(payerTransaction)

        then:
        payerTransactions.size() == 1
        payerTransactions.get(0).getPayer() == 'DANNON'
        payerTransactions.get(0).getPoints() == 200
    }

    def 'Should_Spend_Oldest_Points_First'() {
        given:
        PayerTransaction payerTransaction = new PayerTransaction(payer:'DANNON', points:500, timestamp: ZonedDateTime.now().minusDays(2))
        PayerTransaction payerTransactionTwo = new PayerTransaction(payer:'UNILEVER', points:200, timestamp: ZonedDateTime.now().minusDays(3))
        PayerTransaction payerTransactionThree = new PayerTransaction(payer:'DANNON', points:-100, timestamp: ZonedDateTime.now().minusDays(1))

        when:
        pointsService.addTransactionForPayer(payerTransaction)
        pointsService.addTransactionForPayer(payerTransactionTwo)
        pointsService.addTransactionForPayer(payerTransactionThree)
        List<PayerTransaction> result = pointsService.spendPointsForUser(new PointsTransaction(points: 300))

        then:
        result.size() == 2
        result.get(0).getPayer() == 'UNILEVER'
        result.get(0).getPoints() == -200
        result.get(1).getPayer() == 'DANNON'
        result.get(1).getPoints() == -100
    }

    def 'Should_Return_Points_Balance_For_All_Payers'() {
        given:
        PayerTransaction payerTransaction = new PayerTransaction(payer:'DANNON', points:500, timestamp: ZonedDateTime.now().minusDays(2))
        PayerTransaction payerTransactionTwo = new PayerTransaction(payer:'UNILEVER', points:200, timestamp: ZonedDateTime.now().minusDays(3))

        when:
        pointsService.addTransactionForPayer(payerTransaction)
        pointsService.addTransactionForPayer(payerTransactionTwo)
        Map<String, Integer> result = pointsService.getPayerBalancesForUser()

        then:
        result.size() == 2
        result.get('DANNON') == 500
        result.get('UNILEVER') == 200
    }
}
