package com.sainsburys;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


/**
 * Created by dmitri on 23/02/16.
 * WebPageConsumerTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class})
public class WebPageConsumerTest {

    @InjectMocks
    private WebPageConsumer webPageConsumer;

    @Mock
    private Connection connection;
    @Mock
    private Document productsInfo, productInfo;
    @Mock
    private Element link1, link2, pricePerUnit, description;

    private Elements links, pricing, descriptions;
    private String pageHtml;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Jsoup.class);
        webPageConsumer = new WebPageConsumer();
        links = new Elements();
        pricing = new Elements();
        descriptions = new Elements();
    }


    @Test
    public void test_grocery_products_successful_retrieval() throws IOException {

        mockGroceryProductsData();

        when(pricePerUnit.text()).thenReturn("£1.50/unit");
        pricing.add(pricePerUnit);
        when(productInfo.getElementsByClass(anyString())).thenReturn(pricing);

        pageHtml = "<html>some text</html>";
        when(productInfo.outerHtml()).thenReturn(pageHtml);

        when(description.text()).thenReturn("Apricots");
        descriptions.add(description);
        when(productInfo.select(anyString())).thenReturn(descriptions);

        String response = webPageConsumer.processProducts(links);

        JSONObject result = new JSONObject(response);
        JSONArray products = (JSONArray) result.get("results");

        JSONObject product1 = (JSONObject) products.get(0);
        assertTrue("Apricot Ripe & Ready x5".equals(product1.get("title")));

        JSONObject product2 = (JSONObject) products.get(1);
        assertTrue("Avocado Ripe & Ready XL Loose".equals(product2.get("title")));

        assertTrue(3.0 == result.getDouble("total"));
    }

    @Test
    public void test_no_grocery_products_on_web_page() throws IOException {

        PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
        when(connection.get()).thenReturn(productsInfo);
        when(productsInfo.select(anyString())).thenReturn(null);
        String result = webPageConsumer.consume("dummy url");

        assertNull(result);
    }

    @Test
    public void test_unit_price_missing_on_product_details_page() throws IOException {

        mockGroceryProductsData();

        when(pricePerUnit.text()).thenReturn("");
        pricing.add(pricePerUnit);
        when(productInfo.getElementsByClass(anyString())).thenReturn(pricing);

        pageHtml = "<html>some text</html>";
        when(productInfo.outerHtml()).thenReturn(pageHtml);

        when(description.text()).thenReturn("Apricots");
        descriptions.add(description);
        when(productInfo.select(anyString())).thenReturn(descriptions);

        String response = webPageConsumer.processProducts(links);

        JSONObject result = new JSONObject(response);
        JSONArray products = (JSONArray) result.get("results");

        JSONObject product1 = (JSONObject) products.get(0);
        assertFalse(product1.has("unit_price"));

        JSONObject product2 = (JSONObject) products.get(1);
        assertFalse(product2.has("unit_price"));

        assertTrue(0 == result.getDouble("total"));
    }

    private void mockGroceryProductsData() throws IOException {

        when(link1.text()).thenReturn("Apricot Ripe & Ready x5" );
        when(link1.absUrl(anyString())).thenReturn("http://Apricot_Ripe_Ready_x5.html");
        links.add(link1);
        when(link2.text()).thenReturn("Avocado Ripe & Ready XL Loose" );
        when(link2.absUrl(anyString())).thenReturn("http://Avocado_Ripe_Ready_XL_Loose.html");//
        links.add(link2);

        PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
        when(connection.get()).thenReturn(productInfo);
    }

    @Test
    public void test_io_exception_thrown() throws IOException {

        PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
        doThrow(IOException.class).when(connection).get();

        assertNull(webPageConsumer.consume("http://invalid"));

    }

    @After
    public void destroy(){
        connection = null;
    }
}
