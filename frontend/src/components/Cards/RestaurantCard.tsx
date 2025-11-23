import React, { useContext } from "react";
import type { Restaurant } from "../../types/Restaurant";
import { AuthContext } from "../../context/AuthContext";

interface Props {
    restaurant: Restaurant;
    onViewOffers: (restaurant: Restaurant) => void;
    onViewEmployees: (restaurant: Restaurant) => void;
}

export const RestaurantCard: React.FC<Props> = ({ restaurant, onViewOffers, onViewEmployees }) => {
    const { role } = useContext(AuthContext);

    return (
        <div className="card card-row">
            <div style={{ flex: 1 }}>
                <h3>{role == "ADMIN" && (<>{restaurant.id} -</>)} {restaurant.name}</h3>
                <p><strong>City:</strong> {restaurant.cityName}</p>
                <p><strong>Street:</strong> {restaurant.street}</p>
                <p><strong>Phone:</strong> {restaurant.phone}</p>
            </div>
            <div className="card-column">
                <button onClick={() => onViewOffers(restaurant)}>View Offers</button>
                {role == "ADMIN" && <button onClick={() => onViewEmployees(restaurant)}>View Employees</button>}
            </div>
        </div>
    );
};