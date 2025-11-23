import React, { useContext, useEffect, useState } from "react";
import type { Offer } from "../types/Offer";
import type { Review } from "../types/Review";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { OfferCard } from "../components/Cards/OfferCard";
import { ReviewCard } from "../components/Cards/ReviewCard";
import { OfferForm } from "../components/Forms/OfferForm";
import type { Order } from "../types/Order";
import { createOffer, fetchOfferById, fetchOffers, fetchOffersByCity, fetchOffersByRestaurant } from "../api/offers";
import { fetchReviewsByOffer } from "../api/reviews";
import { createOrder } from "../api/orders";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";

export const OffersPage: React.FC = () => {
	const location = useLocation();
	const { id, role, restaurantId } = useContext(AuthContext);
	const { offerId } = useParams<{ offerId: string }>();

	const restaurant = location.state?.restaurant ?? null;
	const city = location.state?.city ?? null;

	const [offers, setOffers] = useState<Offer[]>([]);
	const [reviews, setReviews] = useState<Review[]>([]);
	const [reviewlabel, setReviewLabel] = useState<string>("Reviews");
	const [quantities, setQuantities] = useState<{ [offerId: number]: number }>({});
	const [createOfferError, setCreateOfferError] = useState<ApiError | null>(null);
	const [createOfferSuccess, setCreateOfferSuccess] = useState<string | null>(null);

	useEffect(() => {
		loadOffers();
	}, [restaurant, city]);

	const loadOffers = async () => {
		try {
			let data: Offer[] = [];
			if (restaurant) data = await fetchOffersByRestaurant(restaurant.id);
			else if (city) data = await fetchOffersByCity(city.id);
			else if (offerId) data = [await fetchOfferById(Number(offerId))];
			else data = await fetchOffers();

			setOffers(data);
			const initialQuantities: { [id: number]: number } = {};
			data.forEach(o => { if (o.id) initialQuantities[o.id] = 1 });
			setQuantities(initialQuantities);
		} catch (err: any) {}
	};

	const handleViewReviews = async (offer: Offer) => {
		try {
			const all = await fetchReviewsByOffer(offer.id);
			setReviews(all);
			setReviewLabel(`Reviews for offer - ${offer.title}`);
		} catch (err: any) {
			console.error(err);
		}
	};


	const handleQuantityChange = (offerId: number, value: string) => {
		setQuantities(prev => ({
			...prev,
			[offerId]: Number(value),
		}));
	};

	const handleCreateOrder = async (offer: Offer) => {
		const quantity = quantities[offer.id];
		if (quantity > offer.availableQuantity) {
			alert(`Cannot order more than available quantity (${offer.availableQuantity})`);
			return;
		}

		if (quantity <= 0) {
			alert("Quantity must be at least 1");
			return;
		}

		if (!id) return;

		const currentOrder: Partial<Order> = {
			offerId: offer.id,
			quantity: quantity,
			clientId: id,
		};

		console.log(currentOrder);

		try {
			await createOrder(currentOrder);
			alert("Order created!");
			await loadOffers();
		} catch (err: any) {
			alert(err?.message || "Failed to create order");
		}
	};

	const handleCreateOffer = async (offer: Partial<Offer>) => {
		setCreateOfferError(null);
		setCreateOfferSuccess(null);
		try {
			await createOffer(offer);
			setCreateOfferSuccess("Offer created successfully!");
			loadOffers();
		} catch (err: any) {
			setCreateOfferError(err);
		}
	};

	return (
		<div className="page">

			<div className="subpage">
				<h2>
					{restaurant && `Offers for restaurant - ${restaurant.name || city.id}`}
					{city && `Offers for city - ${city.name || city.id}`}
					{!restaurant && !city && "All Offers"}
				</h2>
				{offers.length === 0 && "No Offers Available"}
				{offers.map(o => (
					<OfferCard key={o.id} offer={o} quantity={quantities[o.id] ?? 1} onQuantityChange={handleQuantityChange} onCreateOrder={handleCreateOrder} onViewReviews={handleViewReviews} showRestarant={!restaurant} />
				))}
			</div>

			{role == "EMPLOYEE" && restaurantId && (
				<div className="subpage">
					<h2>Create Offer</h2>
					<OfferForm restaurantId={restaurantId} onSubmit={handleCreateOffer} />
					<ApiStatusDisplay error={createOfferError} success={createOfferSuccess} />
				</div>
			)}

			<div className="subpage">
				<h2>{reviewlabel}</h2>
				{reviewlabel !== "Reviews" && reviews.length === 0 && <p>No reviews available.</p>}
				{reviewlabel == "Reviews" && reviews.length === 0 && <p>Please select an offer to view its reviews.</p>}
				{reviews.map(r => (<ReviewCard key={r.id} review={r} />))}
			</div>
		</div>
	);
};
