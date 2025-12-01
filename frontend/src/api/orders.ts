import type { Order } from "../types/Order";
import { fetchWithAuth } from "./client";

export const createOrder = async (order: Partial<Order>,): Promise<Order> => {
    return fetchWithAuth("/orders", {
        method: "POST",
        body: JSON.stringify(order),
    });
};

export const fetchOrders = async (): Promise<Order[]> => {
    return fetchWithAuth(`/orders`);
};

export const fetchOrdersByClient = async (clientId: number): Promise<Order[]> => {
    return fetchWithAuth(`/orders/client/${clientId}`);
};

export const fetchOrdersByRestaurant = async (restaurantId: number): Promise<Order[]> => {
    return fetchWithAuth(`/orders/restaurant/${restaurantId}`);
};

export const changeOrderStatus = async (order: Partial<Order>): Promise<Order[]> => {
    return fetchWithAuth(`/orders/${order.id}/${order.status?.toLowerCase()}`, {
        method: "PUT"
    });
};

export const deleteOrder = async (order: Partial<Order>): Promise<Order[]> => {
    return fetchWithAuth(`/orders/${order.id}`, {
        method: "DELETE"
    });
};