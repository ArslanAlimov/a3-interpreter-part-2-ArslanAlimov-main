package edu.csc413.interpreter.expression;

import edu.csc413.interpreter.ProgramState;

import edu.csc413.interpreter.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class FunctionCallExpression implements Expression {
    private final String fName;
    private final List<Expression> pVal;

    public FunctionCallExpression(String fName, List<Expression> pVal) {
        this.pVal = pVal;
        this.fName = fName;

    }

    @Override
    public int evaluate(ProgramState programState) {

        List<Integer> valList = new LinkedList<>();
        for (Expression expression : pVal) {
            valList.add(expression.evaluate(programState));
        }
        int num = 0;
        programState.addCallFrame();
        for (String pName : programState.getParameterNames(fName)) {
                 programState.setVariable(pName, valList.get(num));
                  num++;
        }
        for (Statement tempSt : programState.getFunctionStatements(fName)) {
            tempSt.run(programState);

            if (programState.hasReturnValue()) {
                int retVal = programState.getReturnValue();
                programState.clearReturnValue();
                programState.removeCallFrame();
                return retVal;

            }
        }
        programState.removeCallFrame();
        return -1;
    }
}
