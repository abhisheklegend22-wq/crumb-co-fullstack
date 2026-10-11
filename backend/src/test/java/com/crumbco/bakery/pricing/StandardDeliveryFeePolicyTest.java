package com.crumbco.bakery.pricing;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardDeliveryFeePolicyTest {
    private final DeliveryFeePolicy policy = new StandardDeliveryFeePolicy();

    @Test void chargesStandardFeeBelowFreeDeliveryThreshold() {
        assertEquals(new BigDecimal("49.00"), policy.calculate(new BigDecimal("798.99")));
    }

    @Test void freeDeliveryStartsAt799() {
        assertEquals(new BigDecimal("0"), policy.calculate(new BigDecimal("799.00")));
    }

    @Test void emptyCartHasNoDeliveryFee() {
        assertEquals(new BigDecimal("0"), policy.calculate(BigDecimal.ZERO));
    }
}
