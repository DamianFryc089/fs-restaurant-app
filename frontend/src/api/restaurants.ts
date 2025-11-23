import type { Restaurant } from "../types/Restaurant";
import { fetchWithAuth } from "./client";

export const fetchRestaurants = async (): Promise<Restaurant[]> => {
    return fetchWithAuth("/restaurants");
};

export const createRestaurant = async (restaurant: Partial<Restaurant>): Promise<Restaurant> => {
    return fetchWithAuth("/restaurants", {
        method: "POST",
        body: JSON.stringify(restaurant),
    });
};

export const fetchRestaurantById = async (restaurantId: string): Promise<Restaurant> => {
    return fetchWithAuth(`/restaurants/${restaurantId}`);
};

export const fetchRestaurantsByCityId = async (cityId: number): Promise<Restaurant[]> => {
    return fetchWithAuth(`/restaurants/city/${cityId}`);
};