export type OfferStatus = "PENDING" | "CANCELED" | "COMPLETED";

export interface Order {
    id: number;
    clientId: number;
    restaurantId: number;
    restaurantName: string;
    status: OfferStatus;
    totalPrice: number;
    createdAt: string;
    updatedAt: string;
    offerId: number;
    offerTitle: string;
    quantity: number;
    reviewId: number | null;
}