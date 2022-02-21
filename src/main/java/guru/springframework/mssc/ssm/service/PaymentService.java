package guru.springframework.mssc.ssm.service;

import guru.springframework.mssc.ssm.domain.Payment;
import guru.springframework.mssc.ssm.domain.PaymentEvent;
import guru.springframework.mssc.ssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {

    String PAYMENT_HEADER = "payment_id";

    Payment newPayment(Payment payment);

    StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> authorisePayment(Long paymentId);
    StateMachine<PaymentState, PaymentEvent> decline(Long paymentId);

}
