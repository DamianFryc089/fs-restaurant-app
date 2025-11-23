import { Link } from "react-router-dom";
import type { Order } from "../../types/Order";
import { AuthContext } from "../../context/AuthContext";
import { useContext } from "react";

interface Props {
  order: Order;
  onCancel: (order: Order) => void;
  onComplete: (order: Order) => void;
  onDelete: (order: Order) => void;
  onReview: (order: Order) => void;
}

export const OrderCard: React.FC<Props> = ({ order, onCancel, onComplete, onDelete, onReview }) => {
  const { role } = useContext(AuthContext);

  return (
    <div className={["card card-row", order.status.toLowerCase() || ""].join(" ")}>
      <div style={{ flex: 1 }}>
        <Link to={`/restaurants/${order.restaurantId}`}>{order.restaurantName}</Link> - <Link to={`/offers/${order.offerId}`}>{order.offerTitle}</Link>
        <p><strong>Quantity:</strong> {order.quantity}</p>
        <p><strong>Total:</strong> {order.totalPrice} zł</p>
        <p><strong>Status:</strong> {order.status}</p>
      </div>
      <div className="card-column">
        {role && order.status == "PENDING" && role != "ADMIN" &&  (
          <button onClick={() => onCancel(order)}>Cancel</button>
        )}
        {(role === "EMPLOYEE") && (order.status == "PENDING") && (
          <button onClick={() => onComplete(order)}>Complete</button>
        )}
        {role === "ADMIN" && (
          <button onClick={() => onDelete(order)}>Delete</button>
        )}
        {order.status == "COMPLETED" && (
          <button onClick={() => onReview(order)}>Review</button>
        )}
      </div>
    </div>
  );
};