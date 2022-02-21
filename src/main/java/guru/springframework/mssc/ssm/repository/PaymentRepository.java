package guru.springframework.mssc.ssm.repository;

import guru.springframework.mssc.ssm.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
