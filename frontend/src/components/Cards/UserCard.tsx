import React from "react";
import type { User } from "../../types/User";

interface Props {
  user: User;
  onBlockUser: (user: User) => void;
  onActivateUser: (user: User) => void;
  onSelectUser: (user: User) => void;
}



export const UserCard: React.FC<Props> = ({ user, onBlockUser, onActivateUser, onSelectUser }) => (
  <div className={["card card-row", user.status == "BLOCKED" ? "cancelled" : ""].join(" ")}>
    <div style={{ flex: 1 }}>
      <p>{user.id} {user.role} {user.username} {user.email} {(user.restaurantId && (`(${user.restaurantId})`))} {(user.cityName && (`- ${user.cityName}`))}</p>
    </div>
    <div>
      <button onClick={() => onSelectUser(user)}>Select</button>
      {user.status == "ACTIVE" && user.role != "ADMIN" && <button onClick={() => onBlockUser(user)}>Block</button>}
      {user.status == "BLOCKED" &&  user.role != "ADMIN" && <button onClick={() => onActivateUser(user)}>Activate</button>}
    </div>
  </div>
);