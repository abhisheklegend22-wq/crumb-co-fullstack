package com.crumbco.bakery.pricing;

import java.math.BigDecimal;

/** Strategy interface: alternate delivery pricing rules can be substituted without changing order logic. */
public interface DeliveryFeePolicy {
    BigDecimal calculate(BigDecimal subtotal);
}
