package com.mobitech.speachtotext.textToSpeech.model;

public class AssignmentModel {

    // "Code": "TPQ-234",
    //      "ID": 1001,
    //      "Name": "Presentation 1",
    //      "Subject": "Automata",
    //      "TeacherID": 23
    private String Code,ID,Name,Subject,TeacherID;

    public AssignmentModel() {
    }

    public AssignmentModel(String code, String ID, String name, String subject, String teacherID) {
        Code = code;
        this.ID = ID;
        Name = name;
        Subject = subject;
        TeacherID = teacherID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String teacherID) {
        TeacherID = teacherID;
    }
}
