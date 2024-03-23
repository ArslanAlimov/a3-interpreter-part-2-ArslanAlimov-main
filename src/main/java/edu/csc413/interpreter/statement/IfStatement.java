package edu.csc413.interpreter.statement;

import edu.csc413.interpreter.ProgramState;
import edu.csc413.interpreter.expression.Condition;
import java.util.List;

/**
 * Statement representing an if-statement, with a condition check and a list of statements that are only run if the
 * condition evaluates to true.
 */
// TODO: Implement. IfStatement and WhileStatement share a lot in common; find a way to avoid code duplication.
public class IfStatement extends BlockStatement {
    public IfStatement(Condition condition, List<Statement> blockStatements){
        super(condition, blockStatements);
    }
    // Tried to understand this method

    @Override
    public void run(ProgramState programState) {
        if(getCondition().evaluate(programState)){
            super.blockStatement(programState);
        }
    }
}

