FROM node:20-alpine AS build
LABEL authors="Damian Fryc"
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]