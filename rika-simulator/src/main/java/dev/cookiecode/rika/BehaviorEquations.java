package dev.cookiecode.rika;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class BehaviorEquations {

    // Room temperature
    static final Expression TEMPERATURE_DECREASE_STOVE_INACTIVE_NIGHT_DEPERDITION = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
            .variables("x", "y")
            .build();

}
