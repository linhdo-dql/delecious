package com.example.delicious.Model;

public class Step {
    String stepContent, imageUri;

    public Step(String stepContent, String imageUri) {
        this.stepContent = stepContent;
        this.imageUri = imageUri;
    }

    public String getStepContent() {
        return stepContent;
    }

    public void setStepContent(String stepContent) {
        this.stepContent = stepContent;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
