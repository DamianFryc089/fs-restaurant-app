import type { Offer } from "../types/Offer";
import { fetchWithAuth } from "./client";

export const createOffer = async (offer: Partial<Offer>,): Promise<Offer> => {
    return fetchWithAuth("/offers", {
        method: "POST",
        body: JSON.stringify(offer),
    });
};

export const fetchOffersByRestaurant = async (restaurantId: number): Promise<Offer[]> => {
    return fetchWithAuth(`/offers/restaurant/${restaurantId}`);
};

export const fetchOfferById = async (offerId: number): Promise<Offer> => {
    return fetchWithAuth(`/offers/${offerId}`);
};

export const fetchOffers = async (): Promise<Offer[]> => {
    return fetchWithAuth(`/offers`);
};

export const fetchOffersByCity = async (cityId: number): Promise<Offer[]> => {
    return fetchWithAuth(`/offers/city/${cityId}`);
};
