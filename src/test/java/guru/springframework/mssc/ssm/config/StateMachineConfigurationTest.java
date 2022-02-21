package guru.springframework.mssc.ssm.config;

import guru.springframework.mssc.ssm.domain.PaymentEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StateMachineConfigurationTest {

    @Autowired
    private StateMachineFactory factory;

    @Test
    public void stateMachineTransitionTest() {

        StateMachine sm = factory.getStateMachine();

        sm.start();

        System.out.println(sm.getState());
        sm.sendEvent(PaymentEvent.PRE_AUTHORISE);
        System.out.println(sm.getState());
        sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);
        System.out.println(sm.getState());

    }


}