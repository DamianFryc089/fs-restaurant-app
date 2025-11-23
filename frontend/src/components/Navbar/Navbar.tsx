import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { NavItem } from "./NavItem";
import styles from "./Navbar.module.css";

export const Navbar: React.FC = () => {
  const { username, role } = useContext(AuthContext);

  return (
    <nav className={styles.navbar}>
      <div className={styles.links}>
        <NavItem label="Restaurants" to="/restaurants" />
        <NavItem label="Offers" to="/offers" />
        <NavItem label="Orders" to="/orders" allowedRoles={["CLIENT","EMPLOYEE","ADMIN"]}/>
        <NavItem label="Users" to="/users" allowedRoles={["ADMIN"]} />
        <NavItem label="Profile" to="/profile" allowedRoles={["CLIENT","EMPLOYEE","ADMIN"]} />
        <NavItem label="City" to="/cities" />
        <NavItem label="Login" to="/login" showForLoggedOut />
        <NavItem label="Logout" to="/logout" allowedRoles={["CLIENT","EMPLOYEE","ADMIN"]}/>
      </div>
      {username && (
        <div className={styles.user}>
          {username} - {role}
        </div>
      )}
    </nav>
  );
};
