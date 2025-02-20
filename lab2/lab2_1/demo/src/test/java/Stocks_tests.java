import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.IStockmarketService;
import com.example.Stock;
import com.example.StocksPortfolio;




@ExtendWith(MockitoExtension.class)
class Stocks_tests {


    @Test
    void whenGetTotal_thenSumWithMockedMarket(){
        IStockmarketService market = Mockito.mock(IStockmarketService.class);
        StocksPortfolio portfolio= new StocksPortfolio(market);

        Mockito.when(market.lookUpPrice("EBAY")).thenReturn(4.0);
        Mockito.when(market.lookUpPrice("MSFT")).thenReturn(1.5);

        portfolio.addStocks(new Stock("EBAY",2));
        portfolio.addStocks(new Stock("MSFT",4));
        Double result =portfolio.totalValue();

        assertEquals(14.0, result);
        assertThat(result, is(14.0));
        Mockito.verify(market,Mockito.times(2)).lookUpPrice(Mockito.anyString());
    }

    
    @Test
    void assertWithHamcrestMatcher() {
        assertThat(2 + 1, equalTo(3));
        assertThat("Foo", notNullValue());
        assertThat("Hello world", containsString("world"));
    }
    @Test 
    void Test_mostValuableStocks(){
        IStockmarketService market = Mockito.mock(IStockmarketService.class);
        StocksPortfolio portfolio= new StocksPortfolio(market);

        Mockito.when(market.lookUpPrice("EBAY")).thenReturn(4.0);
        Mockito.when(market.lookUpPrice("MSFT")).thenReturn(1.5);
        Mockito.when(market.lookUpPrice("APLL")).thenReturn(8.0);
        Mockito.when(market.lookUpPrice("TESL")).thenReturn(2.0);
        Mockito.when(market.lookUpPrice("AMDS")).thenReturn(10.0);

        portfolio.addStocks(new Stock("EBAY",2));
        portfolio.addStocks(new Stock("MSFT",4));
        portfolio.addStocks(new Stock("APLL",5));
        portfolio.addStocks(new Stock("TESL",10));
        portfolio.addStocks(new Stock("AMDS",10));
        Double result =portfolio.totalValue();

        List<Stock> topStocks = portfolio.mostValuableStocks(3);
        assertEquals(3, topStocks.size());
        assertEquals("AMDS", topStocks.get(0).getLabel()); 
        assertEquals("APLL", topStocks.get(1).getLabel());
        assertEquals("TESL", topStocks.get(2).getLabel()); 
        
        assertEquals(174.0 ,result);
        assertThat(result, is(174.0));
       

    }
}
