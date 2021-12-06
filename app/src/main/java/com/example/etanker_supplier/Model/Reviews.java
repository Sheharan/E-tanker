package com.example.etanker_supplier.Model;

public class Reviews {
    private String deliveredDate;
    private String feedback;
    private String feedbackBy;
    private String rating;

    public Reviews() {
    }

    public Reviews(String deliveredDate, String feedback, String feedbackBy, String rating) {
        this.deliveredDate = deliveredDate;
        this.feedback = feedback;
        this.feedbackBy = feedbackBy;
        this.rating = rating;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackBy() {
        return feedbackBy;
    }

    public void setFeedback_By(String feedbackBy) {
        this.feedbackBy = feedbackBy;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
