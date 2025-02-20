package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StocksPortfolio  implements  IStockmarketService{
    public IStockmarketService stockmarket;
    public List<Stock> stocks;

    public StocksPortfolio(IStockmarketService service) {
        this.stockmarket = service;
        this.stocks=new ArrayList<>();
        

    }

 
    public void addStocks(Stock stock) {
        stocks.add(stock);
    }
    public double totalValue() {
        int price=0;
        for(Stock stock:stocks){
            int number =stock.getQuantity();
            price += ((stockmarket.lookUpPrice(stock.getLabel())) *number);
        }
        return price;
    }


    @Override
    public double lookUpPrice(String label) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'lookUpPrice'");
    }
    public List<Stock> mostValuableStocks(int topN) {
    return stocks.stream()
            .sorted(Comparator.comparingDouble(stock -> 
                    stockmarket.lookUpPrice(((Stock) stock).getLabel()) * ((Stock) stock).getQuantity()
            ).reversed()) // Ordena do maior para o menor
            .limit(topN) // Pega apenas os topN elementos
            .collect(Collectors.toList());
}
}