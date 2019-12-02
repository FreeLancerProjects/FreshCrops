package com.appzone.freshcrops.singletone;

import com.appzone.freshcrops.models.AlternativeProductItem;
import com.appzone.freshcrops.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsSingleTone {

    private static OrderItemsSingleTone instance = null;
    private List<OrderItem> orderItemList = new ArrayList<>();

    private OrderItemsSingleTone(){}

    public static synchronized OrderItemsSingleTone newInstance()
    {
        if (instance == null)
        {
            instance = new OrderItemsSingleTone();
        }
        return instance;
    }

    public void AddProduct(OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);
        if (pos !=-1)
        {
            OrderItem item = orderItemList.get(pos);
            int product_new_quantity = orderItem.getProduct_quantity() + item.getProduct_quantity();
            item.setProduct_quantity(product_new_quantity);
            double product_total_price = item.getProduct_price() * product_new_quantity;
            item.setProduct_total_price(product_total_price);
            orderItemList.set(pos,item);

        }else
        {

            orderItemList.add(orderItem);

        }
    }

    public void UpdateProduct(OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);

        AlternativeProductItem alternativeProductItem = orderItem.getAlternativeProductItem();
        if (alternativeProductItem!=null)
        {
            alternativeProductItem.setProduct_quantity(orderItem.getProduct_quantity());
            orderItem.setAlternativeProductItem(alternativeProductItem);
        }

        orderItemList.set(pos,orderItem);
    }

    public void RemoveProduct (OrderItem orderItem)
    {
        int pos = getItemPosition(orderItem);
        orderItemList.remove(pos);
        if (orderItemList.size()==0)
        {
            ClearCart();
        }
    }

    private int getItemPosition(OrderItem orderItem)
    {
        int pos = -1;

        for (int i = 0 ; i< orderItemList.size() ; i++)
        {

            OrderItem item = orderItemList.get(i);

            if (item.getProduct_id()==orderItem.getProduct_id() && orderItem.getProduct_price_id()==item.getProduct_price_id())
            {

                pos = i;
                break;
            }
        }

        return pos;
    }

    public void AddListOrderItems(List<OrderItem> orderItemList)
    {
        this.orderItemList.clear();
        this.orderItemList.addAll(orderItemList);
    }
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public int getItemsCount ()
    {
        return orderItemList.size();
    }

    public void ClearCart()
    {
        orderItemList.clear();
    }

}
