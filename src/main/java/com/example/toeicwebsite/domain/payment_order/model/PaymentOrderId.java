package com.example.toeicwebsite.domain.payment_order.model;

import java.util.UUID;

public class PaymentOrderId {
    private UUID id;

    public PaymentOrderId(UUID id) {
        this.id = id;
    }
    public UUID value() {
        return id;
    }
    public static PaymentOrderId newId(){ return new PaymentOrderId(UUID.randomUUID()); }
    @Override
    public boolean equals(Object o) {
        return o instanceof PaymentOrderId other && this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
