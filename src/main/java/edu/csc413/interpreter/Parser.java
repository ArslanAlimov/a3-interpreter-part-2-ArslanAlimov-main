package edu.csc413.interpreter;

import edu.csc413.interpreter.expression.*;
import edu.csc413.interpreter.statement.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Parser class is used to convert information that Interpreter pulls from the program lines into actual Statement,
 * Expression, and Condition objects. You will need to implement all of the methods marked by TODO.
 *
 * You will find it useful to call parseExpression and parseCondition in your implementation. Both of these are already
 * complete and don't need to be modified, though they do rely on your createConstantExpression, etc. methods being
 * implemented.
 */
public class Parser {
    public Expression createConstantExpression(int value) {
        return new ConstantExpression(value);
    }

    public Expression createVariableExpression(String variableName) {
        return new VariableExpression(variableName);
    }

    public Expression createArithmeticExpression(String operator, String lhsAsString, String rhsAsString) {
        Expression lhs = parseExpression(lhsAsString);
        Expression rhs = parseExpression(rhsAsString);
        return switch (operator) {
            case "+" -> new AddExpression(lhs, rhs);
            case "-" -> new SubtractExpression(lhs, rhs);
            case "*" -> new MultiplyExpression(lhs, rhs);
            case "/" -> new DivideExpression(lhs, rhs);
            default -> throw new RuntimeException("Unrecognized operator: " + operator);
        };
    }

    public Condition createCondition(String operator, String lhsAsString, String rhsAsString) {
        Expression lhs = parseExpression(lhsAsString);
        Expression rhs = parseExpression(rhsAsString);
        return switch (operator) {
            case "<" -> new LessThanCondition(lhs, rhs);
            case ">" -> new GreaterThanCondition(lhs, rhs);
            case "==" -> new EqualsCondition(lhs, rhs);
            default -> throw new RuntimeException("Unrecognized operator: " + operator);
        };
    }

    public Statement createPrintStatement(String expressionAsString) {
        return new PrintStatement(parseExpression(expressionAsString));
    }

    public Statement createAssignStatement(String variableName, String expressionAsString) {
        return new AssignStatement(variableName, parseExpression(expressionAsString));
    }

    public Statement createIfStatement(String conditionAsString, List<Statement> bodyStatements) {
        return new IfStatement(parseCondition(conditionAsString), bodyStatements);
    }

    public Statement createWhileStatement(String conditionAsString, List<Statement> bodyStatements) {
        // TODO: Implement.
        return new WhileStatement(parseCondition(conditionAsString), bodyStatements);
    }

    public Statement createForStatement(
            String loopVariableName,
            String rangeStartAsString,
            String rangeEndAsString,
            List<Statement> bodyStatements) {
        // TODO: Implement.
        return new ForStatement(
                loopVariableName,
                parseExpression(rangeStartAsString),
                parseExpression(rangeEndAsString),
                bodyStatements);
    }

    public Statement createDefineFunctionStatement(
            String functionName, List<String> parameterNames, List<Statement> functionStatements) {
        // TODO: Implement.
        return new DefineFunctionStatement(functionName, parameterNames, functionStatements);
    }

    public Statement createReturnStatement(String expressionAsString) {
        // TODO: Implement.
        return new ReturnStatement(parseExpression(expressionAsString));
    }

    public Expression createFunctionCallExpression(String functionName, List<String> parameterValuesAsStrings) {
        // TODO: Implement.
        List<Expression> values = new LinkedList<>();
        for (String temp : parameterValuesAsStrings) {
            values.add(parseExpression(temp));
        }
        return new FunctionCallExpression(functionName, values);
    }

    /** Converts a String representing an expression into an Expression object, based on the pattern detected. */
    private Expression parseExpression(String expressionAsString) {
        if (expressionAsString.matches(CONSTANT_PATTERN.pattern())) {
            return createConstantExpression(Integer.parseInt(expressionAsString));
        }
        if (expressionAsString.matches(VARIABLE_NAME_PATTERN.pattern())) {
            return createVariableExpression(expressionAsString);
        }
        Matcher functionCallMatcher = FUNCTION_CALL_PATTERN.matcher(expressionAsString);
        if (functionCallMatcher.matches()) {
            String functionName = functionCallMatcher.group(1).trim();
            List<String> parameterValuesAsStrings =
                    Arrays.stream(functionCallMatcher.group(2).split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
            if (parameterValuesAsStrings.size() == 1 && parameterValuesAsStrings.get(0).isEmpty()) {
                parameterValuesAsStrings.clear();
            }
            return createFunctionCallExpression(functionName, parameterValuesAsStrings);
        }

        int parenthesisCount = 0;
        for (int i = 0; i < expressionAsString.length(); i++) {
            char ch = expressionAsString.charAt(i);
            if (ch == '(') {
                parenthesisCount++;
            } else if (ch == ')') {
                parenthesisCount--;
            } else if (parenthesisCount == 0
                    && Arrays.stream(ARITHMETIC_OPERATORS).anyMatch(op -> op.charAt(0) == ch)) {
                String lhsAsString = expressionAsString.substring(0, i).trim();
                String rhsAsString = expressionAsString.substring(i + 1).trim();
                return createArithmeticExpression(String.valueOf(ch), lhsAsString, rhsAsString);
            }
        }

        throw new RuntimeException("Unrecognized expression: " + expressionAsString);
    }

    /** Converts a String representing a boolean condition into a Condition object, based on the pattern detected. */
    private Condition parseCondition(String conditionAsString) {
        for (String operator: CONDITION_OPERATORS) {
            Matcher matcher = Pattern.compile(String.format("^(.+)%s(.+)$", operator)).matcher(conditionAsString);
            if (matcher.matches()) {
                return createCondition(operator, matcher.group(1).trim(), matcher.group(2).trim());
            }
        }

        throw new RuntimeException("Unrecognized condition: " + conditionAsString);
    }

    private static final Pattern CONSTANT_PATTERN = Pattern.compile("^[0-9]*$");
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]*$");
    private static final Pattern FUNCTION_CALL_PATTERN = Pattern.compile("^([A-Za-z][A-Za-z0-9_]*)\\(([^()]*)\\)$");
    private static final String[] ARITHMETIC_OPERATORS = new String[]{"+", "-", "*", "/"};
    private static final String[] CONDITION_OPERATORS = new String[]{"==", "<", ">"};
}
