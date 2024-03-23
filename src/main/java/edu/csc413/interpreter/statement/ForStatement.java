package edu.csc413.interpreter.statement;

import edu.csc413.interpreter.ProgramState;
import edu.csc413.interpreter.expression.Expression;

import java.util.List;

public class ForStatement extends BlockStatement {
    private String loopVariable;
    private Expression rStart;
    private Expression rEnd;
    public ForStatement(String loopVariableName,Expression rStart,Expression rEnd,List<Statement> bodyStatements) {
        super(null, bodyStatements);
        this.loopVariable = loopVariableName;
        this.rStart = rStart;
        this.rEnd = rEnd;
    }
    @Override
    public void run(ProgramState programState) {
        int endVal = rEnd.evaluate(programState); // endvalue not sure if this can be done in a different way

        programState.setVariable(loopVariable, rStart.evaluate(programState));
        //porgramstate sets variable of loopvar , the start of range
        while (programState.getVariable(loopVariable) < endVal) {
            // gets the loop variable
            for (Statement tempState : getStatement()) {
                tempState.run(programState);
                if (programState.hasReturnValue()) {
                    return; // goes back to caller
                }
            }
            programState.setVariable(loopVariable, programState.getVariable(loopVariable) + 1);//i++
        }
    }
}
