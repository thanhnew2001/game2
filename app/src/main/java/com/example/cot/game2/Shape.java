package com.example.cot.game2;

import java.util.List;

/**
 * Created by CoT on 8/19/17.
 */

public class Shape {

    String name;

    int imageId;

    Letter[] letters;

    int currentLetter =0;

    public Shape(String name, int imageId, Letter[] letters) {
        this.name = name;
        this.imageId = imageId;
        this.letters = letters;
    }

    public Letter nextLetter(){

        currentLetter++;

        if(currentLetter>letters.length-1) currentLetter=0;

        return letters[currentLetter];
    }
}
