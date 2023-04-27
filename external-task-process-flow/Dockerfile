FROM node:18-alpine3.15

# Create and define the node_modules's cache directory.
RUN mkdir -p /usr/src/cache

WORKDIR /usr/src/cache

COPY package.json .

COPY package-lock.json .

RUN npm install

WORKDIR /app

COPY entrypoint.sh .

COPY package.json .

COPY package-lock.json .

COPY . .

CMD ["/bin/sh", "/app/entrypoint.sh"]
