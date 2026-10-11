package com.crumbco.bakery.pricing;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Default implementation of the delivery-pricing strategy (polymorphism). */
@Component
public class StandardDeliveryFeePolicy implements DeliveryFeePolicy {
    private static final BigDecimal FREE_DELIVERY_LIMIT = new BigDecimal("799.00");
    private static final BigDecimal STANDARD_FEE = new BigDecimal("49.00");

    @Override
    public BigDecimal calculate(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() <= 0) return BigDecimal.ZERO;
        return subtotal.compareTo(FREE_DELIVERY_LIMIT) >= 0 ? BigDecimal.ZERO : STANDARD_FEE;
    }
}
