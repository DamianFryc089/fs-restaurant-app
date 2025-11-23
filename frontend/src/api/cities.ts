import type { City } from "../types/City";
import { fetchWithAuth } from "./client";

export const createCity = async (city: Partial<City>): Promise<City> => {
    return fetchWithAuth("/cities", {
        method: "POST",
        body: JSON.stringify(city),
    });
};

export const fetchCities = async (): Promise<City[]> => {
    return fetchWithAuth("/cities");
}

export const setCityToUser = async (city: City, userId: number): Promise<City> => {
    return fetchWithAuth(`/users/setCity/${userId}`, {
        method: "PUT",
        body: JSON.stringify(city),
    });
}