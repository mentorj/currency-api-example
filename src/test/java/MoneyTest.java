
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
public class MoneyTest
{
    @Test
    void addingDollarsToDollars(){
        Money money= Money.of(200,"USD");
        Money money2 = Monetary.getAmountFactory(Money.class)
                .setCurrency("USD")
                .setAmount(
                        Money.of(200,Monetary.getCurrency(Locale.US))).create();

        Money added = money.add(money2);
        assertThat(added).isEqualTo(Money.of(400,"USD"));

    }


    @Test
    void javaxMoneyKnowTheDefaultRounding(){
        CurrencyUnit bhd = Monetary.getCurrency("BHD");
        Money money = Money.of(200.5879,bhd);
        assertThat(money.getCurrency().getDefaultFractionDigits()).isEqualTo(3);
    }


    @Test
    void exchangeProviderAreSimpleToUse(){
        ExchangeRateProvider ecbRateProvider = MonetaryConversions
                .getExchangeRateProvider("ECB");
        CurrencyUnit dollar = Monetary.getCurrency("USD");
        CurrencyUnit  euro = Monetary.getCurrency("EUR");
        Money moneyInEuros = Money.of(100,euro);
        CurrencyConversion ecbDollarConvertion = ecbRateProvider
                .getCurrencyConversion(dollar);

        var conversionFactor = ecbDollarConvertion.getExchangeRate(Money.of(1,"EUR")).getFactor();
        System.out.println("conversion factor for EURO to DOLLAR ="+ conversionFactor);
        Money convertedToUSD = moneyInEuros.with(ecbDollarConvertion);
        assertThat(convertedToUSD.getNumber().doubleValueExact()).isBetween(100d,120d);
    }
}
