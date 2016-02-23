package com.sainsburys;


/**
 * Created by dmitri on 22/02/16.
 * WebPageConsumerRunner
 */
public class WebPageConsumerRunner {


    public static void main(String [] args) {

        WebPageConsumer webPageConsumer = new WebPageConsumer();
        String productsJson = webPageConsumer.consume("http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html");
        System.out.print(String.format("Products Data %s", productsJson));
    }
}
