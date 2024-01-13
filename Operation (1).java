package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Operation implements Expression{
    private final Operator op;
    private Expression leftOperand;
    private Expression rightOperand;

    public Operation(Operator op, Expression leftOperand, Expression rightOperand) {
        this.op = op;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Returns the result of combining both evaluated operand children with its operator.
     * Throws UnboundVariableException if either operand cannot be evaluated
     * (variable is not defined in vars).
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * Returns one (operator applied to both operands)
     * plus the count of operations performed for both operands.
     */
    @Override
    public int opCount() {
        return 1 + leftOperand.opCount() + rightOperand.opCount();
    }

    /**
     * Returns an Operation enclosed in parentheses, and its operands
     * separated from its operator symbol by spaces.
     */
    @Override
    public String infixString() {
        return "(" + leftOperand.infixString() + " " + op.symbol() + " "
                + rightOperand.infixString() + ")";
    }

    /**
     * Returns a postorder traversal of the Operation.
     */
    @Override
    public String postfixString() {
        return leftOperand.postfixString() + " " + rightOperand.postfixString()
                + " " + op.symbol();
    }

    /**
     * Returns the optimization of Operation to a constant if leftOperand and rightOperand
     * can be evaluated to yield a number. if a child depends on an unbound variable,
     * return new copy where children are replaced with their optimized forms.
     */
    @Override
    public Expression optimize(VarTable vars) {
        Operation copy = this;
        try {
            Constant cons = new Constant(this.eval(vars));
            return cons.optimize(vars);
        }
        catch (UnboundVariableException e) {
            copy.leftOperand = this.leftOperand.optimize(vars);
            copy.rightOperand = this.rightOperand.optimize(vars);
            return copy;
        }
    }

    /**
     * Returns the union of leftOperand dependencies and rightOperand dependencies.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> dependencies = new HashSet<String>(leftOperand.dependencies());
        dependencies.addAll(rightOperand.dependencies());
        return dependencies;
    }

    /**
     * Return whether `other` is an Operation of the
     * same class with the same operator 'op', leftOperand, and rightOperand.
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        Operation c = (Operation) other;
        return c.op.equals(op) && c.leftOperand.equals(leftOperand) &&
                c.rightOperand.equals(rightOperand);
    }
}
