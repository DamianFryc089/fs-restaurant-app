export interface Offer {
  id: number;
  title: string;
  description: string;
  restaurantId: number;
  restaurantName: string;
  price: number;
  availableQuantity: number;
  status: string;
  rating: number;
}