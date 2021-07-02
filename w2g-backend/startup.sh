#!/bin/sh
/w2g-backend/bin/w2g-backend db migrate config.yml
/w2g-backend/bin/w2g-backend server config.yml

