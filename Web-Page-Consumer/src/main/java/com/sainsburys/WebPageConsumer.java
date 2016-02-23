package com.sainsburys;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by dmitri on 22/02/16.
 * WebPageConsumer
 */
public class WebPageConsumer {

    private BigDecimal totalUnitPrice = new BigDecimal(0);
    /**
     * Consume web page and process products data
     * @param url endpoint of the web page
     * @return Json String of products
     */
    public String consume(String url) {

        try {
            Document productsInfo = Jsoup.connect(url).get();
            Elements links = productsInfo.select(".productInfo > h3 > a[href]");

            if(links != null && links.size() > 0) {

                System.out.println(String.format("There are %d grocery products on the page.", links.size()));

                return processProducts(links);
            }
        } catch (IOException e) {
            System.err.println(String.format("Could not connect to %s - reason %s", url, e));
        }
        return null;
    }

    /**
     * Process Products links
     * @param links product links on the page
     * @return formatted JSON
     * @throws IOException is thrown in case of invalid URL
     */
    public String processProducts(Elements links) throws IOException {

        JSONObject resultsObject = new JSONObject();
        JSONArray productsArray = new JSONArray();

        System.out.println("Scraping product info from a grocery web page...");

        for(Element link : links) {
            String title = link.text();
            String href = link.absUrl("href");

            System.out.println(String.format("Trying to follow a link %s", href));

            Document productInfo = Jsoup.connect(href).get();

            if(productInfo != null) {

                JSONObject productObject = new JSONObject();

                float pageSize = productInfo.outerHtml().length();

                processPricingElement(productInfo, productObject);
                processDescriptionElement(productInfo, productObject);

                productObject.put("size", pageSize / 1000 + "kb");
                productObject.put("title", title);

                productsArray.put(productObject);
            }
        }

        resultsObject.put("results", productsArray);
        resultsObject.put("total", totalUnitPrice.setScale(2, BigDecimal.ROUND_DOWN));

        System.out.println("Added product info from a grocery web page to JSON Object.");

        return resultsObject.toString();
    }

    /**
     * Process pricing element
     * @param productInfo source of product info
     * @param productObject json object containing product info
     */
    private void processPricingElement(Document productInfo, JSONObject productObject) {

        Element pricingElement = productInfo.getElementsByClass("pricePerUnit").first();

        if(pricingElement != null) {
            String pricePerUnit = pricingElement.text();

            if(pricePerUnit.contains("£") && pricePerUnit.contains("/")) {
                String productUnitPrice= pricePerUnit.substring(1, pricePerUnit.indexOf("/"));
                BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(productUnitPrice));
                totalUnitPrice = totalUnitPrice.add(bigDecimal);
                productObject.put("unit_price", productUnitPrice);
            }
        }
    }

    /**
     * Process description element
     * @param productInfo source of product info
     * @param productObject json object containing product info
     */
    private void processDescriptionElement(Document productInfo, JSONObject productObject) {

        Element descriptionElement = productInfo.select("productcontent > htmlcontent > .productText > p").first();

        if(descriptionElement != null){
            String description = descriptionElement.text();
            productObject.put("description", description);
        }
    }
}


