export type UserRole = "CLIENT" | "EMPLOYEE" | "ADMIN";
export type UserStatus = "ACTIVE" | "BLOCKED";

export interface User {
    id: number;
    username: string;
    email: string;
    role: UserRole;
    status: UserStatus;
    cityName: string;
    restaurantId: number | null;
}

export interface CreateUser {
    username: string;
    email: string;
    password: string;
}

export interface UserFilters {
  restaurantId?: number;
  role?: UserRole;
  status?: UserStatus;
  cityName?: string;
  username?: string;
  email?: string;
}