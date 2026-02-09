# Food Order Management System

## Project Information
This is an academic project developed for university classes. The primary goal was to design and implement a secure, scalable system for restaurant order management with a focus on relational database integration and server-side logic.

## Tech Stack
| Layer        | Technologies                                       |
|--------------|----------------------------------------------------|
| **Backend**  | Java 21, Spring Boot 3, Spring Data JPA, Hibernate |
| **Security** | Spring Security, JWT, HttpOnly Cookies             |
| **Database** | PostgreSQL (Prod), H2 (Test)                       |
| **Frontend** | React.js, TypeScript                               |
| **DevOps**   | Docker, Docker Compose                             |
| **Testing**  | JUnit 5, Mockito                                   |

## Key Features
- **Role-Based Access Control (RBAC)**: Implementation of access levels for Admin, Employee, and Client roles.
- **Automated DB Schema**: Synchronization between Java entities and PostgreSQL tables using Hibernate `ddl-auto`.
- **Secure Authentication**: Stateless JWT implementation with protection against XSS via HttpOnly cookies.
- **Data Validation**: Strict server-side validation using Jakarta Validation and Global Exception Handling.
- **Testing Layer**: Suite of unit and integration tests for the backend service layer and API endpoints.

## Deployment

### Prerequisites
- Docker and Docker Compose are required. If not installed, download: [Docker Desktop](https://docs.docker.com/get-started/get-docker/)

### Setup
1. **Clone the repository:**
   ```bash
   git clone https://github.com/DamianFryc089/fs-restaurant-app
   ```

2. **Environment Configuration:** 
Rename the `.env.example` file to `.env` in the root directory:
   ```bash
   cp .env.example .env
   ```

3. **Launch the system:** 
Execute the following command to build and start all services:
   ```bash
   docker-compose up --build
   ```

<small> **Note:** The very first account registered in the application is automatically assigned the **ADMIN** role. Every subsequent registration will default to the **CLIENT** role. </small>

### Verification
Once the containers are running, you can access the system at:
| Component    | URL                     | Description         |
|--------------|-------------------------|---------------------|
| **Frontend** | `http://localhost:5173` | Main User Interface |
| **API**      | `http://localhost:8080` | Backend API         |
