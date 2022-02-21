package guru.springframework.mssc.ssm.service;

import guru.springframework.mssc.ssm.domain.Payment;
import guru.springframework.mssc.ssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.config.StateMachineFactory;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PaymentServiceImplTest {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setup () {
        payment = Payment.builder().amount(new BigDecimal(44.99)).build();

    }
    @Transactional
    @Test
    void newPayment() {
        payment = paymentService.newPayment(payment);
        System.out.println(payment);
        paymentService.preAuth(payment.getId());
        payment = paymentRepository.getById(payment.getId());
        System.out.println(payment);
    }
    @Transactional
    @RepeatedTest(10)
    void preAuth() {
        payment = paymentService.newPayment(payment);
        System.out.println(payment);
        paymentService.preAuth(payment.getId());
        System.out.println(payment);
        payment = paymentRepository.getById(payment.getId());
        System.out.println(payment);
    }
    @Transactional
    @Disabled
    @RepeatedTest(10)
    void authorisePayment() {
        payment = paymentService.newPayment(payment);
        System.out.println(payment);
        paymentService.preAuth(payment.getId());
        System.out.println(payment);
        paymentService.authorisePayment(payment.getId());
        payment = paymentRepository.getById(payment.getId());
        System.out.println(payment);
    }
    @Transactional
    @Test
    void decline() {
        payment = paymentService.newPayment(payment);
        System.out.println(payment);
        paymentService.decline(payment.getId());
        payment = paymentRepository.getById(payment.getId());
        System.out.println(payment);
    }
}