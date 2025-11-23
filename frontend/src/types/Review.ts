export interface Review {
    id: number;
    orderId: number;
    username: string;
    rating: number;
    comment: string;
    createdAt: number;
    anonymous: boolean;
}