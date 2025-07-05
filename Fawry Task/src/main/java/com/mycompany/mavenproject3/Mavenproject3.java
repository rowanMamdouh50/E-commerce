/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject3;

import java.util.*;

/**
 *
 * @author HP
 */


interface Shippable {
    String getName();
    double getWeight();
}

class Products {
    private String name;
    private double price;
    private double quantity;
    public Products(String name ,double price ,double quantity ){
        this.name =name;
        this.price=price;
        this.quantity=quantity;
    }
    public String getName() {
        return name;
    }
    public  double getprice(){
        return price;
    }
    public  double getquantity(){
        return quantity;
    }
    public boolean isExpire(){
        return false;
    }

    public void quantityMinus(int taken){
        quantity -= taken;
    }


}





 class ExpiryDate extends Products{
    private Date expiryDate;

    public ExpiryDate(String name, double price, double quantity , Date prodDate) {
        super(name, price, quantity);

        }
    public boolean isExpired() {
        return (expiryDate.before(new Date()));
    }
}


class shipping extends Products {
        private double wight;
        public shipping(String name, double price, double quantity, double wight) {
            super(name, price, quantity);
            this.wight = wight;
        }

        public double getWight = wight;
    }
// Customer class
class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public boolean pay (double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        else{ return false;}
    }
    public double getBalance() {return balance;}
}


// Shopping cart item
class CartItem {
    Products product;
    int quantity;

    public CartItem(Products product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public double getTotalPrice() {
        return (product.getprice() * quantity);
    }
}

// Shipping service
  class ShippingCompany {
        public static void shipItems(List<Shippable> items) {
            System.out.println("Shipping the following items:");
            for (Shippable item : items) {
                System.out.println(item.getName() + "Wight: " + item.getWeight() + "kg");
            }
        }
    }


 class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addToCart(Products product, int quantity) {
     try {        if (quantity <= product.getquantity()) {
                  items.add(new CartItem(product, quantity));
                 }
      }catch (Exception e){
         System.out.println("Error"+e.getMessage());
     }
    }

     public void checkout(Customer myCustomer) {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        double subtotal = 0;
        double shippingFee = 0;
        List<Shippable> toShip = new ArrayList<>();

        for (CartItem item : items) {
            Products p = item.product;
            if (p.isExpire()) {
                throw new IllegalStateException("Product expired: " + p.getName());
            }
            if (p.getquantity() < item.quantity) {
                throw new IllegalStateException("Out of stock: " + p.getName());
            }
            subtotal += item.getTotalPrice();

            if (p instanceof Shippable) {
                Shippable s = (Shippable) p;
                shippingFee += s.getWeight() * 10;
                toShip.add(s);
            }
        }
        double total = subtotal + shippingFee;


        if (  !myCustomer.pay(total)) {
            throw new IllegalStateException("Not enough Money");
        }
        for (CartItem item : items) {
            item.product.quantityMinus(item.quantity);
        }
        System.out.println("\n Checkout Summary:");
        System.out.printf("Subtotal: " + subtotal +"\n");
        System.out.printf("Shipping Fee: "+ shippingFee+"\n");
        System.out.printf("Total Paid: ", total +"\n");
        System.out.printf("Remaining Balance: ", myCustomer.getBalance()+"\n");


        if (!toShip.isEmpty()) ShippingCompany.shipItems(toShip);
    }
}



// Main class to demo the system
public class Mavenproject3 {
    public static void main(String[] args) {
        Customer customer = new Customer("Rowan", 15000.00);
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.DECEMBER, 13);
        Date expiry = cal.getTime();

        Products cheese = new ExpiryDate("Cheese", 50.00, 5, expiry);
        Products tv = new shipping("TV", 300.00, 2, 20.0);
        Products scratchCard = new Products("Mobile Scratch Card", 20.00, 10);

        Cart cart = new Cart();

        // Add products to cart
        cart.addToCart(cheese, 2);
        cart.addToCart(tv, 1);
        cart.addToCart(scratchCard, 3);

        // Perform checkout
        cart.checkout(customer);
    }
}
