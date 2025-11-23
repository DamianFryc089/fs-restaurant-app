import type { Review } from "../types/Review";
import { fetchWithAuth } from "./client";

export const createReview = async (review: Partial<Review>): Promise<Review> => {
    return fetchWithAuth("/reviews", {
        method: "POST",
        body: JSON.stringify(review),
    });
};

export const fetchReviews = async (): Promise<Review[]> => {
    return fetchWithAuth(`/reviews`);
};

export const fetchReviewsByOffer = async (offerId: number,): Promise<Review[]> => {
    return fetchWithAuth(`/reviews/offer/${offerId}`);
};

export const fetchReviewsByUser = async (userId: number,): Promise<Review[]> => {
    return fetchWithAuth(`/reviews/user/${userId}`);
};

export const fetchReviewsByOrderId = async (orderId: number,): Promise<Review> => {
    return fetchWithAuth(`/reviews/order/${orderId}`);
};