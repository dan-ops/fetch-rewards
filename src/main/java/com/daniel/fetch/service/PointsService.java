package com.daniel.fetch.service;

import com.daniel.fetch.bean.PayerTransaction;
import com.daniel.fetch.bean.PointsTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PointsService {

    @Autowired
    private List<PayerTransaction> payerTransactions;

    public void addTransactionForPayer(PayerTransaction payerTransaction) {
        payerTransactions.add(payerTransaction);
    }

    public List<PayerTransaction> spendPointsForUser(PointsTransaction pointsTransaction) {
        int pointsToSpend = pointsTransaction.getPoints();

        List<PayerTransaction> spentPointsForPayer = new ArrayList<>();
        Map<String, Integer> pointsUsedForPayer = new HashMap<>();
        Map<String, Integer> deductionsFromNegativeTransactions = new HashMap<>();
        List<PayerTransaction> updatedPayerTransactionList = new ArrayList<>();

        //at the time of spending points we will go back and apply the negative transactions
        payerTransactions.sort(Comparator.comparing(PayerTransaction::getTimestamp, (Comparator.reverseOrder())));
        for (PayerTransaction transaction : payerTransactions) {
            //negative transaction - set transaction to zero and negative points will be applied to a later transaction
            if (transaction.getPoints() < 0) {
                int toDeduct = transaction.getPoints();
                deductionsFromNegativeTransactions.put(transaction.getPayer(),
                        deductionsFromNegativeTransactions.getOrDefault(transaction.getPayer(), 0) + toDeduct);
                transaction.setPoints(0);
            //transaction has points to spend, and we have negative points to be applied for this payer
            } else if (deductionsFromNegativeTransactions.containsKey(transaction.getPayer())) {
                int pointsToDeduct = deductionsFromNegativeTransactions.get(transaction.getPayer());
                int pointsForTransaction = transaction.getPoints();
                if (pointsToDeduct < 0) {
                    //negative balance is grater than transaction balance
                    if (pointsForTransaction + pointsToDeduct < 0) {
                        pointsToDeduct = pointsForTransaction + pointsToDeduct;
                        transaction.setPoints(0);
                    } else {
                        transaction.setPoints(transaction.getPoints() + pointsToDeduct);
                        pointsToDeduct = 0;
                    }
                    deductionsFromNegativeTransactions.put(transaction.getPayer(), pointsToDeduct);
                }
            }
            updatedPayerTransactionList.add(transaction);
        }

        payerTransactions = updatedPayerTransactionList;
        payerTransactions.sort(Comparator.comparing(PayerTransaction::getTimestamp, (Comparator.naturalOrder())));

        //we will start spending the points starting with the last transaction
        for (PayerTransaction payerTransaction : payerTransactions) {
            if (payerTransaction.getPoints() > 0) {
                if (pointsToSpend >= payerTransaction.getPoints() ) {
                    pointsToSpend -= payerTransaction.getPoints();
                    pointsUsedForPayer.put(payerTransaction.getPayer(),
                            pointsUsedForPayer.getOrDefault(payerTransaction.getPayer(), 0) + payerTransaction.getPoints());
                    payerTransaction.setPoints(0);
                } else {
                    payerTransaction.setPoints(payerTransaction.getPoints() - pointsToSpend);
                    pointsUsedForPayer.put(payerTransaction.getPayer(),
                            pointsUsedForPayer.getOrDefault(payerTransaction.getPayer(), 0) + pointsToSpend);
                    pointsToSpend = 0;
                }
                if (pointsToSpend == 0) {
                    break;
                }
            }
        }

        for (String key : pointsUsedForPayer.keySet()) {
            PayerTransaction payerTransaction = new PayerTransaction();
            payerTransaction.setPayer(key);
            payerTransaction.setPoints(-1 * pointsUsedForPayer.get(key));
            spentPointsForPayer.add(payerTransaction);
        }

        return spentPointsForPayer;
    }

    public Map<String, Integer> getPayerBalancesForUser() {
        Map<String, Integer> balances = new HashMap<>();
        payerTransactions.forEach(payerTransaction -> {
            balances.put(payerTransaction.getPayer(),
                    balances.getOrDefault(payerTransaction.getPayer(), 0) + payerTransaction.getPoints());
        });
        return balances;
    }
}
