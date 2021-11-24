package com.javatpoint;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController 
{
@Autowired
private IProductService productService;
//mapping the getProduct() method to /product
@GetMapping(value = "/product")
public List<Product> getProduct() 
{
//finds all the products
List<Product> products = productService.findAll();
//returns the product list
return products;
}
  
  @PostMapping(path = "/save-cust-info")
    public String customerInformation(@RequestBody Object cust) {

        /* You can write your DAO logic here.
         * For time being I am printing the customer data just to show the POST call is working.
         */

        return "Customer information saved successfully ::." + cust.toString();
    }
}
