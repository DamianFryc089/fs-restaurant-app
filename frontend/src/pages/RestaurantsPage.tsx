import React, { useEffect, useState, useContext } from "react";
import type { Restaurant } from "../types/Restaurant";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { RestaurantCard } from "../components/Cards/RestaurantCard";
import { RestaurantForm } from "../components/Forms/RestaurantForm";
import type { City } from "../types/City";
import { createRestaurant, fetchRestaurantById, fetchRestaurants, fetchRestaurantsByCityId } from "../api/restaurants";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";

export const RestaurantsPage: React.FC = () => {
	const { role } = useContext(AuthContext);

	const location = useLocation();
	const stateRestaurant = location.state?.restaurant as Restaurant | undefined;
	const stateCity = location.state?.city as City | undefined;
	const { restaurantId } = useParams<{ restaurantId: string }>();

	const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
	const [createRestaurantSuccess, setCreateRestaurantSuccess] = useState<string | null>(null);
	const [createRestaurantError, setCreateRestaurantError] = useState<ApiError | null>(null);

	const navigate = useNavigate();

	useEffect(() => {
		loadRestaurants();
	}, []);

	const loadRestaurants = async () => {
		try {
			if (stateRestaurant) {
				setRestaurants([stateRestaurant]);
			} else if (restaurantId) {
				const data = await fetchRestaurantById(restaurantId);
				setRestaurants([data]);
			} else if (stateCity) {
				const data = await fetchRestaurantsByCityId(stateCity.id);
				setRestaurants(data);
			} else {
				const data = await fetchRestaurants();
				setRestaurants(data);
			}
		} catch (err: any) {}
	};

	const handleViewOffers = (restaurant: Restaurant) => {
		navigate(`/offers/restaurant/${restaurant.id}`, {
			state: { restaurant }
		});
	};

	const handleViewEmployees = (restaurant: Restaurant) => {
		navigate(`/users/restaurant/${restaurant.id}`, {
			state: { restaurant }
		});
	};


	const handleRestaurantSubmit = async (data: any) => {
		setCreateRestaurantError(null);
		setCreateRestaurantSuccess(null);
		try {
			await createRestaurant(data);
			setCreateRestaurantSuccess("Restaurant created successfully!");
			loadRestaurants();
		} catch (err: any) {
			setCreateRestaurantError(err);
		}
	};

	return (
		<div className="page">
			<div className="subpage">
				{restaurantId ? <h2>Restaurant Details</h2> : <h2>Restaurants</h2>}
				{restaurants.map(r => (
					<RestaurantCard key={r.id} restaurant={r} onViewOffers={handleViewOffers} onViewEmployees={handleViewEmployees} />
				))}
			</div>
			{role == "ADMIN" && (
				<div className="subpage">
					<h2> Create Restaurant </ h2>
					<RestaurantForm onSubmit={handleRestaurantSubmit} />
					<ApiStatusDisplay error={createRestaurantError} success={createRestaurantSuccess} />
				</div>)}
		</div>
	);
};
