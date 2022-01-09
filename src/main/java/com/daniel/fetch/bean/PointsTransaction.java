package com.daniel.fetch.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PointsTransaction implements Serializable {

    private static final long serialVersionUID = 6741087231198774621L;

    private Integer points;
}