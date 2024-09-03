FROM node:16.14.2 as builder
WORKDIR ./app
COPY package*.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=builder ./app/build .
COPY default.conf /etc/nginx/conf.d/
