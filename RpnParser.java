package cs2110;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;

public class RpnParser {

    /**
     * Parse the RPN expression in `exprString` and return the corresponding expression tree. Tokens
     * must be separated by whitespace.  Valid tokens include decimal numbers (scientific notation
     * allowed), arithmetic operators (+, -, *, /, ^), function names (with the suffix "()"), and
     * variable names (anything else).  When a function name is encountered, the corresponding
     * function will be retrieved from `funcDefs` using the name (without "()" suffix) as the key.
     *
     * @throws IncompleteRpnException     if the expression has too few or too many operands
     *                                    relative to operators and functions.
     * @throws UndefinedFunctionException if a function name applied in `exprString` is not present
     *                                    in `funcDefs`.
     */
    public static Expression parse(String exprString, Map<String, UnaryFunction> funcDefs)
            throws IncompleteRpnException, UndefinedFunctionException {
        Deque<Expression> stack = new ArrayDeque<>();
        if (exprString.equals("")) {
            throw new IncompleteRpnException(exprString, 0);
        }

        // Loop over each token in the expression string from left to right
        for (Token token : Token.tokenizer(exprString)) {
            // TODO: Based on the dynamic type of the token, create the appropriate Expression node
            // and push it onto the stack, popping arguments as needed.
            // The "number" token is done for you as an example.
            if (token instanceof Token.Number) {
                Token.Number numToken = (Token.Number) token;
                stack.push(new Constant(numToken.doubleValue()));
            }

            if (token instanceof Token.Operator) {
                Token.Operator opToken = (Token.Operator) token;
                if (stack.peek() == null) {
                    throw new IncompleteRpnException(exprString, stack.size());
                }
                Expression right = stack.peek();
                stack.pop();
                if (stack.peek() == null) {
                    throw new IncompleteRpnException(exprString, stack.size());
                }
                Expression left = stack.peek();
                stack.pop();
                stack.push(new Operation(opToken.opValue(), left, right));
            }

            if (token instanceof Token.Function) {
                Token.Function funcToken = (Token.Function) token;
                if (stack.peek() == null) {
                    throw new IncompleteRpnException(exprString, stack.size());
                }
                Expression arg = stack.peek();
                stack.pop();
                if (funcDefs.get(funcToken.name()) == null) {
                    throw new UndefinedFunctionException(funcToken.name());
                }
                stack.push(new Application(funcDefs.get(funcToken.name()), arg));
            }

            if (token instanceof Token.Variable) {
                Token.Variable varToken = (Token.Variable) token;
                stack.push(new Variable(varToken.value()));
            }
        }

        // TODO: Return the overall expression node.  (This might also be a good place to check that
        // the string really did correspond to a single expression.)
            if (stack.size() != 1) {
                throw new IncompleteRpnException(exprString, stack.size());
            }
            return stack.pop();
    }
}
