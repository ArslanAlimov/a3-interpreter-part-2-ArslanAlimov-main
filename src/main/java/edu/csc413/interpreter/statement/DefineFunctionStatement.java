package edu.csc413.interpreter.statement;

import edu.csc413.interpreter.ProgramState;

import java.util.List;

public class DefineFunctionStatement implements Statement {
    private String fName;
    private List<String> pName;
    private List<Statement> fState;

    public DefineFunctionStatement(String fName,List<String> pName,List<Statement> fState) {
        this.fName = fName;
        this.pName = pName;
        this.fState = fState;
    }
    @Override
    public void run(ProgramState programState) {
        programState.registerFunction(fName, pName, fState);
    }
}
