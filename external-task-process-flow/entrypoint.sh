#!/bin/bash

cp -r /usr/src/cache/node_modules/ /app/ && chown -R node:node /app/node_modules && node index.js
