package edu.csc413.interpreter.statement;
import edu.csc413.interpreter.ProgramState;
import edu.csc413.interpreter.expression.Condition;
import java.util.List;
public abstract class BlockStatement implements Statement {
    private final Condition condition;
    private final List<Statement> bodyStatement;

    public BlockStatement(Condition condition, List<Statement>bodyStatement)
    {
        this.condition = condition;
        this.bodyStatement = bodyStatement;
    }

    public abstract void run(ProgramState programState);

    protected Condition getCondition() {
        return  condition;
    }
    protected List<Statement> getStatement()
    {
        return bodyStatement;
    }
    // to fix project
    protected void blockStatement(ProgramState programState) {
        for(Statement tStatement: bodyStatement){
            tStatement.run(programState);
        }
    }


}