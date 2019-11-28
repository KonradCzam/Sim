package com.example.Sim.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageNotFound extends Exception {
    private String textMessage;
}
