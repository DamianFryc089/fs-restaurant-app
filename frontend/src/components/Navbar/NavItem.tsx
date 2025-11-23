import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import type { UserRole } from "../../types/User";

interface NavItemProps {
  label: string;
  to: string;
  allowedRoles?: UserRole[];
  showForLoggedOut?: boolean;
}

export const NavItem: React.FC<NavItemProps> = ({ label, to, allowedRoles, showForLoggedOut  }) => {
  const { role, username } = useContext(AuthContext);

  if (showForLoggedOut && username) return null;
  const canSee = !allowedRoles || (role && allowedRoles.includes(role as UserRole));
  if (!canSee) return null;

  return (
    <Link to={to}> {label} </Link>
  );
};
