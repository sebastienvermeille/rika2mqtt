package dev.cookiecode.rika;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.mockserver.integration.ClientAndServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class RikaStoveSimulator {

    private ClientAndServer mockServer;

    public void start(){
        System.out.println("Starting Rika Stove Simulator");
        mockServer = startClientAndServer(1080);

        var results = generateNextTemperatures(StoveBehavior.INACTIVE, 16.8);

        System.out.println(results);
    }

    private List<Double> generateNextTemperatures(StoveBehavior stoveBehavior, double currentTemperature){
        switch (stoveBehavior) {
            case INACTIVE -> {
                return generateValues(currentTemperature, BehaviorEquations.TEMPERATURE_DECREASE_STOVE_INACTIVE_NIGHT_DEPERDITION);
            }
        }
        return Collections.emptyList();
    }

    private List<Double> generateValues(double currentTemperature, Expression expression) {
        List<Double> results = new ArrayList<>();
        for(var i=0; i<180;i++){
            expression.setVariable("x", i);
            expression.setVariable("y", 1);
            results.add(expression.evaluate());
        }
        return results;
    }

    public void stop(){
        mockServer.stop();
    }
}
