FROM node:20-alpine as build
WORKDIR /app
COPY package*.json ./
run npm install
COPY . .
run npm run build

from nginx:stable-alpine
copy --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
expose 80