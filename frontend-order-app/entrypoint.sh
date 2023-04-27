#!/bin/bash



cp -r /usr/src/cache/node_modules/ /frontend-order-app/ && chown -R node:node /frontend-order-app/node_modules && npm run dev
