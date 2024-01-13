package cs2110;

import java.util.Set;
import java.util.HashSet;

public class Variable implements Expression{
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the Variable if it exists in the VarTable 'vars';
     * if it doesn't exist in 'vars', throw UnboundVariableException.
     */

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        return vars.get(name);
    }

    /**
     * No operations are required to evaluate a Variable's value.
     */
    @Override
    public int opCount() {
        return 0;
    }

    /**
     * Returns the String representation of this node's 'name'.
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Returns the String representation of this node's 'name'.
     */
    @Override
    public String postfixString() {
        return name;
    }

    /**
     * Returns the optimization of the variable as a constant
     * if it has an assigned value in the provided variable table.
     * otherwise, it optimizes to itself.
     */
    @Override
    public Expression optimize(VarTable vars) {
        try {
            vars.get(this.name);
            Constant cons = new Constant(vars.get(this.name));
            return cons.optimize(vars);
        }
        catch(UnboundVariableException e) {
            return this;
        }
    }

    /**
     * A Variable node depends on itself.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> dependencies = new HashSet<String>();
        dependencies.add(this.name);
        return dependencies;
    }

    /**
     * Return whether `other` is a Variable of the same class with the same 'name'.
     */
    public boolean equals (Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        Variable c = (Variable) other;
        return c.name.equals(name);
    }
}

