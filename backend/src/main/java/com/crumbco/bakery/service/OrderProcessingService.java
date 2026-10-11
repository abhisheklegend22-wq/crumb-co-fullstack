package com.crumbco.bakery.service;

import com.crumbco.bakery.dto.OrderRequest;
import com.crumbco.bakery.model.CustomerOrder;

/** Contract allows the controller to depend on an abstraction, not a concrete implementation. */
public interface OrderProcessingService {
    CustomerOrder place(OrderRequest request);
}
