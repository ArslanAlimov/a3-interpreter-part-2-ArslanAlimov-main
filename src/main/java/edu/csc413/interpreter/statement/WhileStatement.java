package edu.csc413.interpreter.statement;
import edu.csc413.interpreter.ProgramState;
import edu.csc413.interpreter.expression.Condition;

import java.util.List;
public class WhileStatement extends BlockStatement {

    public WhileStatement(Condition condition, List<Statement> body) {
        super(condition, body);
    }
    @Override
    public void run(ProgramState programState) {
        while (getCondition().evaluate(programState)) {
            for (Statement tempState : getStatement()) {
                tempState.run(programState);
                if (programState.hasReturnValue()) {
                    return;
                }
            }
        }
    }
}
