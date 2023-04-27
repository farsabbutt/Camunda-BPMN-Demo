FROM node:16.16.0-buster

# Create and define the node_modules's cache directory.
RUN mkdir /usr/src/cache

WORKDIR /usr/src/cache

COPY package.json .

COPY package-lock.json .

RUN npm install

WORKDIR /frontend-order-app

COPY package.json .

COPY package-lock.json .

COPY . .

CMD ["/bin/sh", "-c", "./entrypoint.sh"]

