package com.example.toeicwebsite.domain.shared;

import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Value
public class Money {
    BigDecimal amount;
    String currency;

    public static Money vnd(long amount) {
        return new Money(BigDecimal.valueOf(amount), "VND");
    }

    public Money(BigDecimal amount, String currency) {
        if (amount == null) throw new BusinessRuleException("Amount cannot be null");

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public Money add(Money other) {
        checkSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        checkSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Money cannot be negative after subtraction");
        }
        return new Money(result, this.currency);
    }

    public boolean isGreaterThan(Money other) {
        checkSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isZeroOrLess() {
        return this.amount.compareTo(BigDecimal.ZERO) <= 0;
    }

    private void checkSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BusinessRuleException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    @Override
    public String toString() {
        return String.format("%,.0f %s", amount, currency);
    }
}