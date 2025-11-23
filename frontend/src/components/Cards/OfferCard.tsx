import React, { useContext } from "react";
import type { Offer } from "../../types/Offer";
import { Link } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";


interface OfferProps {
    offer: Offer;
    quantity: number;
    onQuantityChange: (offerId: number, value: string) => void;
    onCreateOrder: (offer: Offer) => void;
    onViewReviews: (offer: Offer) => void;
    showRestarant?: boolean;
}


export const OfferCard: React.FC<OfferProps> = ({ offer, quantity, onQuantityChange, onCreateOrder, onViewReviews, showRestarant = true }) => {
    const { role } = useContext(AuthContext);
    return (
        <div className={["card card-row", offer.availableQuantity <= 0 ? "cancelled" : ""].join(" ")}>
            <div style={{ flex: 1 }}>
                <p>{showRestarant && <Link to={`/restaurants/${offer.restaurantId}`}>{offer.restaurantName} - </Link>}{offer.title}</p>
                <p><strong>Description:</strong> {offer.description}</p>
                <p><strong>Price:</strong> {offer.price.toFixed(2)} zł</p>
                <p><strong>Available:</strong> {offer.availableQuantity}</p>
                <p><strong>Rating:</strong> {offer.rating}</p>
            </div>
            <div>
                {role == "CLIENT" && (<>
                    <input type="number" min={1} max={offer.availableQuantity} value={quantity} onChange={e => onQuantityChange(offer.id, e.target.value)} />
                    <button onClick={() => onCreateOrder(offer)}>Order</button>
                </>)}
                <button onClick={() => onViewReviews(offer)}>Reviews</button>
            </div>
        </div>)
};