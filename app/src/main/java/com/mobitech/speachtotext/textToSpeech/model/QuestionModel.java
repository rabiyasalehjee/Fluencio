package com.mobitech.speachtotext.textToSpeech.model;

public class QuestionModel {
    // "Category": "Basic",
    //      "instruction": "Always choose a weakness that doesn\u2019t impact your current job role.  Never brag about the strengths and skills that you cannot justify.",
    //      "quest_id ": 4,
    //      "question": "What are your weaknesses?",
    //      "suggested_answer": "I sometimes have dif
    String category,instruction,question,question_id,suggested_answer;

    public QuestionModel() {
    }

    public QuestionModel(String instruction, String question) {
        this.instruction = instruction;
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getSuggested_answer() {
        return suggested_answer;
    }

    public void setSuggested_answer(String suggested_answer) {
        this.suggested_answer = suggested_answer;
    }
}
