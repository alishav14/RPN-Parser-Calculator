package cs2110;

import java.util.Set;

public class Application implements Expression{
    private final UnaryFunction func;
    private Expression argument;

    public Application(UnaryFunction func, Expression argument) {
        this.func = func;
        this.argument = argument;
    }

    /**
     * Returns a double value of the function 'func'
     * applied on the evaluated argument based on VarTable 'vars'. Throws UnboundVariableException
     * if argument cannot be evaluated (variable is not defined in vars).
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        return func.apply(argument.eval(vars));
    }

    /**
     * Returns one (the function applied to argument)
     * plus the count of operations performed for the argument.
     */
    @Override
    public int opCount() {
        return 1 + argument.opCount();
    }

    /**
     * Returns the String representation of the function 'func' name
     * followed by the infix representation of its argument, enclosed in parentheses.
     */
    @Override
    public String infixString() {
        return func.name() + "(" + argument.infixString() + ")";
    }

    /**
     *  Returns an argument’s postfix string, followed by the function name with a "()" suffix.
     */

    @Override
    public String postfixString() {
        return argument.postfixString() + " " + func.name() + "()";
    }

    /**
     * Returns the optimization of Application to a constant if argument
     * can be evaluated to yield a number. if argument depends on an unbound variable,
     * return new copy where argument is replaced with its optimized form.
     */
    @Override
    public Expression optimize(VarTable vars) {
        try {
            Constant cons = new Constant(this.eval(vars));
            return cons.optimize(vars);
        }
        catch (UnboundVariableException e) {
            Application copy = this;
            copy.argument = this.argument.optimize(vars);
            return copy;
        }
    }

    /**
     * Returns the argument's dependencies as it is the only child.
     */
    @Override
    public Set<String> dependencies() {
        return argument.dependencies();
    }

    /**
     * Return whether `other` is an Application of the
     * same class with the same function 'func' and argument.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        Application c = (Application) other;
        return c.func.equals(func) && c.argument.equals(argument);
    }
}
