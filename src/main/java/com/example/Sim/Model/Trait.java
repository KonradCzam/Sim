package com.example.Sim.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
public class Trait implements Serializable {
    private String name;
}
