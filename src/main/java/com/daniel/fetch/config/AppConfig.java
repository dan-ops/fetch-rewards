package com.daniel.fetch.config;

import com.daniel.fetch.bean.PayerTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.*;

@Configuration
public class AppConfig {

    @Bean(name = "payerTransactions")
    public List<PayerTransaction> getPayerTransactions() {
        return new ArrayList<>();
    }

}