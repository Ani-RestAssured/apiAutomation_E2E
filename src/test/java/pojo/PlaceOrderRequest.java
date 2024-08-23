package pojo;

import java.util.List;

public class PlaceOrderRequest {

    public List<PlaceOrderRequest_OrderDetails> getOrder() {
        return Order;
    }

    public void setOrder(List<PlaceOrderRequest_OrderDetails> order) {
        Order = order;
    }

    List<PlaceOrderRequest_OrderDetails> Order;


}
