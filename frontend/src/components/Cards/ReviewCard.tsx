import { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import type { Review } from "../../types/Review";

interface Props {
    review: Review;
    onDelete: (review: Review) => void;
}

export const ReviewCard: React.FC<Props> = ({ review, onDelete }) => {
    const { role } = useContext(AuthContext);

    return (
        <div className="card">
            <p><strong>User:</strong> {review.username}</p>
            <p><strong>Comment:</strong> {review.comment}</p>
            <p><strong>Rating:</strong> {review.rating}</p>
            {role === "ADMIN" && (
                <button onClick={() => onDelete(review)}>Delete</button>
            )}
        </div>
    );
};
