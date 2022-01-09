package com.daniel.fetch.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PayerTransaction extends PointsTransaction implements Serializable {

    private static final long serialVersionUID = -5829820898071081507L;

    private String payer;
    private ZonedDateTime timestamp;
}
