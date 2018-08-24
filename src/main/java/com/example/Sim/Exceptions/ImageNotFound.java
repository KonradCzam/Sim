package com.example.Sim.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ImageNotFound extends Exception {
    private String textMessage;
}
