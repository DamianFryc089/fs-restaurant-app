import React, { useContext, useEffect, useState } from "react";
import type { Order } from "../types/Order";
import type { Review } from "../types/Review";
import { ReviewCard } from "../components/Cards/ReviewCard";
import { OrderCard } from "../components/Cards/OrderCard";
import { AuthContext } from "../context/AuthContext";
import { ReviewForm } from "../components/Forms/ReviewForm";
import { changeOrderStatus, deleteOrder, fetchOrders, fetchOrdersByClient, fetchOrdersByRestaurant } from "../api/orders";
import { createReview, deleteReview, fetchReviewByOrderId } from "../api/reviews";
import { ApiStatusDisplay, type ApiError } from "../components/ApiErrorDisplay";

export const OrdersPage: React.FC = () => {
	const { role, restaurantId, id } = useContext(AuthContext);
	const [orders, setOrders] = useState<Order[]>([]);
	const [review, setReview] = useState<Review | null>(null);
	const [selectedOrderId, setSelectedOrderId] = useState<number>();
	const [getOrdersError, setGetOrdersError] = useState<string | null>(null);
	const [createReviewSuccess, setCreateReviewSuccess] = useState<string | null>(null);
	const [createReviewError, setCreateReviewError] = useState<ApiError | null>(null);

	useEffect(() => {
		loadOrders();
	}, []);

	const loadOrders = async () => {

		try {
			if (role == "EMPLOYEE" && restaurantId) {
				const fetchedOrders = await fetchOrdersByRestaurant(restaurantId);
				setOrders(fetchedOrders);
			} else if (role == "CLIENT" && id) {
				const fetchedOrders = await fetchOrdersByClient(id);
				setOrders(fetchedOrders);
			} else if (role == "ADMIN") {
				const fetchedOrders = await fetchOrders();
				setOrders(fetchedOrders);
			}

		} catch (err: any) {
			setGetOrdersError(err?.message || "Failed to fetch restaurants");
		}
	};

	const handleOnOrderCancel = async (order: Partial<Order>) => {
		order.status = "CANCELED";
		await changeOrderStatus(order)
		await loadOrders();
	}

	const handleOnOrderComplete = async (order: Partial<Order>) => {
		order.status = "COMPLETED";
		await changeOrderStatus(order)
		await loadOrders();
	}

	const handleOnOrderReview = async (order: Partial<Order>) => {
		if (!order.id) return;
		try {
			const data = await fetchReviewByOrderId(order.id)
			setReview(data);
		}
		catch {
			if (order.clientId == id) {
				setReview(null);
				setSelectedOrderId(order.id);
			}
		}
	}

	const handleOnOrderDelete = async (order: Partial<Order>) => {
		await deleteOrder(order)
		await loadOrders();
	}

	const handleOnReviewDelete = async (review: Partial<Review>) => {
		await deleteReview(review)
		await handleOnOrderReview({id: review.orderId});
	}

	const handleReviewSubmit = async (review: Partial<Review>) => {
		setCreateReviewError(null);
		setCreateReviewSuccess(null);
		try {
			const createdReview = await createReview(review);
			setReview(createdReview);
			setCreateReviewSuccess("Review created successfully!");
		} catch (err: any) {
			setCreateReviewError(err);
		}
	};

	return (
		<div className="page">
			<div className="subpage">
				<h2>Orders</h2>
				{getOrdersError && <p style={{ color: "red" }}>{getOrdersError}</p>}
				{orders.map(o => (<OrderCard key={o.id} order={o} onCancel={handleOnOrderCancel} onComplete={handleOnOrderComplete} onDelete={handleOnOrderDelete} onReview={handleOnOrderReview} />))}
			</div>
			<div className="subpage">
				<h2>Review {orders.find(o => o.id === (review?.orderId || selectedOrderId))?.offerTitle || ""}</h2>
				{review && (<ReviewCard review={review} onDelete={handleOnReviewDelete}/>)}
				{!review && selectedOrderId && role == "CLIENT" && (<>
					<ReviewForm orderId={selectedOrderId} onSubmit={handleReviewSubmit} />
					<ApiStatusDisplay error={createReviewError} success={createReviewSuccess} />
				</>)}
				{!review && !selectedOrderId && (<p>Please select an offer to view its reviews.</p>)}
			</div>
		</div>
	);
};
