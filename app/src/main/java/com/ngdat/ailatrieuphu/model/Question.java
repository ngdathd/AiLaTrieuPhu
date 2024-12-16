package com.ngdat.ailatrieuphu.model;

public class Question {
    private String question;
    private String caseA;
    private String caseB;
    private String caseC;
    private String caseD;
    private int trueCase, level;

    public Question(){
    }

    public Question(String question, String caseA,
                    String caseB, String caseC,
                    String caseD, int trueCase, int level) {
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.trueCase = trueCase;
        this.level = level;
    }
    @Override
    public String toString() {
        return question+"\n"+
                "level"+level+"\n"+
                "A:"+caseA+"\n"+
                "B:"+caseB+"\n"+
                "C:"+caseC+"\n"+
                "D:"+caseD+"\n"+
                "Answer:"+trueCase+"\n";
    }
    public int getLevel() {
        return level;
    }

    public String getQuestion() {
        return question;
    }

    public String getCaseA() {
        return caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public int getTrueCase() {
        return trueCase;
    }

}