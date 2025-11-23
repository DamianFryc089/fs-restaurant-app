import React from "react";

export interface ApiError {
  message: string;
  errors?: string[];
}

interface ApiStatusDisplayProps {
  error: ApiError | null;
  success: string | null;
}

export const ApiStatusDisplay: React.FC<ApiStatusDisplayProps> = ({ error, success }) => {
  return (
    <div>
      {success && (
        <p style={{ color: "green" }}>
          {success}
        </p>
      )}

      {error && (
        <div style={{ color: "red" }}>
          <p>{error.message}</p>
          {error.errors && (
            <ul>
              {error.errors.map((err, i) => (
                <li key={i}>{err}</li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
};
