package guru.springframework.mssc.ssm.service;

import guru.springframework.mssc.ssm.domain.Payment;
import guru.springframework.mssc.ssm.domain.PaymentEvent;
import guru.springframework.mssc.ssm.domain.PaymentState;
import guru.springframework.mssc.ssm.repository.PaymentRepository;
import guru.springframework.mssc.ssm.service.intercepter.PaymentStateChangeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

// TODO needs to be updated tou use reactive apis as the standard api is deprecated
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
    private final PaymentStateChangeInterceptor paymentStateChangeInterceptor;

    @Transactional
    @Override
    public Payment newPayment(Payment payment) {
       payment.setState(PaymentState.NEW);
       return paymentRepository.save(payment);
    }
    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = rebuildStateMachine(paymentId);
        sendEventMessage(paymentId,sm, PaymentEvent.PRE_AUTHORISE);
        return sm;
    }
    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> authorisePayment(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = rebuildStateMachine(paymentId);
        sendEventMessage(paymentId,sm, PaymentEvent.AUTHORISE);
        return sm;
    }
    @Transactional
    @Override
    public StateMachine<PaymentState, PaymentEvent> decline(Long paymentId) {
        StateMachine<PaymentState, PaymentEvent> sm = rebuildStateMachine(paymentId);
        sendEventMessage(paymentId,sm, PaymentEvent.AUTH_DECLINED);
        return sm;
    }

    private void sendEventMessage(Long PaymentId, StateMachine sm, PaymentEvent event ) {
        Message msg = MessageBuilder.withPayload(event).setHeader(PAYMENT_HEADER, PaymentId).build();
        sm.sendEvent(msg);
    }

    private StateMachine<PaymentState, PaymentEvent> rebuildStateMachine(Long paymentId) {
        Payment payment = paymentRepository.getById(paymentId);
        StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));
        sm.getStateMachineAccessor()
        .doWithAllRegions( sma -> {
                    sma.addStateMachineInterceptor(paymentStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<PaymentState,PaymentEvent>(payment.getState(),null,null,null));
                }
        );
        sm.start();
        return sm;
    }


}
