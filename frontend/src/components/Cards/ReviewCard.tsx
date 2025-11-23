import type { Review } from "../../types/Review";

interface ReviewCardProps {
    review: Review;
}


export const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => (
    <div className="card">
        <p>
            <strong>User:</strong> {review.username}
        </p>
        <p>
            <strong>Comment:</strong> {review.comment}
        </p>
        <p>
            <strong>Rating:</strong> {review.rating}
        </p>
    </div>
);